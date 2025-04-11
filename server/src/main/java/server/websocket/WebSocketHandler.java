package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private static final String UNICODE_ESCAPE = "\u001b";
    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    public static final String SET_TEXT_COLOR_BLUE = SET_TEXT_COLOR + "12m";
    public static final String RESET_TEXT_COLOR = UNICODE_ESCAPE + "[39m";

    private final ConnectionManager connections = new ConnectionManager();
    Gson serializer = new Gson();

    private final SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
    private final SQLGameDataAccess sqlGameDataAccess = new SQLGameDataAccess();

    public WebSocketHandler() {
        System.out.println("websockethandler instantiated");
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket connected: " + session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
//        System.out.println("Received WebSocket message: " + message);
//        System.out.println("First char: " + message.charAt(0));
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        AuthDataRecord auth = authDataAccess.getAuthData(command.authToken());
        GameDataRecord gameData = sqlGameDataAccess.getGame(command.gameID());

//        System.out.println("gameData.game(): " + gameData.game());
//        System.out.println("class: " + gameData.game().getClass().getName());

        switch (command.commandType()) {
            case CONNECT -> connect(session, command, gameData.game());
            case MAKE_MOVE -> make_move(session, command);
            case LEAVE -> leave(session, command);
            case RESIGN -> resign(session, command);
        }
    }



    private void connect(Session session, UserGameCommand UGC, ChessGame game) throws IOException{
       int gameID = UGC.gameID();

       connections.add(UGC.gameID(), UGC.username(), session);

       ChessGame.TeamColor pov;
        if (UGC.playerColor().equals("WHITE")) {
            pov = ChessGame.TeamColor.WHITE;
        } else {
            pov = ChessGame.TeamColor.BLACK;
        }

        try {
            ServerMessage load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game, pov);
            session.getRemote().sendString(serializer.toJson(load));

            String joinMessage = SET_TEXT_COLOR_BLUE + UGC.username() + RESET_TEXT_COLOR + " has joined the game.";
            Notifications notification = new Notifications(joinMessage, null, null);

            connections.broadcast(gameID, UGC.username(), notification);

        } catch (Exception exception) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setMessage(SET_TEXT_COLOR_BLUE + "Failed to connect: " + RESET_TEXT_COLOR + exception.getMessage());
            session.getRemote().sendString(serializer.toJson(error));
        }
    }

    private void make_move(Session session, UserGameCommand UGC) throws IOException {

        ChessGame.TeamColor pov;
        if (UGC.playerColor().equals("WHITE")) {
            pov = ChessGame.TeamColor.WHITE;
        } else {
            pov = ChessGame.TeamColor.BLACK;
        }

        String start = UGC.ogPos();
        String end = UGC.newPos();

        try {
            GameDataRecord updatedGameData = sqlGameDataAccess.getGame(UGC.gameID());
            ChessGame updatedGame = updatedGameData.game();

            System.out.println(updatedGame.getBoard().toString());

            ServerMessage load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame, pov);
            session.getRemote().sendString(serializer.toJson(load));

            String moveMess = SET_TEXT_COLOR_BLUE + UGC.username() + RESET_TEXT_COLOR +" moved the piece at " +
                    SET_TEXT_COLOR_BLUE + start + RESET_TEXT_COLOR + " to " + SET_TEXT_COLOR_BLUE + end +
                    RESET_TEXT_COLOR + ".";
            Notifications notifications = new Notifications(moveMess, null, null);
            connections.broadcast(UGC.gameID(), UGC.username(), notifications);

        } catch (Exception exception) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setMessage("Move error: " + exception.getMessage());
            session.getRemote().sendString(serializer.toJson(error));
        }

    }

    private void leave(Session session, UserGameCommand UGC) throws IOException {

        try {

            String message = SET_TEXT_COLOR_BLUE + UGC.username() + RESET_TEXT_COLOR + " has left the game";
            Notifications leaveMessage = new Notifications(message, null, null);
            connections.broadcast(UGC.gameID(), UGC.username(), leaveMessage);

        } catch (Exception exception) {

            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setMessage("Resignation error: " + exception.getMessage());
            session.getRemote().sendString(serializer.toJson(error));

        }

    }

    private void resign(Session session, UserGameCommand UGC) throws IOException {

        int gameID = UGC.gameID();

        try {

            String message = SET_TEXT_COLOR_BLUE + UGC.username() + RESET_TEXT_COLOR + " has resigned the game.";
            Notifications resignMessage = new Notifications(message, null, null);
            connections.broadcast(gameID, UGC.username(), resignMessage);
            connections.remove(gameID, UGC.username());

        } catch (Exception exception) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setMessage("Resignation error: " + exception.getMessage());
            session.getRemote().sendString(serializer.toJson(error));
        }
    }
}
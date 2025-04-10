package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


@WebSocket
public class WebSocketHandler {

    private static final String UNICODE_ESCAPE = "\u001b";
    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    public static final String SET_TEXT_COLOR_BLUE = SET_TEXT_COLOR + "12m";
    public static final String RESET_TEXT_COLOR = UNICODE_ESCAPE + "[39m";

    private final ConnectionManager connections = new ConnectionManager();
    Gson serializer = new Gson();

    private final HashMap<Integer, ArrayList<Session>> sessions = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();

        AuthDataRecord auth = authDataAccess.getAuthData(command.authToken());
        GameDataRecord gameData = gameDataAccess.getGame(command.gameID());

        if (auth == null) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            errorMessage.setMessage("Invalid" + SET_TEXT_COLOR_BLUE + "AuthToken" + RESET_TEXT_COLOR);
            session.getRemote().sendString(serializer.toJson(errorMessage));
            return;
        }

        if (gameData == null) {
            ServerMessage gameError = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            gameError.setMessage(SET_TEXT_COLOR_BLUE + "Game" + RESET_TEXT_COLOR + " not found.");
            session.getRemote().sendString(serializer.toJson(gameError));
            return;
        }

        ChessGame game = serializer.fromJson(gameData.game().toString(), ChessGame.class);

        switch (command.commandType()) {
            case CONNECT -> connect(session, command, game);
            case MAKE_MOVE -> make_move();
            case LEAVE -> leave();
            case RESIGN -> resign(session, command);
        }
    }

    private void connect(Session session, UserGameCommand UGC, ChessGame game) throws IOException{
       int gameID = UGC.gameID();

       sessions.putIfAbsent(gameID, new ArrayList<>());
       sessions.get(gameID).add(session);

        try {
            ServerMessage load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            session.getRemote().sendString(serializer.toJson(load));

            String joinMessage = SET_TEXT_COLOR_BLUE + UGC.username() + RESET_TEXT_COLOR + " has joined the game.";
            Notifications notification = new Notifications(joinMessage, null);

            for (Session sesh : sessions.get(gameID)) {
                if (sesh.isOpen() && !sesh.equals(session)) {
                    sesh.getRemote().sendString(serializer.toJson(notification));
                }
            }

//            connections.add(UGC.gameID(), UGC.username(), session);
//            ServerMessage load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
//            session.getRemote().sendString(serializer.toJson(load));

//            String joinMessage = SET_TEXT_COLOR_BLUE + UGC.username() + RESET_TEXT_COLOR + " has joined the game.";
//            Notifications notification = new Notifications(joinMessage, null);
//            connections.broadcast(gameData.gameID(), UGC.username(), notification);

        } catch (Exception exception) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            error.setMessage(SET_TEXT_COLOR_BLUE + "Failed to connect: " + RESET_TEXT_COLOR + exception.getMessage());
            session.getRemote().sendString(serializer.toJson(error));
        }
    }

    private void make_move() {

    }

    private void leave() {

    }

    private void resign(Session session, UserGameCommand UGC) throws IOException {

        int gameID = UGC.gameID();

        try {

            String message = SET_TEXT_COLOR_BLUE + UGC.username() + RESET_TEXT_COLOR + " has resigned the game.";
            Notifications resignMessage = new Notifications(message, null);

            for (Session sesh : sessions.get(gameID)) {
                if (sesh.isOpen()) {
                    sesh.getRemote().sendString(serializer.toJson(resignMessage));
                }
            }
        } catch (Exception exception) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            error.setMessage("Resignation error: " + exception.getMessage());
            session.getRemote().sendString(serializer.toJson(error));
        }
    }
}
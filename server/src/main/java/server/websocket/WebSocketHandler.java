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


@WebSocket
public class WebSocketHandler {

    private static final String UNICODE_ESCAPE = "\u001b";
    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    public static final String SET_TEXT_COLOR_BLUE = SET_TEXT_COLOR + "12m";
    public static final String RESET_TEXT_COLOR = UNICODE_ESCAPE + "[39m";

    private final ConnectionManager connections = new ConnectionManager();
    Gson serializer = new Gson();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command);
            case MAKE_MOVE -> make_move();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }

    private void connect(Session session, UserGameCommand UGC) throws IOException{
        try {
            SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
            SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();

            AuthDataRecord auth = authDataAccess.getAuthData(UGC.getAuthToken());
            GameDataRecord gameData = gameDataAccess.getGame(UGC.getGameID());

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
            connections.add(UGC.getGameID(), session);

            ServerMessage load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            session.getRemote().sendString(serializer.toJson(load));

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

    private void resign() {

    }
}
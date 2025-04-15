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
import java.util.Collection;

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

    private boolean resigned = false;

    public WebSocketHandler() {
        System.out.println("websockethandler instantiated");
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket connected: " + session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
//        System.out.println("Received WebSocket message: " + message);
//        System.out.println("First char: " + message.charAt(0));
//        System.out.println("Incoming message: " + message);
        UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);

//        if (command.gameID() == null) {
//            sendError(session, "Invalid gameID");
//            return;
//        }

//        System.out.println("gameID type: " + command.gameID().getClass().getName());
//        System.out.println("gameID value: " + command.gameID());

        try {
            AuthDataRecord auth = authDataAccess.getAuthData(command.authToken());
            if (auth == null) {
                String errorMessage = "Error: AuthData is null.";
                ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
                error.setErrorMessage(errorMessage);
                session.getRemote().sendString(serializer.toJson(error));
                return;
            }

//            System.out.println("Fetching game data for gameID: " + command.gameID());
            GameDataRecord gameData = sqlGameDataAccess.getGame(command.gameID());
            if (gameData == null) {
                String errorMessage = "Error: GameData is null.";
                ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
                error.setErrorMessage(errorMessage);
                session.getRemote().sendString(serializer.toJson(error));
                return;
            }
//            System.out.println("gameData: " + gameData);

//        System.out.println("gameData.game(): " + gameData.game());
//        System.out.println("class: " + gameData.game().getClass().getName());

//        System.out.println("Parsed command type: " + command.commandType());

            switch (command.commandType()) {
                case CONNECT -> connect(session, command, auth, gameData);
                case MAKE_MOVE -> make_move(session, command, auth, gameData, command.move());
                case LEAVE -> leave(session, command, auth, gameData);
                case RESIGN -> resign(session, command, auth, gameData);
            }
        } catch (DataAccessException | NullPointerException e) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage("Error processing command: " + e.getMessage());
            session.getRemote().sendString(serializer.toJson(error));
        }
    }

    private void connect(Session session, UserGameCommand UGC, AuthDataRecord authData, GameDataRecord gameData) throws Exception {
        try {
            ChessGame game = gameData.game();
            String blackUser = gameData.blackUsername();
            String whiteUser = gameData.whiteUsername();

            String username = getUser(UGC, authData.username());
            ChessGame.TeamColor pov = getPlayerColor(UGC, username, blackUser, whiteUser);

            int gameID = UGC.gameID();

            connections.add(UGC.gameID(), username, session);

            try {
                ServerMessage load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game, pov);
                session.getRemote().sendString(serializer.toJson(load));

                String joinMessage = SET_TEXT_COLOR_BLUE + username + RESET_TEXT_COLOR + " has joined the game.";

                ServerMessage connectNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, null);
                connectNotification.setMessage(joinMessage);
                connections.broadcast(gameID, username, connectNotification);

            } catch (Exception exception) {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
                errorMessage.setErrorMessage("Connect error: Unable to connect.");
                session.getRemote().sendString(serializer.toJson(errorMessage));
            }
        } catch (Exception exception) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            errorMessage.setErrorMessage("Connect error: " + exception.getMessage());
            String json = serializer.toJson(errorMessage);
            session.getRemote().sendString(json);
        }
    }

    private String getUser(UserGameCommand UGC, String username) {
        String newUser = "";
        if (UGC.username() == null) {
            newUser = username;
        }
        return newUser;
    }

    private static ChessGame.TeamColor getPlayerColor(UserGameCommand UGC, String username, String blackUser, String whiteUser) {
        ChessGame.TeamColor pov = null;
        if (UGC.username() == null) {
            if (username != null) {
                if (blackUser.equals(username)) {
                    pov = ChessGame.TeamColor.BLACK;
                } else if (whiteUser.equals(username)) {
                    pov = ChessGame.TeamColor.WHITE;
                }
            }
        } else {
            if (UGC.playerColor().equals("WHITE")) {
                pov = ChessGame.TeamColor.WHITE;
            } else if (UGC.playerColor().equals("BLACK")) {
                pov = ChessGame.TeamColor.BLACK;
            }
        }

        return pov;
    }

    private void make_move(Session session, UserGameCommand UGC, AuthDataRecord authData, GameDataRecord gameData, ChessMove move) throws IOException {

        String blackUser = gameData.blackUsername();
        String whiteUser = gameData.whiteUsername();

        String username = getUser(UGC, authData.username());
        ChessGame.TeamColor pov = getPlayerColor(UGC, username, blackUser, whiteUser);

        String start = UGC.ogPos();
        String end = UGC.newPos();

        ChessGame game = gameData.game();

        Collection<ChessMove> legalMoves = game.validMoves(move.getStartPosition());
        if (!legalMoves.contains(move)) {
            String errorMessage = "Error: Illegal move.";
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage(errorMessage);
            session.getRemote().sendString(serializer.toJson(error));
            return;
        }

        if (pov != game.getTeamTurn()) {
            String errorMessage = "Error: It's not your turn.";
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage(errorMessage);
            session.getRemote().sendString(serializer.toJson(error));
            return;
        }

        if (resigned) {
            String errorMessage = "Error: You can't make moves after resigning. Please enter return to return to game selection.";
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage(errorMessage);
            session.getRemote().sendString(serializer.toJson(error));
            return;
        }

        try {
            GameDataRecord updatedGameData = sqlGameDataAccess.getGame(UGC.gameID());
            ChessGame updatedGame = updatedGameData.game();

//            System.out.println(updatedGame.getBoard().toString());

            ServerMessage load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame, pov);
            session.getRemote().sendString(serializer.toJson(load));

            for (Session sesh : connections.getSession(UGC.gameID(), username)) {
                ServerMessage loadToOthers = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame, pov);
                sesh.getRemote().sendString(serializer.toJson(loadToOthers));
            }

            String moveMess = SET_TEXT_COLOR_BLUE + UGC.username() + RESET_TEXT_COLOR +" moved the piece at " +
                    SET_TEXT_COLOR_BLUE + start + RESET_TEXT_COLOR + " to " + SET_TEXT_COLOR_BLUE + end +
                    RESET_TEXT_COLOR + ".";
            ServerMessage notifications = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, null);
            notifications.setNotificationMessage(moveMess);
            connections.broadcast(UGC.gameID(), username, notifications);

        } catch (Exception exception) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage("Move error: invalid move.");
            session.getRemote().sendString(serializer.toJson(error));
        }
    }

    private void leave(Session session, UserGameCommand UGC, AuthDataRecord auth, GameDataRecord gameData) throws IOException {

        String username = getUser(UGC, auth.username());
        ChessGame.TeamColor pov = getPlayerColor(UGC, username, gameData.blackUsername(), gameData.whiteUsername());
        ChessGame game = gameData.game();


        try {

            String message = SET_TEXT_COLOR_BLUE + username + RESET_TEXT_COLOR + " has left the game";
            ServerMessage leaveMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, null);
//            leaveMessage.setNotifications(new Notifications(message, null, null));
            leaveMessage.setNotificationMessage(message);

            connections.broadcast(UGC.gameID(), username, leaveMessage);
            connections.remove(UGC.gameID(), username);

        } catch (Exception exception) {

            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage("Leave error: Unable to connect.");
            session.getRemote().sendString(serializer.toJson(error));

        }

    }

    private void resign(Session session, UserGameCommand UGC, AuthDataRecord auth, GameDataRecord gameData) throws IOException {

        int gameID = UGC.gameID();

        String username = auth.username();
        String whiteUser = gameData.whiteUsername();
        String blackUser = gameData.blackUsername();

        if (!username.equals(whiteUser) && !username.equals(blackUser)) {
            String errorMessage = "Observers cannot resign. Please enter return to return to game selection.";
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage(errorMessage);
            session.getRemote().sendString(serializer.toJson(error));
            return;
        }

        if (resigned) {
            String errorMessage = "Game over. Please enter return to return to game selection.";
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage(errorMessage);
            session.getRemote().sendString(serializer.toJson(error));
            return;
        }

        try {

            String message = SET_TEXT_COLOR_BLUE + UGC.username() + RESET_TEXT_COLOR + " has resigned the game.";
            ServerMessage resignMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, null);
            resignMessage.setNotificationMessage(message);
            connections.broadcast(gameID, UGC.username(), resignMessage);
            connections.remove(gameID, UGC.username());

            resigned = true;

        } catch (Exception exception) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage("Resign error: Unable to resign.");
            session.getRemote().sendString(serializer.toJson(error));
        }
    }

}
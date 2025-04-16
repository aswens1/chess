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

    public WebSocketHandler() {
        System.out.println("websockethandler instantiated");
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket connected: " + session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {

        UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);

        try {
            AuthDataRecord auth = authDataAccess.getAuthData(command.authToken());
            if (auth == null) {
                String errorMessage = "Error: AuthData is null.";
                ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
                error.setErrorMessage(errorMessage);
                session.getRemote().sendString(serializer.toJson(error));
                return;
            }

            GameDataRecord gameData = sqlGameDataAccess.getGame(command.gameID());
            if (gameData == null) {
                String errorMessage = "Error: GameData is null.";
                ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
                error.setErrorMessage(errorMessage);
                session.getRemote().sendString(serializer.toJson(error));
                return;
            }

            switch (command.commandType()) {
                case CONNECT -> connect(session, command, auth, gameData);
                case MAKE_MOVE -> makeMove(session, command, auth, gameData, command.move());
                case LEAVE -> leave(session, command, auth, gameData);
                case RESIGN -> resign(session, command, auth, gameData);
            }
        } catch (DataAccessException | NullPointerException e) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage("Error processing command: " + e.getMessage());
            session.getRemote().sendString(serializer.toJson(error));
        }
    }

    private void connect(Session session, UserGameCommand userGameCommand, AuthDataRecord authData, GameDataRecord gameData) throws Exception {
        try {
            ChessGame game = gameData.game();
            String blackUser = gameData.blackUsername();
            String whiteUser = gameData.whiteUsername();

            String username = getUser(userGameCommand, authData.username());
            ChessGame.TeamColor pov = getPlayerColor(userGameCommand, username, blackUser, whiteUser);

            int gameID = userGameCommand.gameID();

            connections.add(userGameCommand.gameID(), username, session);

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

    private String getUser(UserGameCommand userGameCommand, String username) {
        String newUser = "";
        if (userGameCommand.username() == null) {
            newUser = username;
        }
        return newUser;
    }

    private static ChessGame.TeamColor getPlayerColor(UserGameCommand userGameCommand, String username, String blackUser, String whiteUser) {
        ChessGame.TeamColor pov = null;

        if (whiteUser == null && !username.equals(blackUser)) {
            pov = ChessGame.TeamColor.WHITE;
            return pov;
        }

        if (userGameCommand.username() == null) {
            if (username != null) {
                if (blackUser.equals(username)) {
                    pov = ChessGame.TeamColor.BLACK;
                } else if (whiteUser.equals(username)) {
                    pov = ChessGame.TeamColor.WHITE;
                }
            }
        } else {
            if (userGameCommand.playerColor().equals("WHITE")) {
                pov = ChessGame.TeamColor.WHITE;
            } else if (userGameCommand.playerColor().equals("BLACK")) {
                pov = ChessGame.TeamColor.BLACK;
            }
        }

        return pov;
    }

    private void makeMove(Session session, UserGameCommand userGameCommand, AuthDataRecord authData,
                          GameDataRecord gameData, ChessMove move) throws IOException, InvalidMoveException {

        String blackUser = gameData.blackUsername();
        String whiteUser = gameData.whiteUsername();

        String username = getUser(userGameCommand, authData.username());
        ChessGame.TeamColor pov = getPlayerColor(userGameCommand, username, blackUser, whiteUser);

        String start = userGameCommand.ogPos();
        String end = userGameCommand.newPos();

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

        if (connections.playerResigned(userGameCommand.gameID(), username)) {
            String errorMessage = "Error: You can't make moves after resigning. Please enter return to return to game selection.";
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage(errorMessage);
            session.getRemote().sendString(serializer.toJson(error));
            return;
        }

        if (connections.isGameOver(userGameCommand.gameID())) {
            String errorMessage = "Error: Game over. Please enter return to return to game selection.";
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage(errorMessage);
            session.getRemote().sendString(serializer.toJson(error));
            return;
        }

        game.makeMove(move);
        sqlGameDataAccess.updateGameState(userGameCommand.gameID(), username, pov, game);


        try {
            GameDataRecord updatedGameData = sqlGameDataAccess.getGame(userGameCommand.gameID());
            ChessGame updatedGame = updatedGameData.game();

            boolean isCheckmate = updatedGame.isInCheckmate(game.getTeamTurn());
            boolean isStalemate = updatedGame.isInStalemate(game.getTeamTurn());

            if (isCheckmate || isStalemate) {
                connections.addGameOver(userGameCommand.gameID());
            }

            ServerMessage load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame, pov);
            session.getRemote().sendString(serializer.toJson(load));

            for (Session sesh : connections.getSession(userGameCommand.gameID(), username)) {
                ServerMessage loadToOthers = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame, pov);
                sesh.getRemote().sendString(serializer.toJson(loadToOthers));
            }

            String moveMess = SET_TEXT_COLOR_BLUE + userGameCommand.username() + RESET_TEXT_COLOR +" moved the piece at " +
                    SET_TEXT_COLOR_BLUE + start + RESET_TEXT_COLOR + " to " + SET_TEXT_COLOR_BLUE + end +
                    RESET_TEXT_COLOR + ".";
            ServerMessage notifications = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, null);
            notifications.setMessage(moveMess);
            connections.broadcast(userGameCommand.gameID(), username, notifications);

        } catch (Exception exception) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage("Move error: invalid move.");
            session.getRemote().sendString(serializer.toJson(error));
        }
    }

    private void leave(Session session, UserGameCommand userGameCommand, AuthDataRecord auth, GameDataRecord gameData) throws IOException {

        String username = getUser(userGameCommand, auth.username());
        ChessGame.TeamColor pov = getPlayerColor(userGameCommand, username, gameData.blackUsername(), gameData.whiteUsername());
        ChessGame game = gameData.game();

        sqlGameDataAccess.updateGamePlayer(userGameCommand.gameID(), null, pov, game);

        connections.clearResign(userGameCommand.gameID());

        try {

            connections.remove(userGameCommand.gameID(), username);


            String message = SET_TEXT_COLOR_BLUE + username + RESET_TEXT_COLOR + " has left the game";
            ServerMessage leaveMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, null);
            leaveMessage.setMessage(message);

            connections.broadcast(userGameCommand.gameID(), username, leaveMessage);

        } catch (Exception exception) {

            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage("Leave error: Unable to connect.");
            session.getRemote().sendString(serializer.toJson(error));

        }

    }

    private void resign(Session session, UserGameCommand userGameCommand, AuthDataRecord auth, GameDataRecord gameData) throws IOException {

        int gameID = userGameCommand.gameID();

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

        if (connections.playerResigned(gameID, username)) {
            String errorMessage = "Game over. Please enter return to return to game selection.";
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage(errorMessage);
            session.getRemote().sendString(serializer.toJson(error));
            return;
        }

        if (connections.isGameOver(gameID)) {
            String errorMessage = "The game is already over. Please enter return to return to game selection.";
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage(errorMessage);
            session.getRemote().sendString(serializer.toJson(error));
            return;
        }

        try {
            connections.resign(gameID, username);
            connections.remove(gameID, userGameCommand.username());
            connections.addGameOver(gameID);

            String message = SET_TEXT_COLOR_BLUE + userGameCommand.username() + RESET_TEXT_COLOR + " has resigned the game.";
            ServerMessage resignMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, null);
            resignMessage.setMessage(message);
            connections.broadcast(gameID, userGameCommand.username(), resignMessage);

        } catch (Exception exception) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
            error.setErrorMessage("Resign error: Unable to resign.");
            session.getRemote().sendString(serializer.toJson(error));
        }
    }

}
package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    ChessGame game;
    String errorMessage;
    ChessGame.TeamColor pov;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, ChessGame game, ChessGame.TeamColor pov) {
        this.serverMessageType = type;
        this.game = game;
        this.pov = pov;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public ChessGame.TeamColor getPov() {
        return this.pov;
    }

    public ChessGame getGame() {
        return this.game;
    }

    public String getMessage() {
        return this.errorMessage;
    }

//    public void setMessage(String message) {
//        this.message = message;
//    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return serverMessageType == that.serverMessageType && Objects.equals(game, that.game) && Objects.equals(errorMessage, that.errorMessage) && pov == that.pov;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverMessageType, game, errorMessage, pov);
    }

}

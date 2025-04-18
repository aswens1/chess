package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public record UserGameCommand(websocket.commands.UserGameCommand.CommandType commandType, String authToken,
                              Integer gameID, String playerColor, String username, String ogPos, String newPos, ChessMove move) {

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGameCommand that)) {
            return false;
        }
        return commandType() == that.commandType() &&
                Objects.equals(authToken(), that.authToken()) &&
                Objects.equals(gameID(), that.gameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType(), authToken(), gameID());
    }
}

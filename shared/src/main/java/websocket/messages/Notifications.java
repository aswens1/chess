package websocket.messages;

import chess.ChessGame;

public class Notifications extends ServerMessage{

    private final String message;
    private ChessGame chessGame;

    public Notifications(String message, ChessGame chessGame) {
        super(ServerMessageType.NOTIFICATION, chessGame);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

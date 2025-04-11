package websocket.messages;

import chess.ChessGame;

public class Notifications {

    private final String message;
    private final ChessGame chessGame;
    private final ChessGame.TeamColor pov;

    private final ServerMessage.ServerMessageType serverMessageType = ServerMessage.ServerMessageType.NOTIFICATION;


    public Notifications(String message, ChessGame chessGame, ChessGame.TeamColor pov) {
        this.message = message;
        this.chessGame = chessGame;
        this.pov = pov;
    }

    public String getMessage() {
        return this.message;
    }

    public ChessGame getChessGame() {
        return this.chessGame;
    }

    public ChessGame.TeamColor getPov() {
        return pov;
    }
}

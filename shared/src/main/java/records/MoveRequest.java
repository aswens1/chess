package records;

import chess.ChessGame;
import chess.ChessMove;

public record MoveRequest(int gameID, ChessMove move, String username, ChessGame.TeamColor pov) {
}

package chess;

import java.util.ArrayList;
import java.util.List;

public class BishopRookCalc {

    public static List<ChessMove> moveCalculator(ChessPosition startingPosition, int startRow, int startCol, int rowDirection,
                                                     int colDirection, ChessBoard board, ChessGame.TeamColor myTeam) {
        List<ChessMove> moves = new ArrayList<>();

        int row = startRow;
        int col = startCol;

        while ((row > 0 && row <= 8) && (col > 0 && col <= 8)) {
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece occupiedPiece = board.getPiece(newPosition);
            ChessMove newMove = new ChessMove(startingPosition, newPosition, null);

            if (occupiedPiece == null) {
                moves.add(newMove);
                row += rowDirection;
                col += colDirection;
            } else {
                if (occupiedPiece.getTeamColor() != myTeam) {
                    moves.add(newMove);
                }
                break;
            }
        }
        return moves;
    }

}

package chess;

import java.util.ArrayList;
import java.util.List;

public class Bishop implements PieceMovesCalculator {
    @Override
    public List<ChessPosition> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessPosition> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        if (piece == null || piece.getPieceType() != ChessPiece.PieceType.BISHOP) {
            // checks the spot is empty or if its not a bishop
            return moves;
        }
        return moves;
    }
}
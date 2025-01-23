package chess;

import java.util.ArrayList;
import java.util.List;

public class Queen {
    public static List<ChessMove> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessMove> possibleBishopMoves = Bishop.possibleMoves(position, board);
        List<ChessMove> possibleRookMoves = Rook.possibleMoves(position, board);
        possibleBishopMoves.addAll(possibleRookMoves);
        return possibleBishopMoves;
    }
}

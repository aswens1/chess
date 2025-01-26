package chess;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the easiest of them all and doesn't need to implement the PieceMovesCalculator interface.
 */
public class Queen {
    /**
     * The queen is unique in that she can do all the moves of a bishop and a rook. She doesn't need any of her own code,
     * and can just call on the moves of a bishop and a rook.
     * <p>
     * Lists can be combined into one.
     * @param position
     * @param board
     * @return a list of all the possible moves a queen can make
     */
    public static List<ChessMove> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessMove> possibleBishopMoves = Bishop.possibleMoves(position, board);
        List<ChessMove> possibleRookMoves = Rook.possibleMoves(position, board);
        possibleBishopMoves.addAll(possibleRookMoves);
        return possibleBishopMoves;
    }
}

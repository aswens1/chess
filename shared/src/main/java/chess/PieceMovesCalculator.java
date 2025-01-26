package chess;

import java.util.List;

/**
 * This is an interface that allows us to calculate the different moves a different piece can make. It's different from a class
 * because it is incredibly vague, and all it does is tell us the name of the method,
 * what parameters it can take in, and what the method returns.
 */
public interface PieceMovesCalculator {
    /**
    An interface to calculate the possible moves each piece can make

    @param position = the position of the piece in question
    @param board = the board and where all the pieces are
    @return a list of all the moves a piece can make
    */
    static List<ChessMove> possibleMoves (ChessPosition position, ChessBoard board){
        return null;
    };
}
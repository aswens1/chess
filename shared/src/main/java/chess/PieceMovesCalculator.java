package chess;

import java.util.Collections;
import java.util.List;

public interface PieceMovesCalculator {
    /**
    An interface to calculate the possible moves each piece can make

    @param position = the position of the piece in question
    @param board = the board and where all the pieces are
    @return where the piece can go
    */
    static List<ChessMove> possibleMoves (ChessPosition position, ChessBoard board){
        return null;
    };
}
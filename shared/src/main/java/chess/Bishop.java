package chess;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the PieceMovesCalculator interface. It compiles a list of all the possible moves the bishop
 * piece can make during its turn.
 * <p>
 * Bishops can move diagonally in all directions, and can move many pieces unless stopped by the edge of the board,
 * a piece from their team, or by taking a piece of the other team.
 */
public class Bishop implements PieceMovesCalculator {

    /**
     * Here is where the actual list of moves is calculated.
     * <p>
     * It's important to know the colour of the team the piece being checked is on.
     * <p>
     * Bishops can move in four directions. Up and to the right (+,+), up and to the left (-,+),
     * down and to the right (+,-), and down and to the left (-,-). It helps to imagine a chessboard as a coordinate plane.
     * <p>
     * Before any move is made, you want to see if that spot is empty or not. If it is, the bishop can keep moving until
     * its stopped. If it is not empty, that means that piece is as far as it can go (if it's not on its team).
     * <p>
     * Remember that the list of moves will only let you add a ChessMove type to it, and that a ChessMove
     * needs a ChessPosition to be initialised.
     * <p>
     * Make sure that you check every possible move that the bishop can make.
     * @param position
     * @param board
     * @return A list of the possible moves that a bishop can make
     */
    public static List<ChessMove> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessMove> moves = new ArrayList<>();

        ChessPiece piece = board.getPiece(position);
        // checks the spot is empty

        if (piece == null) {return moves;}

        ChessGame.TeamColor myColour = piece.getTeamColor();

        // up right
        moves.addAll(BishopRookCalc.moveCalculator(position, position.getRow() + 1, position.getColumn() + 1,
                1, 1, board, myColour));
        // down left
        moves.addAll(BishopRookCalc.moveCalculator(position,position.getRow() - 1, position.getColumn() - 1,
                -1, -1, board, myColour));
        // up left
        moves.addAll(BishopRookCalc.moveCalculator(position,position.getRow() + 1, position.getColumn() - 1,
                1, -1, board, myColour));

        // down right
        moves.addAll(BishopRookCalc.moveCalculator(position,position.getRow() - 1, position.getColumn() + 1,
                -1, 1, board, myColour));

        return moves;
    }
}
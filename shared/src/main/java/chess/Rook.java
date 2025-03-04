package chess;

import java.util.ArrayList;
import java.util.List;

import static chess.BishopRookCalc.moveCalculator;

/**
 * This class implements the PieceMovesCalculator interface. It compiles a list of all the moves a
 * rook can make during its turn.
 * <p>
 * Rooks can move until it's blocked in the up, down, right, and left directions.
 */
public class Rook implements PieceMovesCalculator{

    /**
     * Here is where the actual list of moves is calculated.
     * <p>
     * It's important to know the colour of the team the piece being checked is on.
     * <p>
     * Rooks can move continuously until blocked. They can move up in their column (0,+), right in their row (+,0),
     * down in their column (0,-), and left in their row (-,0).
     * <p>
     * The rook can move until it is blocked by a piece from its own team, the edge of the board, or by taking the spot
     * of a piece from the other team.
     * Remember that the list of moves will only let you add a ChessMove type to it, and that a ChessMove
     * needs a ChessPosition to be initialised.
     * <p>
     * Make sure that you check every possible move that the bishop can make.
     * @param position
     * @param board
     * @return a list of all the possible moves a rook can make on its turn
     */
    public static List<ChessMove> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessMove> moves = new ArrayList<>();

        ChessPiece piece = board.getPiece(position);

        // checks the spot is empty
        if (piece == null) {
            return moves;
        }

        ChessGame.TeamColor myColour = piece.getTeamColor();


        // up
        moves.addAll(BishopRookCalc.moveCalculator(position, position.getRow(), position.getColumn() + 1, 0, 1, board, myColour));
        // down
        moves.addAll(BishopRookCalc.moveCalculator(position, position.getRow(), position.getColumn() - 1, 0, -1, board, myColour));
        // left
        moves.addAll(BishopRookCalc.moveCalculator(position, position.getRow() - 1, position.getColumn(), -1, 0, board, myColour));
        // right
        moves.addAll(BishopRookCalc.moveCalculator(position, position.getRow() + 1, position.getColumn(), 1, 0, board, myColour));

    return moves;
    }
}


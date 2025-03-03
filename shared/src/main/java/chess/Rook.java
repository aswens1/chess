package chess;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the PieceMovesCalculator interface. It compiles a list of all the moves a
 * rook can make during its turn.
 * <p>
 * Rooks can move until it's blocked in the up, down, right, and left directions.
 */
public class Rook implements PieceMovesCalculator{

    public static List<ChessMove> rookMoveCalculator(ChessPosition startingPosition, int startRow, int startCol, int rowDirection,
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
        moves.addAll(rookMoveCalculator(position, position.getRow(), position.getColumn() + 1, 0, 1, board, myColour));
        // down
        moves.addAll(rookMoveCalculator(position, position.getRow(), position.getColumn() - 1, 0, -1, board, myColour));
        // left
        moves.addAll(rookMoveCalculator(position, position.getRow() - 1, position.getColumn(), -1, 0, board, myColour));
        // right
        moves.addAll(rookMoveCalculator(position, position.getRow() + 1, position.getColumn(), 1, 0, board, myColour));

    return moves;
    }
}


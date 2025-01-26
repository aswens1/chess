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
        ChessGame.TeamColor myColour = board.getPiece(position).getTeamColor();

        // checks the spot is empty
        if (piece == null) {
            return moves;
        }

        // checks all the moves going vertically up
        int upRow = position.getRow() + 1;
        int column = position.getColumn();

        while ((upRow > 0) && (upRow <= 8)) {
            ChessPosition upPosition = new ChessPosition(upRow, column);
            ChessPiece occupiedPieceUp = board.getPiece(upPosition);
            ChessMove newMoveUp = new ChessMove(position, upPosition, null);

            if (occupiedPieceUp == null) {
                moves.add(newMoveUp);
                upRow++;
            } else {
                if (occupiedPieceUp.getTeamColor() != myColour) {
                    moves.add(newMoveUp);
                }
                break;
            }
        }

        //checks all the moves going vertically down
        int downRow = position.getRow() - 1;

        while ((downRow > 0) && (downRow <= 8)) {
            ChessPosition downPosition = new ChessPosition(downRow, column);
            ChessPiece occupiedPieceDown = board.getPiece(downPosition);
            ChessMove newMoveDown = new ChessMove(position, downPosition, null);

            if (occupiedPieceDown == null) {
                moves.add(newMoveDown);
                downRow--;
            } else {
                if (occupiedPieceDown.getTeamColor() != myColour) {
                    moves.add(newMoveDown);
                }
                break;
            }
        }

        // checks all the moves going to the left
        int row = position.getRow();
        int leftColumn = position.getColumn() - 1;

        while ((leftColumn > 0) && (leftColumn <= 8)) {
            ChessPosition leftPosition = new ChessPosition(row, leftColumn);
            ChessPiece occupiedPieceLeft = board.getPiece(leftPosition);
            ChessMove newMoveLeft = new ChessMove(position, leftPosition, null);

            if (occupiedPieceLeft == null) {
                moves.add(newMoveLeft);
                leftColumn--;
            } else {
                if (occupiedPieceLeft.getTeamColor() != myColour) {
                    moves.add(newMoveLeft);
                }
                break;
            }
        }

        // checks all the moves going right
        int rightColumn = position.getColumn() + 1;

        while ((rightColumn > 0) && (rightColumn <= 8)) {
            ChessPosition rightPosition = new ChessPosition(row, rightColumn);
            ChessPiece occupiedPieceRight = board.getPiece(rightPosition);
            ChessMove newMoveRight = new ChessMove(position, rightPosition, null);

            if (occupiedPieceRight == null) {
                moves.add(newMoveRight);
                rightColumn++;
            } else {
                if (occupiedPieceRight.getTeamColor() != myColour) {
                    moves.add(newMoveRight);
                }
                break;
            }
        }

    return moves;
    }
}


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

        ChessGame.TeamColor myColour = board.getPiece(position).getTeamColor();

        // checks the spot is empty
        if (piece == null) {
            return moves;
        }


        // checks all the moves going up and to the right
        int upRightRow = position.getRow() + 1;
        int upRightColumn = position.getColumn() + 1;

        while(upRightRow > 0 && upRightRow <= 8 && upRightColumn > 0 && upRightColumn <= 8) {
            ChessPosition upRightPosition = new ChessPosition(upRightRow, upRightColumn);
            ChessPiece occupiedPieceUR = board.getPiece(upRightPosition);
            ChessMove newMoveUR = new ChessMove(position, upRightPosition, null);

            if (occupiedPieceUR == null) {
                moves.add(newMoveUR);
                upRightRow++;
                upRightColumn++;
            } else {
                if (occupiedPieceUR.getTeamColor() != myColour) {
                    moves.add(newMoveUR);
                }
                break;
            }
            System.out.println("Moves: " + moves);
        }


        // checks all the moves going down and to the right
        int downRightRow = position.getRow() - 1;
        int downRightColumn = position.getColumn() + 1;

        while(downRightRow > 0 && downRightRow <= 8 && downRightColumn > 0 && downRightColumn <= 8) {
            ChessPosition downRightPosition = new ChessPosition(downRightRow, downRightColumn);
            ChessPiece occupiedPieceDR = board.getPiece(downRightPosition);
            ChessMove newMoveDR = new ChessMove(position, downRightPosition, null);

            if (occupiedPieceDR == null) {
                moves.add(newMoveDR);
                downRightRow--;
                downRightColumn++;
            } else {
                if (occupiedPieceDR.getTeamColor() != myColour) {
                    moves.add(newMoveDR);
                }
                break;
            }
            System.out.println("Moves: " + moves);
        }

        // checks all the moves going down and to the left
        int downLeftRow = position.getRow() - 1;
        int downLeftColumn = position.getColumn() - 1;

        while(downLeftRow > 0 && downLeftRow <= 8 && downLeftColumn > 0 && downLeftColumn <= 8) {
            ChessPosition downLeftPosition = new ChessPosition(downLeftRow, downLeftColumn);
            ChessPiece occupiedPieceDL = board.getPiece(downLeftPosition);
            ChessMove newMoveDL = new ChessMove(position, downLeftPosition, null);

            if (occupiedPieceDL == null) {
                moves.add(newMoveDL);
                downLeftRow--;
                downLeftColumn--;
            } else {
                if (occupiedPieceDL.getTeamColor() != myColour) {
                    moves.add(newMoveDL);
                }
                break;
            }
            System.out.println("Moves: " + moves);
        }

        // checks all the moves going up and to the left
        int upLeftRow = position.getRow() + 1;
        int upLeftColumn = position.getColumn() - 1;

        while(upLeftRow > 0 && upLeftRow <= 8 && upLeftColumn > 0 && upLeftColumn <= 8) {
            ChessPosition upLeftPosition = new ChessPosition(upLeftRow, upLeftColumn);
            ChessPiece occupiedPieceUL = board.getPiece(upLeftPosition);
            ChessMove newMoveUL = new ChessMove(position, upLeftPosition, null);

            if (occupiedPieceUL == null) {
                moves.add(newMoveUL);
                upLeftRow++;
                upLeftColumn--;
            } else {
                if (occupiedPieceUL.getTeamColor() != myColour) {
                    moves.add(newMoveUL);
                }
                break;
            }
        }
        System.out.println("Moves: " + moves);
        return moves;
    }
}
package chess;

import java.util.ArrayList;
import java.util.List;

public class Rook implements PieceMovesCalculator{
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
            } else if (occupiedPieceUp == piece) {
                upRow++;
            } else {
                if (occupiedPieceUp.getTeamColor() != myColour) {
                    moves.add(newMoveUp);
                }
                break;
            }
            System.out.println("Moves: " + moves);
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
            } else if (occupiedPieceDown == piece) {
                downRow--;
            } else {
                if (occupiedPieceDown.getTeamColor() != myColour) {
                    moves.add(newMoveDown);
                }
                break;
            }
            System.out.println("Moves: " + moves);
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
            } else if (occupiedPieceLeft == piece) {
                leftColumn--;
            } else {
                if (occupiedPieceLeft.getTeamColor() != myColour) {
                    moves.add(newMoveLeft);
                }
                break;
            }
            System.out.println("Moves: " + moves);

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
            } else if (occupiedPieceRight == piece) {
                rightColumn++;
            } else {
                if (occupiedPieceRight.getTeamColor() != myColour) {
                    moves.add(newMoveRight);
                }
                break;
            }
            System.out.println("Moves: " + moves);

        }

    return moves;
    }
}


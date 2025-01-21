package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop implements PieceMovesCalculator {

    public static List<ChessMove> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);

        ChessGame.TeamColor myColour = board.getPiece(position).getTeamColor();

        if (piece == null || piece.getPieceType() != ChessPiece.PieceType.BISHOP) {
            // checks the spot is empty or if it's not a bishop
            return moves;
        }

        int zeroRowUpRight = position.getRow() - 1;
        int zeroColumnUpRight = position.getColumn() - 1;

        // checks all the moves going up and to the right
        int upRightRow = position.getRow() + 1;
        int upRightColumn = position.getColumn() + 1;

        while(zeroRowUpRight >= 0 && zeroRowUpRight < 8 && zeroColumnUpRight >= 0 && zeroColumnUpRight < 8) {
            ChessPosition zeroPositionUR = new ChessPosition(zeroRowUpRight, zeroColumnUpRight);
            ChessPosition upRightPosition = new ChessPosition(upRightRow, upRightColumn);
            ChessPiece occupiedPieceUR = board.getPiece(zeroPositionUR);
            ChessMove newMoveUR = new ChessMove(position, upRightPosition, null);

            if (occupiedPieceUR == null) {
                moves.add(newMoveUR);
                upRightRow++;
                upRightColumn++;

                zeroRowUpRight++;
                zeroColumnUpRight++;
            } else if (occupiedPieceUR == piece) {
                zeroRowUpRight++;
                zeroColumnUpRight++;
            } else {
                if (occupiedPieceUR.getTeamColor() != myColour) {
                    moves.add(newMoveUR);
                }
                break;
            }
        }

        // checks all the moves going down and to the right
        int zeroRowDownRight = position.getRow() - 1;
        int zeroColumnDownRight = position.getColumn() - 1;

        int downRightRow = position.getRow() - 1;
        int downRightColumn = position.getColumn() + 1;

        while(zeroRowDownRight > 0 && zeroRowDownRight < 8 && zeroColumnDownRight > 0 && zeroColumnDownRight < 8) {
            ChessPosition zeroPositionDR = new ChessPosition(zeroRowDownRight, zeroColumnDownRight);
            ChessPosition downRightPosition = new ChessPosition(downRightRow, downRightColumn);
            ChessPiece occupiedPieceDR = board.getPiece(zeroPositionDR);
            ChessMove newMoveDR = new ChessMove(position, downRightPosition, null);

            if (occupiedPieceDR == null) {
                moves.add(newMoveDR);
                downRightRow--;
                downRightColumn++;

                zeroRowDownRight--;
                zeroColumnDownRight++;
            } else if (occupiedPieceDR == piece) {
                zeroRowDownRight--;
                zeroColumnDownRight++;
            } else {
                if (occupiedPieceDR.getTeamColor() != myColour) {
                    moves.add(newMoveDR);
                }
                break;
            }
        }

        // checks all the moves going down and to the left
        int zeroRowDownLeft = position.getRow() - 1;
        int zeroColumnDownLeft = position.getColumn() - 1;

        int downLeftRow = position.getRow() - 1;
        int downLeftColumn = position.getColumn() - 1;

        while(zeroRowDownLeft > 0 && zeroRowDownLeft < 8 && zeroColumnDownLeft > 0 && zeroColumnDownLeft < 8) {
            ChessPosition zeroPositionDL = new ChessPosition(zeroRowDownLeft, zeroColumnDownLeft);
            ChessPosition downLeftPosition = new ChessPosition(downLeftRow, downLeftColumn);
            ChessPiece occupiedPieceDL = board.getPiece(zeroPositionDL);
            ChessMove newMoveDL = new ChessMove(position, downLeftPosition, null);

            if (occupiedPieceDL == null) {
                moves.add(newMoveDL);
                downLeftRow--;
                downLeftColumn--;

                zeroRowDownLeft--;
                zeroColumnDownLeft--;
            } else if (occupiedPieceDL == piece) {
                zeroRowDownLeft--;
                zeroColumnDownLeft--;
            } else {
                if (occupiedPieceDL.getTeamColor() != myColour) {
                    moves.add(newMoveDL);
                }
                break;
            }
        }

        // checks all the moves going up and to the left
        int zeroRowUpLeft = position.getRow() - 1;
        int zeroColumnUpLeft = position.getColumn() - 1;

        int upLeftRow = position.getRow() + 1;
        int upLeftColumn = position.getColumn() - 1;

        while(zeroRowUpLeft > 0 && zeroRowUpLeft < 8 && zeroColumnUpLeft > 0 && zeroColumnUpLeft < 8) {
            ChessPosition zeroPositionUL = new ChessPosition(zeroRowUpLeft, zeroColumnUpLeft);
            ChessPosition upLeftPosition = new ChessPosition(upLeftRow, upLeftColumn);
            ChessPiece occupiedPieceUL = board.getPiece(zeroPositionUL);
            ChessMove newMoveUL = new ChessMove(position, upLeftPosition, null);

            if (occupiedPieceUL == null) {
                moves.add(newMoveUL);
                upLeftRow++;
                upLeftColumn--;

                zeroRowUpLeft++;
                zeroColumnUpLeft--;
            } else if (occupiedPieceUL == piece) {
                zeroRowUpLeft++;
                zeroColumnUpLeft--;
            } else {
                if (occupiedPieceUL.getTeamColor() != myColour) {
                    moves.add(newMoveUL);
                }
                break;
            }
        }

        return moves;
    }
}
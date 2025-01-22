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

            } else if (occupiedPieceUR == piece) {
                upRightRow++;
                upRightColumn++;
            } else {
                if (occupiedPieceUR.getTeamColor() != myColour) {
                    moves.add(newMoveUR);
                }
                break;
            }
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
            } else if (occupiedPieceDR == piece) {
                downRightRow--;
                downRightColumn++;
            } else {
                if (occupiedPieceDR.getTeamColor() != myColour) {
                    moves.add(newMoveDR);
                }
                break;
            }
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
            } else if (occupiedPieceDL == piece) {
                downLeftRow--;
                downLeftColumn--;
            } else {
                if (occupiedPieceDL.getTeamColor() != myColour) {
                    moves.add(newMoveDL);
                }
                break;
            }
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
            } else if (occupiedPieceUL == piece) {
                upLeftRow++;
                upLeftColumn--;
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
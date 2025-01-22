package chess;

import java.util.ArrayList;
import java.util.List;

public class King implements PieceMovesCalculator{
    public static List<ChessMove> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessMove> moves = new ArrayList<>();

        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor myColour = board.getPiece(position).getTeamColor();

        // checks if the spot is empty
        if (piece == null) {
            return moves;
        }

        int row = position.getRow();
        int column = position.getColumn();
        int upOne = position.getRow() + 1;
        int downOne = position.getRow() - 1;
        int leftOne = position.getColumn() - 1;
        int rightOne = position.getColumn() + 1;

        // check moves 1 up, 1 up and to the right, and 1 up and to the left
        if (upOne <= 8) {
            ChessPosition upPosition = new ChessPosition(upOne, column);
            ChessPiece occupiedPiece = board.getPiece(upPosition);
            ChessMove upOneMove = new ChessMove(position, upPosition, null);
            if (occupiedPiece == null) {
                moves.add(upOneMove);
            } else if (occupiedPiece.getTeamColor() != myColour) {
                moves.add(upOneMove);
            }

            if (rightOne <= 8) {
                ChessPosition upRightPosition = new ChessPosition(upOne, rightOne);
                ChessPiece occupiedPieceRight = board.getPiece(upRightPosition);
                ChessMove rightUpMove = new ChessMove(position, upRightPosition, null);
                if (occupiedPieceRight == null) {
                    moves.add(rightUpMove);
                } else if (occupiedPieceRight.getTeamColor() != myColour) {
                    moves.add(rightUpMove);
                }
            }

            if (leftOne >= 1) {
                ChessPosition upLeftPosition = new ChessPosition(upOne, leftOne);
                ChessPiece occupiedPieceLeft = board.getPiece(upLeftPosition);
                ChessMove leftUpMove = new ChessMove(position, upLeftPosition, null);
                if (occupiedPieceLeft == null) {
                    moves.add(leftUpMove);
                } else if (occupiedPieceLeft.getTeamColor() != myColour) {
                    moves.add(leftUpMove);
                }
            }
        }

        // check moves 1 down, 1 down and to the right, 1 down and to the left
        if (downOne >= 1) {
            ChessPosition downPosition = new ChessPosition(downOne, column);
            ChessPiece occupiedPiece = board.getPiece(downPosition);
            ChessMove downOneMove = new ChessMove(position, downPosition, null);
            if (occupiedPiece == null) {
                moves.add(downOneMove);
            } else if (occupiedPiece.getTeamColor() != myColour) {
                moves.add(downOneMove);
            }

            if (rightOne <= 8) {
                ChessPosition downRightPosition = new ChessPosition(downOne, rightOne);
                ChessPiece occupiedPieceRight = board.getPiece(downRightPosition);
                ChessMove rightDownMove = new ChessMove(position, downRightPosition, null);
                if (occupiedPieceRight == null) {
                    moves.add(rightDownMove);
                } else if (occupiedPieceRight.getTeamColor() != myColour) {
                    moves.add(rightDownMove);
                }
            }

            if (leftOne >= 1) {
                ChessPosition downLeftPosition = new ChessPosition(downOne, leftOne);
                ChessPiece occupiedPieceLeft = board.getPiece(downLeftPosition);
                ChessMove leftDownMove = new ChessMove(position, downLeftPosition, null);
                if (occupiedPieceLeft == null) {
                    moves.add(leftDownMove);
                } else if (occupiedPieceLeft.getTeamColor() != myColour) {
                    moves.add(leftDownMove);
                }
            }
        }

        // check move 1 to the right
        if (rightOne <= 8) {
            ChessPosition rightPosition = new ChessPosition(row, rightOne);
            ChessPiece occupiedPieceRight = board.getPiece(rightPosition);
            ChessMove rightMove = new ChessMove(position, rightPosition, null);
            if (occupiedPieceRight == null) {
                moves.add(rightMove);
            } else if (occupiedPieceRight.getTeamColor() != myColour) {
                moves.add(rightMove);
            }
        }

        // check move 1 to the left
        if (leftOne >= 1) {
            ChessPosition leftPosition = new ChessPosition(row, leftOne);
            ChessPiece occupiedPieceLeft = board.getPiece(leftPosition);
            ChessMove leftMove = new ChessMove(position, leftPosition, null);
            if (occupiedPieceLeft == null) {
                moves.add(leftMove);
            } else if (occupiedPieceLeft.getTeamColor() != myColour) {
                moves.add(leftMove);
            }
        }
        return moves;
    }
}

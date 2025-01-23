package chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn implements PieceMovesCalculator {
    public static List<ChessMove> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessMove> moves = new ArrayList<>();

        ChessPiece piece = board.getPiece(position);

        ChessGame.TeamColor myColour = board.getPiece(position).getTeamColor();

        int row = position.getRow();
        int column = position.getColumn();

        // checks the spot is empty
        if (piece == null) {
            return moves;
        }

        int checkLeft = column - 1;
        int checkRight = column + 1;

        // white pawns move up the board
        int whiteRow = position.getRow() + 1;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            // if the pawn is in the 7th row, it can move one or two pieces forwards
            if (row == 2) {
                ChessPosition pawnFirstPositionMoveOne = new ChessPosition(whiteRow, column);
                ChessPosition pawnFirstPositionMoveTwo = new ChessPosition(whiteRow + 1, column);
                ChessMove pawnFirstMoveOne = new ChessMove(position, pawnFirstPositionMoveOne, null);
                ChessMove pawnFirstMoveTwo = new ChessMove(position, pawnFirstPositionMoveTwo, null);

                if (board.getPiece(pawnFirstPositionMoveOne) == null) {
                    moves.add(pawnFirstMoveOne);
                    if (board.getPiece(pawnFirstPositionMoveTwo) == null) {
                        moves.add(pawnFirstMoveTwo);
                    }
                }

                ChessPosition positionLeft = new ChessPosition(whiteRow, checkLeft);
                ChessPosition positionRight = new ChessPosition(whiteRow, checkRight);

                ChessMove captureLeft = new ChessMove(position, positionLeft, null);
                ChessMove captureRight = new ChessMove(position, positionRight, null);

                ChessPiece opLeft = board.getPiece(positionLeft);
                ChessPiece opRight = board.getPiece(positionRight);

                if (opLeft != null) {
                    if (opLeft.getTeamColor() != myColour) {
                        moves.add(captureLeft);
                    }
                }
                if (opRight != null) {
                    if (opRight.getTeamColor() != myColour) {
                        moves.add(captureRight);
                    }
                }
            }

            if (row != 2){
                if (row <= 7) {
                    ChessPosition positionOne = new ChessPosition(whiteRow, column);
                    ChessPiece occupiedPiece = board.getPiece(positionOne);
                    ChessMove moveOne = new ChessMove(position, positionOne, null);

                    ChessPosition positionLeft = new ChessPosition(whiteRow, checkLeft);
                    ChessPosition positionRight = new ChessPosition(whiteRow, checkRight);

                    ChessMove captureLeft = new ChessMove(position, positionLeft, null);
                    ChessMove captureRight = new ChessMove(position, positionRight, null);


                    if (occupiedPiece == null) {
                        moves.add(moveOne);
                    }

                    ChessPiece opLeft = board.getPiece(positionLeft);
                    ChessPiece opRight = board.getPiece(positionRight);
                    if (opLeft != null) {
                        if (opLeft.getTeamColor() != myColour) {
                            moves.add(captureLeft);
                        }
                    }
                    if (opRight != null) {
                        if (opRight.getTeamColor() != myColour) {
                            moves.add(captureRight);
                        }
                    }
                }
            }
        }

        // black pawns move down the board
        int blackRow = position.getRow() - 1;
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            // if the pawn is in the 7th row, it can move one or two pieces forwards
            if (row == 7) {
                ChessPosition pawnFirstPositionMoveOne = new ChessPosition(blackRow, column);
                ChessPosition pawnFirstPositionMoveTwo = new ChessPosition(blackRow - 1, column);
                ChessMove pawnFirstMoveOne = new ChessMove(position, pawnFirstPositionMoveOne, null);
                ChessMove pawnFirstMoveTwo = new ChessMove(position, pawnFirstPositionMoveTwo, null);
                if (board.getPiece(pawnFirstPositionMoveOne) == null) {
                    moves.add(pawnFirstMoveOne);
                    if (board.getPiece(pawnFirstPositionMoveTwo) == null) {
                        moves.add(pawnFirstMoveTwo);
                    }
                }

                ChessPosition positionLeft = new ChessPosition(blackRow, checkLeft);
                ChessPosition positionRight = new ChessPosition(blackRow, checkRight);

                ChessMove captureLeft = new ChessMove(position, positionLeft, null);
                ChessMove captureRight = new ChessMove(position, positionRight, null);

                ChessPiece opLeft = board.getPiece(positionLeft);
                ChessPiece opRight = board.getPiece(positionRight);

                if (opLeft != null) {
                    if (opLeft.getTeamColor() != myColour) {
                        moves.add(captureLeft);
                    }
                }
                if (opRight != null) {
                    if (opRight.getTeamColor() != myColour) {
                        moves.add(captureRight);
                    }
                }
            }

            if (row != 7){
                if (row >= 1) {
                    ChessPosition positionOne = new ChessPosition(blackRow, column);
                    ChessPiece occupiedPiece = board.getPiece(positionOne);
                    ChessMove moveOne = new ChessMove(position, positionOne, null);

                    ChessPosition positionLeft = new ChessPosition(blackRow, checkLeft);
                    ChessPosition positionRight = new ChessPosition(blackRow, checkRight);

                    ChessMove captureLeft = new ChessMove(position, positionLeft, null);
                    ChessMove captureRight = new ChessMove(position, positionRight, null);


                    if (occupiedPiece == null) {
                        moves.add(moveOne);
                    }

                    ChessPiece opLeft = board.getPiece(positionLeft);
                    ChessPiece opRight = board.getPiece(positionRight);
                    if (opLeft != null) {
                        if (opLeft.getTeamColor() != myColour) {
                            moves.add(captureLeft);
                        }
                    }
                    if (opRight != null) {
                        if (opRight.getTeamColor() != myColour) {
                           moves.add(captureRight);
                        }
                    }
                }
            }
        }





        return moves;
    }
}

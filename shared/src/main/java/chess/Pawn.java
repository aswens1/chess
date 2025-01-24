package chess;

import java.util.ArrayList;
import java.util.Collections;
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
            // if the pawn is in the 2nd row, it can move one or two pieces forwards
            if (row == 2) {
                //making sure its only in columns 2-7
                if (column > 1 && column < 8) {
                    ChessPosition pawnFirstPositionMoveOne = new ChessPosition(whiteRow, column);
                    ChessPosition pawnFirstPositionMoveTwo = new ChessPosition(whiteRow + 1, column);
                    ChessMove pawnFirstMoveOne = new ChessMove(position, pawnFirstPositionMoveOne, null);
                    ChessMove pawnFirstMoveTwo = new ChessMove(position, pawnFirstPositionMoveTwo, null);

                    // if no piece in spot ahead, add moves
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

                    // checking and capturing piece one up and to the left
                    if (opLeft != null) {
                        if (opLeft.getTeamColor() != myColour) {
                            moves.add(captureLeft);
                        }
                    }
                    // checking and capturing piece one up and to the right
                    if (opRight != null) {
                        if (opRight.getTeamColor() != myColour) {
                            moves.add(captureRight);
                        }
                    }
                    // takes care of the edge columns
                } else if (column == 1 || column == 8) {
                    ChessPosition pawnFirstPositionMoveOne = new ChessPosition(whiteRow, column);
                    ChessPosition pawnFirstPositionMoveTwo = new ChessPosition(whiteRow + 1, column);

                    ChessMove pawnFirstMoveOne = new ChessMove(position, pawnFirstPositionMoveOne, null);
                    ChessMove pawnFirstMoveTwo = new ChessMove(position, pawnFirstPositionMoveTwo, null);
                    // if position ahead clear, offer moves
                    if (board.getPiece(pawnFirstPositionMoveOne) == null) {
                        moves.add(pawnFirstMoveOne);
                        if (board.getPiece(pawnFirstPositionMoveTwo) == null) {
                            moves.add(pawnFirstMoveTwo);
                        }
                    }
                    // if in the far left column
                    if (column == 1) {
                        ChessPosition positionRight = new ChessPosition(whiteRow, checkRight);
                        ChessMove captureRight = new ChessMove(position, positionRight, null);
                        ChessPiece opRight = board.getPiece(positionRight);

                        // checking only the piece one up and to the right
                        if (opRight != null) {
                            if (opRight.getTeamColor() != myColour) {
                                moves.add(captureRight);
                            }
                        }
                    } else {
                        ChessPosition positionLeft = new ChessPosition(whiteRow, checkLeft);
                        ChessMove captureLeft = new ChessMove(position, positionLeft, null);
                        ChessPiece opLeft = board.getPiece(positionLeft);

                        if (opLeft != null) {
                            if (opLeft.getTeamColor() != myColour) {
                                moves.add(captureLeft);
                            }
                        }
                    }
                }
                return moves;
            }
            // does the move only one space if the row is not 2
            if (row != 2){
                if (row < 7) {
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
                    // handles overtaking a piece between columns 2 and 7 inclusive
                    if (column > 1 && column < 8) {
                        ChessPiece opLeft = board.getPiece(positionLeft);
                        ChessPiece opRight = board.getPiece(positionRight);
                        // up and to the left
                        if (opLeft != null) {
                            if (opLeft.getTeamColor() != myColour) {
                                moves.add(captureLeft);
                            }
                        }
                        // up and to the right
                        if (opRight != null) {
                            if (opRight.getTeamColor() != myColour) {
                                moves.add(captureRight);
                            }
                        }
                    }
                    // if in far left column
                    if (column == 1) {
                        ChessPiece opRight = board.getPiece(positionRight);
                        if (opRight != null) {
                            if (opRight.getTeamColor() != myColour) {
                                moves.add(captureRight);
                            }
                        }
                    }
                    // if in far right column
                    if (column == 8) {
                        ChessPiece opLeft = board.getPiece(positionLeft);
                        if (opLeft != null) {
                            if (opLeft.getTeamColor() != myColour) {
                                moves.add(captureLeft);
                            }
                        }
                    }
                    // if the row is seven, promotions
                    return moves;
                } else {
                    ChessPosition promotionPosition = new ChessPosition(whiteRow, column);
                    ChessPiece occupied = board.getPiece(promotionPosition);
                    List<ChessPiece.PieceType> promotionOptions = new ArrayList<>();
                    Collections.addAll(promotionOptions, ChessPiece.PieceType.KNIGHT,
                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
                            ChessPiece.PieceType.ROOK);
                    // promotions making one step forward to end of board
                    if (occupied == null) {
                        for (ChessPiece.PieceType pieceType : promotionOptions) {
                            ChessMove promotionMove = new ChessMove(position, promotionPosition, pieceType);
                            moves.add(promotionMove);
                        }
                    }
                    // promotions capturing a piece
                    if (column > 1 && column < 8) {
                        ChessPosition positionLeft = new ChessPosition(whiteRow, checkLeft);
                        ChessPosition positionRight = new ChessPosition(whiteRow, checkRight);

                        ChessPiece opLeft = board.getPiece(positionLeft);
                        ChessPiece opRight = board.getPiece(positionRight);
                        // one up and to the right
                        if (opRight != null) {
                            for (ChessPiece.PieceType pieceType : promotionOptions) {
                                ChessMove promotionMove = new ChessMove(position, positionRight, pieceType);
                                moves.add(promotionMove);
                            }
                        }
                        // one up and to the left
                        if (opLeft != null) {
                            for (ChessPiece.PieceType pieceType : promotionOptions) {
                                ChessMove promotionMove = new ChessMove(position, positionLeft, pieceType);
                                moves.add(promotionMove);
                            }
                        }
                        // promotions for pieces in the 1st and 8th column
                    } else {

                        if (column == 1) {
                            ChessPosition positionRight = new ChessPosition(whiteRow, checkRight);
                            ChessPiece opRight = board.getPiece(positionRight);
                            if (opRight != null) {
                                if (opRight.getTeamColor() != myColour) {
                                    for (ChessPiece.PieceType pieceType : promotionOptions) {
                                        ChessMove promotionMove = new ChessMove(position, positionRight, pieceType);
                                        moves.add(promotionMove);
                                    }
                                }
                            }
                        }

                        // if in far right column
                        if (column == 8) {
                            ChessPosition positionLeft = new ChessPosition(whiteRow, checkLeft);
                            ChessPiece opLeft = board.getPiece(positionLeft);
                            if (opLeft != null) {
                                if (opLeft.getTeamColor() != myColour) {
                                    for (ChessPiece.PieceType pieceType : promotionOptions) {
                                        ChessMove promotionMove = new ChessMove(position, positionLeft, pieceType);
                                        moves.add(promotionMove);
                                    }
                                }
                            }
                        }
                    }
                    return moves;
                }
            }
        }

        int blackRow = position.getRow() - 1;
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            // if the pawn is in the 2nd row, it can move one or two pieces forwards
            if (row == 7) {
                //making sure its only in columns 2-7
                if (column > 1 && column < 8) {
                    ChessPosition pawnFirstPositionMoveOne = new ChessPosition(blackRow, column);
                    ChessPosition pawnFirstPositionMoveTwo = new ChessPosition(blackRow - 1, column);
                    ChessMove pawnFirstMoveOne = new ChessMove(position, pawnFirstPositionMoveOne, null);
                    ChessMove pawnFirstMoveTwo = new ChessMove(position, pawnFirstPositionMoveTwo, null);

                    // if no piece in spot ahead, add moves
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

                    // checking and capturing piece one up and to the left
                    if (opLeft != null) {
                        if (opLeft.getTeamColor() != myColour) {
                            moves.add(captureLeft);
                        }
                    }
                    // checking and capturing piece one up and to the right
                    if (opRight != null) {
                        if (opRight.getTeamColor() != myColour) {
                            moves.add(captureRight);
                        }
                    }
                    // takes care of the edge columns
                } else if (column == 1 || column == 8) {
                    ChessPosition pawnFirstPositionMoveOne = new ChessPosition(blackRow, column);
                    ChessPosition pawnFirstPositionMoveTwo = new ChessPosition(blackRow + 1, column);

                    ChessMove pawnFirstMoveOne = new ChessMove(position, pawnFirstPositionMoveOne, null);
                    ChessMove pawnFirstMoveTwo = new ChessMove(position, pawnFirstPositionMoveTwo, null);
                    // if position ahead clear, offer moves
                    if (board.getPiece(pawnFirstPositionMoveOne) == null) {
                        moves.add(pawnFirstMoveOne);
                        if (board.getPiece(pawnFirstPositionMoveTwo) == null) {
                            moves.add(pawnFirstMoveTwo);
                        }
                    }
                    // if in the far left column
                    if (column == 1) {
                        ChessPosition positionRight = new ChessPosition(blackRow, checkRight);
                        ChessMove captureRight = new ChessMove(position, positionRight, null);
                        ChessPiece opRight = board.getPiece(positionRight);

                        // checking only the piece one up and to the right
                        if (opRight != null) {
                            if (opRight.getTeamColor() != myColour) {
                                moves.add(captureRight);
                            }
                        }
                    } else {
                        ChessPosition positionLeft = new ChessPosition(blackRow, checkLeft);
                        ChessMove captureLeft = new ChessMove(position, positionLeft, null);
                        ChessPiece opLeft = board.getPiece(positionLeft);

                        if (opLeft != null) {
                            if (opLeft.getTeamColor() != myColour) {
                                moves.add(captureLeft);
                            }
                        }
                    }
                }
                return moves;
            }
            // does the move only one space if the row is not 2
            if (row != 7){
                if (row > 2) {
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
                    // handles overtaking a piece between columns 2 and 7 inclusive
                    if (column > 1 && column < 8) {
                        ChessPiece opLeft = board.getPiece(positionLeft);
                        ChessPiece opRight = board.getPiece(positionRight);
                        // up and to the left
                        if (opLeft != null) {
                            if (opLeft.getTeamColor() != myColour) {
                                moves.add(captureLeft);
                            }
                        }
                        // up and to the right
                        if (opRight != null) {
                            if (opRight.getTeamColor() != myColour) {
                                moves.add(captureRight);
                            }
                        }
                    }
                    // if in far left column
                    if (column == 1) {
                        ChessPiece opRight = board.getPiece(positionRight);
                        if (opRight != null) {
                            if (opRight.getTeamColor() != myColour) {
                                moves.add(captureRight);
                            }
                        }
                    }
                    // if in far right column
                    if (column == 8) {
                        ChessPiece opLeft = board.getPiece(positionLeft);
                        if (opLeft != null) {
                            if (opLeft.getTeamColor() != myColour) {
                                moves.add(captureLeft);
                            }
                        }
                    }
                    return moves;
                    // if the row is seven, promotions
                } else {
                    ChessPosition promotionPosition = new ChessPosition(blackRow, column);
                    ChessPiece occupied = board.getPiece(promotionPosition);
                    List<ChessPiece.PieceType> promotionOptions = new ArrayList<>();
                    Collections.addAll(promotionOptions, ChessPiece.PieceType.KNIGHT,
                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
                            ChessPiece.PieceType.ROOK);
                    // promotions making one step forward to end of board
                    if (occupied == null) {
                        for (ChessPiece.PieceType pieceType : promotionOptions) {
                            ChessMove promotionMove = new ChessMove(position, promotionPosition, pieceType);
                            moves.add(promotionMove);
                        }
                    }
                    // promotions capturing a piece
                    if (column > 1 && column < 8) {
                        ChessPosition positionLeft = new ChessPosition(blackRow, checkLeft);
                        ChessPosition positionRight = new ChessPosition(blackRow, checkRight);

                        ChessPiece opLeft = board.getPiece(positionLeft);
                        ChessPiece opRight = board.getPiece(positionRight);
                        // one up and to the right
                        if (opRight != null) {
                            for (ChessPiece.PieceType pieceType : promotionOptions) {
                                ChessMove promotionMove = new ChessMove(position, positionRight, pieceType);
                                moves.add(promotionMove);
                            }
                        }
                        // one up and to the left
                        if (opLeft != null) {
                            for (ChessPiece.PieceType pieceType : promotionOptions) {
                                ChessMove promotionMove = new ChessMove(position, positionLeft, pieceType);
                                moves.add(promotionMove);
                            }
                        }
                        // promotions for pieces in the 1st and 8th column
                    } else {

                        if (column == 1) {
                            ChessPosition positionRight = new ChessPosition(blackRow, checkRight);
                            ChessPiece opRight = board.getPiece(positionRight);
                            if (opRight != null) {
                                if (opRight.getTeamColor() != myColour) {
                                    for (ChessPiece.PieceType pieceType : promotionOptions) {
                                        ChessMove promotionMove = new ChessMove(position, positionRight, pieceType);
                                        moves.add(promotionMove);
                                    }
                                }
                            }
                        }

                        // if in far right column
                        if (column == 8) {
                            ChessPosition positionLeft = new ChessPosition(blackRow, checkLeft);
                            ChessPiece opLeft = board.getPiece(positionLeft);
                            if (opLeft != null) {
                                if (opLeft.getTeamColor() != myColour) {
                                    for (ChessPiece.PieceType pieceType : promotionOptions) {
                                        ChessMove promotionMove = new ChessMove(position, positionLeft, pieceType);
                                        moves.add(promotionMove);
                                    }
                                }
                            }
                        }
                    }
                    return moves;
                }
            }
        }
        return moves;
    }
}

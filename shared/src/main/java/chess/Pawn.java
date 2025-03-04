package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class implements the PieceMovesCalculator interface. It compiles a list of all the possible moves a pawn
 * can make on its turn.
 * <p>
 * Pawns are the most complicated piece of the lot. While they can only move one piece vertically, they have a lot
 * of other things to look out for. When taking another piece, pawns can move to the spot kitty corner to their own.
 * They can also be promoted to a queen, rook, knight, or bishop if the pawn makes it to the other side of the board.
 */
public class Pawn implements PieceMovesCalculator {

    public static List<ChessMove> pawnMove(ChessPosition position, ChessBoard board, ChessGame.TeamColor myTeam) {

        List<ChessMove> pawnMove = new ArrayList<>();

        int startOneRow = position.getRow();
        int startOneCol = position.getColumn();

        if (myTeam == ChessGame.TeamColor.WHITE) {
            startOneRow += 1;
        } else if (myTeam == ChessGame.TeamColor.BLACK) {
            startOneRow -= 1;
        }

        ChessPosition firstPosOne = new ChessPosition(startOneRow, startOneCol);
        ChessMove firstMoveOne = new ChessMove(position, firstPosOne, null);

        if ((board.getPiece(firstPosOne) == null) && (startOneRow != 1 && startOneRow != 8)) {
            pawnMove.add(firstMoveOne);

            if ((position.getRow() == 2 && myTeam == ChessGame.TeamColor.WHITE) || (position.getRow() == 7 && myTeam == ChessGame.TeamColor.BLACK)) {

                ChessMove checkedMoveTwo = pawnMoveTwoSpots(position, board, myTeam);

                if (checkedMoveTwo != null) {
                    pawnMove.add(checkedMoveTwo);
                }
            }
        } else if (startOneRow == 1 || startOneRow == 8) {

            List<ChessMove> promotions = promotionMovesCalculator(position, firstPosOne, board, myTeam);
            if (!promotions.isEmpty()) {
                pawnMove.addAll(promotions);
            }
        }

        if (startOneRow != 1 && startOneRow != 8) {
            ChessMove checkIfCanCapture = pawnCaptureMove(position, board, myTeam);

            if (checkIfCanCapture != null) {
                pawnMove.add(checkIfCanCapture);
            }

        }

        return pawnMove;
    }

    public static ChessMove pawnMoveTwoSpots(ChessPosition position, ChessBoard board, ChessGame.TeamColor myTeam) {

        int startTwoRow = position.getRow();
        int startTwoCol = position.getColumn();

        if (myTeam == ChessGame.TeamColor.WHITE) {
            startTwoRow += 2;
        } else if (myTeam == ChessGame.TeamColor.BLACK) {
            startTwoRow -= 2;
        }

        ChessPosition firstPosTwo = new ChessPosition(startTwoRow, startTwoCol);
        ChessMove firstMoveTwo = new ChessMove(position, firstPosTwo, null);

        if (board.getPiece(firstPosTwo) == null) {
            return firstMoveTwo;
        } else {
            return null;
        }
    }

    public static ChessMove pawnCaptureMove(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor myTeam) {

        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();
        int colRight = startCol + 1;
        int colLeft = startCol - 1;

        if (myTeam == ChessGame.TeamColor.WHITE) {
            startRow += 1;
        } else if (myTeam == ChessGame.TeamColor.BLACK) {
            startRow -= 1;
        }

        if (startCol > 1 && startCol < 8) {
            ChessPosition captureRightPos = new ChessPosition(startRow, colRight);
            ChessPosition captureLeftPos = new ChessPosition(startRow, colLeft);

            ChessMove captureRightMove = new ChessMove(startPosition, captureRightPos, null);
            ChessMove captureLeftMove = new ChessMove(startPosition, captureLeftPos, null);

            ChessPiece opRight = board.getPiece(captureRightPos);
            ChessPiece opLeft = board.getPiece(captureLeftPos);

            if ((opRight != null) && (opRight.getTeamColor() != myTeam)) {
                return captureRightMove;
            } else if ((opLeft != null) && opLeft.getTeamColor() != myTeam) {
                return captureLeftMove;
            } else {
                return null;
            }
        } else if (startCol == 1) {
            ChessPosition captureRightPos = new ChessPosition(startRow, colRight);
            ChessMove captureRightMove = new ChessMove(startPosition, captureRightPos, null);
            ChessPiece opRight = board.getPiece(captureRightPos);

            if ((opRight != null) && (opRight.getTeamColor() != myTeam)) {
                return captureRightMove;
            } else {
                return null;
            }

        } else if (startCol == 8) {
            ChessPosition captureLeftPos = new ChessPosition(startRow, colLeft);
            ChessMove captureLeftMove = new ChessMove(startPosition, captureLeftPos, null);
            ChessPiece opLeft = board.getPiece(captureLeftPos);

            if ((opLeft != null) && opLeft.getTeamColor() != myTeam) {
                return captureLeftMove;
            } else {
                return null;
            }
        }
        return null;
    }

    public static List<ChessMove> promotionMovesCalculator(ChessPosition startPosition, ChessPosition endPosition,
                                                           ChessBoard board, ChessGame.TeamColor myTeam) {
        List<ChessMove> promotions = new ArrayList<>();

        List<ChessPiece.PieceType> promotionOptions = new ArrayList<>(Arrays.asList(ChessPiece.PieceType.KNIGHT,
                                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK));

        if (board.getPiece(endPosition) == null) {
            for (ChessPiece.PieceType option : promotionOptions) {
                ChessMove promotionNoCaptureMove = new ChessMove(startPosition, endPosition, option);
                promotions.add(promotionNoCaptureMove);
            }
        }


        return promotions;
    }

    public static List<ChessMove> promotionMoveOnce(ChessPosition startPosition, ChessBoard board, ChessPiece promotionPiece) {
        return null;
    }

    /**
     * Here is where the moves are calculated.
     * <p>
     * It is important to know what team the piece being checked is on.
     * <p>
     * Pawns can only move forwards. White pawns can move up in their columns (0,+),
     * and black pawns can move down in their columns (0,-). The best way to keep track of that is to implement two separate
     * but mostly the same code after checking if the piece is black or white.
     * <p>
     * If it is a pawns first move, they are allowed to move one or two places. The easiest way to keep track of that is
     * to keep track of what row the pawn is in. The pawns will start in row 2 on the white team, and row 7 on the black team.
     * If the pawns are still in this row, it means it is their first move.
     * <p>
     * A pawn cannot move around a piece if it is blocked. If the spot directly in front of it is blocked, that pawn cannot move
     * unless there is a piece to capture on the right or left of the piece right in front of them.
     * <p>
     * Be careful about the pawns in column 1 and 8. Checking out of bounds will result in an error, so the pawn in
     * column 1 cannot check the left, and the pawn in column 8 cannot check the right.
     * <p>
     * Remember that the list of moves will only let you add a ChessMove type to it, and that a ChessMove
     * needs a ChessPosition to be initialised.
     * <p>
     * Be sure to check every possible move a pawn can make.
     * @param position
     * @param board
     * @return a list of all the possible moves a pawn can make
     */
    public static List<ChessMove> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessMove> moves = new ArrayList<>();

        ChessPiece piece = board.getPiece(position);

        // checks the spot is empty
        if (piece == null) {
            return moves;
        }

        ChessGame.TeamColor myColour = board.getPiece(position).getTeamColor();

        moves.addAll(pawnMove(position, board, myColour));

//        int row = position.getRow();
//        int column = position.getColumn();



//        if ((row > 0 && row <= 8) && myColour == ChessGame.TeamColor.WHITE) {
//            moves.addAll(pawnMove(position, board, 1, myColour));
//        } else if ((row > 0 && row <= 8)  && myColour == ChessGame.TeamColor.BLACK) {
//            moves.addAll(pawnMove(position, board, -1, myColour));
//        }

// ---------------------------------------------------------------------------------------------------------------------

//        int checkLeft = column - 1;
//        int checkRight = column + 1;
//
//        // white pawns move up the board
//        int whiteRow = position.getRow() + 1;
//        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
//            // if the pawn is in the 2nd row, it can move one or two pieces forwards
////            if (row == 2) {
////                //making sure its only in columns 2-7
////                if (column > 1 && column < 8) {
////                    ChessPosition pawnFirstPositionMoveOne = new ChessPosition(whiteRow, column);
////                    ChessPosition pawnFirstPositionMoveTwo = new ChessPosition(whiteRow + 1, column);
////                    ChessMove pawnFirstMoveOne = new ChessMove(position, pawnFirstPositionMoveOne, null);
////                    ChessMove pawnFirstMoveTwo = new ChessMove(position, pawnFirstPositionMoveTwo, null);
////
////                    // if no piece in spot ahead, add moves
////                    if (board.getPiece(pawnFirstPositionMoveOne) == null) {
////                        moves.add(pawnFirstMoveOne);
////                        if (board.getPiece(pawnFirstPositionMoveTwo) == null) {
////                            moves.add(pawnFirstMoveTwo);
////                        }
////                    }
////
////                    ChessPosition positionLeft = new ChessPosition(whiteRow, checkLeft);
////                    ChessPosition positionRight = new ChessPosition(whiteRow, checkRight);
////
////                    ChessMove captureLeft = new ChessMove(position, positionLeft, null);
////                    ChessMove captureRight = new ChessMove(position, positionRight, null);
////
////                    ChessPiece opLeft = board.getPiece(positionLeft);
////                    ChessPiece opRight = board.getPiece(positionRight);
////
////                    // checking and capturing piece one up and to the left
////                    if (opLeft != null) {
////                        if (opLeft.getTeamColor() != myColour) {
////                            moves.add(captureLeft);
////                        }
////                    }
////                    // checking and capturing piece one up and to the right
////                    if (opRight != null) {
////                        if (opRight.getTeamColor() != myColour) {
////                            moves.add(captureRight);
////                        }
////                    }
////                    // takes care of the edge columns
////                } else if (column == 1 || column == 8) {
////                    ChessPosition pawnFirstPositionMoveOne = new ChessPosition(whiteRow, column);
////                    ChessPosition pawnFirstPositionMoveTwo = new ChessPosition(whiteRow + 1, column);
////
////                    ChessMove pawnFirstMoveOne = new ChessMove(position, pawnFirstPositionMoveOne, null);
////                    ChessMove pawnFirstMoveTwo = new ChessMove(position, pawnFirstPositionMoveTwo, null);
////                    // if position ahead clear, offer moves
////                    if (board.getPiece(pawnFirstPositionMoveOne) == null) {
////                        moves.add(pawnFirstMoveOne);
////                        if (board.getPiece(pawnFirstPositionMoveTwo) == null) {
////                            moves.add(pawnFirstMoveTwo);
////                        }
////                    }
////                    // if in the far left column
////                    if (column == 1) {
////                        ChessPosition positionRight = new ChessPosition(whiteRow, checkRight);
////                        ChessMove captureRight = new ChessMove(position, positionRight, null);
////                        ChessPiece opRight = board.getPiece(positionRight);
////
////                        // checking only the piece one up and to the right
////                        if (opRight != null) {
////                            if (opRight.getTeamColor() != myColour) {
////                                moves.add(captureRight);
////                            }
////                        }
////                    } else {
////                        ChessPosition positionLeft = new ChessPosition(whiteRow, checkLeft);
////                        ChessMove captureLeft = new ChessMove(position, positionLeft, null);
////                        ChessPiece opLeft = board.getPiece(positionLeft);
////
////                        if (opLeft != null) {
////                            if (opLeft.getTeamColor() != myColour) {
////                                moves.add(captureLeft);
////                            }
////                        }
////                    }
////                }
////                return moves;
////            }
//            // does the move only one space if the row is not 2
//            if (row != 2) {
//                if (row < 7) {
//                    ChessPosition positionOne = new ChessPosition(whiteRow, column);
//                    ChessPiece occupiedPiece = board.getPiece(positionOne);
//                    ChessMove moveOne = new ChessMove(position, positionOne, null);
//
//                    ChessPosition positionLeft = new ChessPosition(whiteRow, checkLeft);
//                    ChessPosition positionRight = new ChessPosition(whiteRow, checkRight);
//
//                    ChessMove captureLeft = new ChessMove(position, positionLeft, null);
//                    ChessMove captureRight = new ChessMove(position, positionRight, null);
//
//                    if (occupiedPiece == null) {
//                        moves.add(moveOne);
//                    }
//                    // handles overtaking a piece between columns 2 and 7 inclusive
//                    if (column > 1 && column < 8) {
//                        ChessPiece opLeft = board.getPiece(positionLeft);
//                        ChessPiece opRight = board.getPiece(positionRight);
//                        // up and to the left
//                        if (opLeft != null) {
//                            if (opLeft.getTeamColor() != myColour) {
//                                moves.add(captureLeft);
//                            }
//                        }
//                        // up and to the right
//                        if (opRight != null) {
//                            if (opRight.getTeamColor() != myColour) {
//                                moves.add(captureRight);
//                            }
//                        }
//                    }
//                    // if in far left column
//                    if (column == 1) {
//                        ChessPiece opRight = board.getPiece(positionRight);
//                        if (opRight != null) {
//                            if (opRight.getTeamColor() != myColour) {
//                                moves.add(captureRight);
//                            }
//                        }
//                    }
//                    // if in far right column
//                    if (column == 8) {
//                        ChessPiece opLeft = board.getPiece(positionLeft);
//                        if (opLeft != null) {
//                            if (opLeft.getTeamColor() != myColour) {
//                                moves.add(captureLeft);
//                            }
//                        }
//                    }
//                    // if the row is seven, promotions
//                    return moves;
//                } else {
//                    ChessPosition promotionPosition = new ChessPosition(whiteRow, column);
//                    ChessPiece occupied = board.getPiece(promotionPosition);
//                    List<ChessPiece.PieceType> promotionOptions = new ArrayList<>();
//                    Collections.addAll(promotionOptions, ChessPiece.PieceType.KNIGHT,
//                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
//                            ChessPiece.PieceType.ROOK);
//                    // promotions making one step forward to end of board
//                    if (occupied == null) {
//                        for (ChessPiece.PieceType pieceType : promotionOptions) {
//                            ChessMove promotionMove = new ChessMove(position, promotionPosition, pieceType);
//                            moves.add(promotionMove);
//                        }
//                    }
//                    // promotions capturing a piece
//                    if (column > 1 && column < 8) {
//                        ChessPosition positionLeft = new ChessPosition(whiteRow, checkLeft);
//                        ChessPosition positionRight = new ChessPosition(whiteRow, checkRight);
//
//                        ChessPiece opLeft = board.getPiece(positionLeft);
//                        ChessPiece opRight = board.getPiece(positionRight);
//                        // one up and to the right
//                        if (opRight != null) {
//                            for (ChessPiece.PieceType pieceType : promotionOptions) {
//                                ChessMove promotionMove = new ChessMove(position, positionRight, pieceType);
//                                moves.add(promotionMove);
//                            }
//                        }
//                        // one up and to the left
//                        if (opLeft != null) {
//                            for (ChessPiece.PieceType pieceType : promotionOptions) {
//                                ChessMove promotionMove = new ChessMove(position, positionLeft, pieceType);
//                                moves.add(promotionMove);
//                            }
//                        }
//                        // promotions for pieces in the 1st and 8th column
//                    } else {
//
//                        if (column == 1) {
//                            ChessPosition positionRight = new ChessPosition(whiteRow, checkRight);
//                            ChessPiece opRight = board.getPiece(positionRight);
//                            if (opRight != null) {
//                                if (opRight.getTeamColor() != myColour) {
//                                    for (ChessPiece.PieceType pieceType : promotionOptions) {
//                                        ChessMove promotionMove = new ChessMove(position, positionRight, pieceType);
//                                        moves.add(promotionMove);
//                                    }
//                                }
//                            }
//                        }
//
//                        // if in far right column
//                        if (column == 8) {
//                            ChessPosition positionLeft = new ChessPosition(whiteRow, checkLeft);
//                            ChessPiece opLeft = board.getPiece(positionLeft);
//                            if (opLeft != null) {
//                                if (opLeft.getTeamColor() != myColour) {
//                                    for (ChessPiece.PieceType pieceType : promotionOptions) {
//                                        ChessMove promotionMove = new ChessMove(position, positionLeft, pieceType);
//                                        moves.add(promotionMove);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    return moves;
//                }
//            }
//        }
//
//        int blackRow = position.getRow() - 1;
//        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
//            // if the pawn is in the 2nd row, it can move one or two pieces forwards
////            if (row == 7) {
////                //making sure its only in columns 2-7
////                if (column > 1 && column < 8) {
////                    ChessPosition pawnFirstPositionMoveOne = new ChessPosition(blackRow, column);
////                    ChessPosition pawnFirstPositionMoveTwo = new ChessPosition(blackRow - 1, column);
////                    ChessMove pawnFirstMoveOne = new ChessMove(position, pawnFirstPositionMoveOne, null);
////                    ChessMove pawnFirstMoveTwo = new ChessMove(position, pawnFirstPositionMoveTwo, null);
////
////                    // if no piece in spot ahead, add moves
////                    if (board.getPiece(pawnFirstPositionMoveOne) == null) {
////                        moves.add(pawnFirstMoveOne);
////                        if (board.getPiece(pawnFirstPositionMoveTwo) == null) {
////                            moves.add(pawnFirstMoveTwo);
////                        }
////                    }
////
////                    ChessPosition positionLeft = new ChessPosition(blackRow, checkLeft);
////                    ChessPosition positionRight = new ChessPosition(blackRow, checkRight);
////
////                    ChessMove captureLeft = new ChessMove(position, positionLeft, null);
////                    ChessMove captureRight = new ChessMove(position, positionRight, null);
////
////                    ChessPiece opLeft = board.getPiece(positionLeft);
////                    ChessPiece opRight = board.getPiece(positionRight);
////
////                    // checking and capturing piece one up and to the left
////                    if (opLeft != null) {
////                        if (opLeft.getTeamColor() != myColour) {
////                            moves.add(captureLeft);
////                        }
////                    }
////                    // checking and capturing piece one up and to the right
////                    if (opRight != null) {
////                        if (opRight.getTeamColor() != myColour) {
////                            moves.add(captureRight);
////                        }
////                    }
////                    // takes care of the edge columns
////                } else if (column == 1 || column == 8) {
////                    ChessPosition pawnFirstPositionMoveOne = new ChessPosition(blackRow, column);
////                    ChessPosition pawnFirstPositionMoveTwo = new ChessPosition(blackRow + 1, column);
////
////                    ChessMove pawnFirstMoveOne = new ChessMove(position, pawnFirstPositionMoveOne, null);
////                    ChessMove pawnFirstMoveTwo = new ChessMove(position, pawnFirstPositionMoveTwo, null);
////                    // if position ahead clear, offer moves
////                    if (board.getPiece(pawnFirstPositionMoveOne) == null) {
////                        moves.add(pawnFirstMoveOne);
////                        if (board.getPiece(pawnFirstPositionMoveTwo) == null) {
////                            moves.add(pawnFirstMoveTwo);
////                        }
////                    }
////                    // if in the far left column
////                    if (column == 1) {
////                        ChessPosition positionRight = new ChessPosition(blackRow, checkRight);
////                        ChessMove captureRight = new ChessMove(position, positionRight, null);
////                        ChessPiece opRight = board.getPiece(positionRight);
////
////                        // checking only the piece one up and to the right
////                        if (opRight != null) {
////                            if (opRight.getTeamColor() != myColour) {
////                                moves.add(captureRight);
////                            }
////                        }
////                    } else {
////                        ChessPosition positionLeft = new ChessPosition(blackRow, checkLeft);
////                        ChessMove captureLeft = new ChessMove(position, positionLeft, null);
////                        ChessPiece opLeft = board.getPiece(positionLeft);
////
////                        if (opLeft != null) {
////                            if (opLeft.getTeamColor() != myColour) {
////                                moves.add(captureLeft);
////                            }
////                        }
////                    }
////                }
////                return moves;
////            }
//            // does the move only one space if the row is not 2
//            if (row != 7) {
//                if (row > 2) {
//                    ChessPosition positionOne = new ChessPosition(blackRow, column);
//                    ChessPiece occupiedPiece = board.getPiece(positionOne);
//                    ChessMove moveOne = new ChessMove(position, positionOne, null);
//
//                    ChessPosition positionLeft = new ChessPosition(blackRow, checkLeft);
//                    ChessPosition positionRight = new ChessPosition(blackRow, checkRight);
//
//                    ChessMove captureLeft = new ChessMove(position, positionLeft, null);
//                    ChessMove captureRight = new ChessMove(position, positionRight, null);
//
//                    if (occupiedPiece == null) {
//                        moves.add(moveOne);
//                    }
//                    // handles overtaking a piece between columns 2 and 7 inclusive
//                    if (column > 1 && column < 8) {
//                        ChessPiece opLeft = board.getPiece(positionLeft);
//                        ChessPiece opRight = board.getPiece(positionRight);
//                        // up and to the left
//                        if (opLeft != null) {
//                            if (opLeft.getTeamColor() != myColour) {
//                                moves.add(captureLeft);
//                            }
//                        }
//                        // up and to the right
//                        if (opRight != null) {
//                            if (opRight.getTeamColor() != myColour) {
//                                moves.add(captureRight);
//                            }
//                        }
//                    }
//                    // if in far left column
//                    if (column == 1) {
//                        ChessPiece opRight = board.getPiece(positionRight);
//                        if (opRight != null) {
//                            if (opRight.getTeamColor() != myColour) {
//                                moves.add(captureRight);
//                            }
//                        }
//                    }
//                    // if in far right column
//                    if (column == 8) {
//                        ChessPiece opLeft = board.getPiece(positionLeft);
//                        if (opLeft != null) {
//                            if (opLeft.getTeamColor() != myColour) {
//                                moves.add(captureLeft);
//                            }
//                        }
//                    }
//                    return moves;
//                    // if the row is seven, promotions
//                } else {
//                    ChessPosition promotionPosition = new ChessPosition(blackRow, column);
//                    ChessPiece occupied = board.getPiece(promotionPosition);
//                    List<ChessPiece.PieceType> promotionOptions = new ArrayList<>();
//                    Collections.addAll(promotionOptions, ChessPiece.PieceType.KNIGHT,
//                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
//                            ChessPiece.PieceType.ROOK);
//                    // promotions making one step forward to end of board
//                    if (occupied == null) {
//                        for (ChessPiece.PieceType pieceType : promotionOptions) {
//                            ChessMove promotionMove = new ChessMove(position, promotionPosition, pieceType);
//                            moves.add(promotionMove);
//                        }
//                    }
//                    // promotions capturing a piece
//                    if (column > 1 && column < 8) {
//                        ChessPosition positionLeft = new ChessPosition(blackRow, checkLeft);
//                        ChessPosition positionRight = new ChessPosition(blackRow, checkRight);
//
//                        ChessPiece opLeft = board.getPiece(positionLeft);
//                        ChessPiece opRight = board.getPiece(positionRight);
//                        // one up and to the right
//                        if (opRight != null) {
//                            for (ChessPiece.PieceType pieceType : promotionOptions) {
//                                ChessMove promotionMove = new ChessMove(position, positionRight, pieceType);
//                                moves.add(promotionMove);
//                            }
//                        }
//                        // one up and to the left
//                        if (opLeft != null) {
//                            for (ChessPiece.PieceType pieceType : promotionOptions) {
//                                ChessMove promotionMove = new ChessMove(position, positionLeft, pieceType);
//                                moves.add(promotionMove);
//                            }
//                        }
//                        // promotions for pieces in the 1st and 8th column
//                    } else {
//
//                        if (column == 1) {
//                            ChessPosition positionRight = new ChessPosition(blackRow, checkRight);
//                            ChessPiece opRight = board.getPiece(positionRight);
//                            if (opRight != null) {
//                                if (opRight.getTeamColor() != myColour) {
//                                    for (ChessPiece.PieceType pieceType : promotionOptions) {
//                                        ChessMove promotionMove = new ChessMove(position, positionRight, pieceType);
//                                        moves.add(promotionMove);
//                                    }
//                                }
//                            }
//                        }
//
//                        // if in far right column
//                        if (column == 8) {
//                            ChessPosition positionLeft = new ChessPosition(blackRow, checkLeft);
//                            ChessPiece opLeft = board.getPiece(positionLeft);
//                            if (opLeft != null) {
//                                if (opLeft.getTeamColor() != myColour) {
//                                    for (ChessPiece.PieceType pieceType : promotionOptions) {
//                                        ChessMove promotionMove = new ChessMove(position, positionLeft, pieceType);
//                                        moves.add(promotionMove);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    return moves;
//                }
//            }
//        }
        return moves;
    }
}

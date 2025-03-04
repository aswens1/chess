package chess;

import java.util.ArrayList;
import java.util.Arrays;
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

            List<ChessMove> promotions = promotionMovesCalculator(position, firstPosOne, board);
            if (!promotions.isEmpty()) {
                pawnMove.addAll(promotions);
            }
        }

        if (startOneRow != 1 && startOneRow != 8) {
            ChessMove checkIfCanCapture = pawnCaptureMove(position, board, myTeam);

            if (checkIfCanCapture != null) {
                pawnMove.add(checkIfCanCapture);
            }
        } else {
            List<ChessMove> promotionAndCaptureMove = promotionAndCapture(position, board, myTeam);

            if (!promotionAndCaptureMove.isEmpty()) {
                pawnMove.addAll(promotionAndCaptureMove);
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
                                                           ChessBoard board) {
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

    public static List<ChessMove> promotionAndCapture(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor myTeam) {
        List<ChessMove> promotions = new ArrayList<>();



        List<ChessPiece.PieceType> promotionOptions = new ArrayList<>(Arrays.asList(ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK));

        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();
        int colRight = startCol + 1;
        int colLeft = startCol - 1;

        if (myTeam == ChessGame.TeamColor.WHITE) {
            startRow += 1;
        } else if (myTeam == ChessGame.TeamColor.BLACK) {
            startRow -= 1;
        }

        ChessPosition captureRightPos = new ChessPosition(startRow, colRight);
        ChessPosition captureLeftPos = new ChessPosition(startRow, colLeft);

        if (startCol != 1 && startCol != 8) {
            if (board.getPiece(captureRightPos) != null && board.getPiece(captureRightPos).getTeamColor() != myTeam) {
                for (ChessPiece.PieceType option : promotionOptions) {
                    ChessMove promotionCaptureMove = new ChessMove(startPosition, captureRightPos, option);
                    promotions.add(promotionCaptureMove);
                }
            } else if (board.getPiece(captureLeftPos) != null && board.getPiece(captureLeftPos).getTeamColor() != myTeam) {
                for (ChessPiece.PieceType option : promotionOptions) {
                    ChessMove promotionCaptureMove = new ChessMove(startPosition, captureLeftPos, option);
                    promotions.add(promotionCaptureMove);
                }
            }
        } else {
            if ((startCol == 1) && ((board.getPiece(captureRightPos) != null) && (board.getPiece(captureRightPos).getTeamColor() != myTeam))) {
                if (board.getPiece(captureRightPos) != null && board.getPiece(captureRightPos).getTeamColor() != myTeam) {
                    for (ChessPiece.PieceType option : promotionOptions) {
                        ChessMove promotionCaptureMove = new ChessMove(startPosition, captureRightPos, option);
                        promotions.add(promotionCaptureMove);
                    }
                }
            } else {
                for (ChessPiece.PieceType option : promotionOptions) {
                    ChessMove promotionCaptureMove = new ChessMove(startPosition, captureLeftPos, option);
                    promotions.add(promotionCaptureMove);
                }
            }
        }

        return promotions;
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
     * @param position takes in a chess position
     * @param board takes in the chess board
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

        return moves;
    }
}

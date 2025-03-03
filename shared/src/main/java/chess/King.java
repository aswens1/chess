package chess;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the PieceMovesCalculator interface. It compiles a list of all the possible moves a king
 * can make on its turn.
 * <p>
 * Kings can move in any direction, but only one place per turn. They can take a piece by moving onto its square.
 */
public class King implements PieceMovesCalculator{

    public static List<ChessMove> createKingMovesList(ChessPosition startingPosition, ChessBoard board, ChessGame.TeamColor myTeam) {
        List<ChessMove> kingMoves = new ArrayList<>();

        int row = startingPosition.getRow();
        int col = startingPosition.getColumn();


        // up right
        kingMoves.addAll(kingMovesCalculator(startingPosition, board, row + 1, col + 1, myTeam));
        // up left
        kingMoves.addAll(kingMovesCalculator(startingPosition, board, row + 1, col - 1, myTeam));
        // down right
        kingMoves.addAll(kingMovesCalculator(startingPosition, board, row - 1, col + 1, myTeam));
        // down left
        kingMoves.addAll(kingMovesCalculator(startingPosition, board, row - 1, col - 1, myTeam));

        // up
        kingMoves.addAll(kingMovesCalculator(startingPosition, board, row, col + 1, myTeam));
        // down
        kingMoves.addAll(kingMovesCalculator(startingPosition, board, row, col - 1, myTeam));
        // left
        kingMoves.addAll(kingMovesCalculator(startingPosition, board, row - 1, col, myTeam));
        // right
        kingMoves.addAll(kingMovesCalculator(startingPosition, board, row + 1, col, myTeam));

        return kingMoves;
    }

    public static List<ChessMove> kingMovesCalculator(ChessPosition startPosition, ChessBoard board, int rowDirection,
                                                      int colDirection, ChessGame.TeamColor myTeam) {
        List<ChessMove> kingMoves = new ArrayList<>();

        if ((rowDirection > 0 && rowDirection <= 8) && (colDirection > 0 && colDirection <= 8)) {
            ChessPosition newPosition = new ChessPosition(rowDirection, colDirection);
            ChessPiece occupiedPiece = board.getPiece(newPosition);

            ChessMove newMove = new ChessMove(startPosition, new ChessPosition(rowDirection, colDirection), null);

            if (occupiedPiece == null) {
                kingMoves.add(newMove);
            } else {
                if (occupiedPiece.getTeamColor() != myTeam) {
                    kingMoves.add(newMove);
                }
            }
        }
        return kingMoves;
    }

    /**
     * Here is where the moves are calculated.
     * <p>
     * It is important to know what team the piece being checked is on.
     * <p>
     * Kings can move in any direction. Directly up (0,+), up and to the right (+,+), directly right (+,0),
     * down and to the right (+,-), directly down (0,-), down and to the left (-,-), directly left (-,0),
     * and up and to the left (-,+). It helps to think about it as a coordinate plane.
     * <p>
     * Before any move is made, you want to see if that spot is empty or not. If it is, the king can move to that spot.
     * If it is not empty, that means that the king either cannot go there (if it's his team) or can take the piece's spot.
     * <p>
     * Remember that the list of moves will only let you add a ChessMove type to it, and that a ChessMove
     * needs a ChessPosition to be initialised.
     * <p>
     * Make sure to check every move the king can make.
     * @param position
     * @param board
     * @return a list of all the possible moves a king can make
     */
    public static List<ChessMove> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessMove> moves = new ArrayList<>();

        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor myColour = board.getPiece(position).getTeamColor();

        // checks if the spot is empty
        if (piece == null) {
            return moves;
        }

        moves.addAll(createKingMovesList(position, board, myColour));

//        int row = position.getRow();
//        int column = position.getColumn();
//        int upOne = position.getRow() + 1;
//        int downOne = position.getRow() - 1;
//        int leftOne = position.getColumn() - 1;
//        int rightOne = position.getColumn() + 1;
//
//        // check moves 1 up, 1 up and to the right, and 1 up and to the left
//        if (upOne <= 8) {
//            ChessPosition upPosition = new ChessPosition(upOne, column);
//            ChessPiece occupiedPiece = board.getPiece(upPosition);
//            ChessMove upOneMove = new ChessMove(position, upPosition, null);
//            if (occupiedPiece == null) {
//                moves.add(upOneMove);
//            } else if (occupiedPiece.getTeamColor() != myColour) {
//                moves.add(upOneMove);
//            }
//
//            if (rightOne <= 8) {
//                ChessPosition upRightPosition = new ChessPosition(upOne, rightOne);
//                ChessPiece occupiedPieceRight = board.getPiece(upRightPosition);
//                ChessMove rightUpMove = new ChessMove(position, upRightPosition, null);
//                if (occupiedPieceRight == null) {
//                    moves.add(rightUpMove);
//                } else if (occupiedPieceRight.getTeamColor() != myColour) {
//                    moves.add(rightUpMove);
//                }
//            }
//
//            if (leftOne >= 1) {
//                ChessPosition upLeftPosition = new ChessPosition(upOne, leftOne);
//                ChessPiece occupiedPieceLeft = board.getPiece(upLeftPosition);
//                ChessMove leftUpMove = new ChessMove(position, upLeftPosition, null);
//                if (occupiedPieceLeft == null) {
//                    moves.add(leftUpMove);
//                } else if (occupiedPieceLeft.getTeamColor() != myColour) {
//                    moves.add(leftUpMove);
//                }
//            }
//        }
//
//        // check moves 1 down, 1 down and to the right, 1 down and to the left
//        if (downOne >= 1) {
//            ChessPosition downPosition = new ChessPosition(downOne, column);
//            ChessPiece occupiedPiece = board.getPiece(downPosition);
//            ChessMove downOneMove = new ChessMove(position, downPosition, null);
//            if (occupiedPiece == null) {
//                moves.add(downOneMove);
//            } else if (occupiedPiece.getTeamColor() != myColour) {
//                moves.add(downOneMove);
//            }
//
//            if (rightOne <= 8) {
//                ChessPosition downRightPosition = new ChessPosition(downOne, rightOne);
//                ChessPiece occupiedPieceRight = board.getPiece(downRightPosition);
//                ChessMove rightDownMove = new ChessMove(position, downRightPosition, null);
//                if (occupiedPieceRight == null) {
//                    moves.add(rightDownMove);
//                } else if (occupiedPieceRight.getTeamColor() != myColour) {
//                    moves.add(rightDownMove);
//                }
//            }
//
//            if (leftOne >= 1) {
//                ChessPosition downLeftPosition = new ChessPosition(downOne, leftOne);
//                ChessPiece occupiedPieceLeft = board.getPiece(downLeftPosition);
//                ChessMove leftDownMove = new ChessMove(position, downLeftPosition, null);
//                if (occupiedPieceLeft == null) {
//                    moves.add(leftDownMove);
//                } else if (occupiedPieceLeft.getTeamColor() != myColour) {
//                    moves.add(leftDownMove);
//                }
//            }
//        }
//
//        // check move 1 to the right
//        if (rightOne <= 8) {
//            ChessPosition rightPosition = new ChessPosition(row, rightOne);
//            ChessPiece occupiedPieceRight = board.getPiece(rightPosition);
//            ChessMove rightMove = new ChessMove(position, rightPosition, null);
//            if (occupiedPieceRight == null) {
//                moves.add(rightMove);
//            } else if (occupiedPieceRight.getTeamColor() != myColour) {
//                moves.add(rightMove);
//            }
//        }
//
//        // check move 1 to the left
//        if (leftOne >= 1) {
//            ChessPosition leftPosition = new ChessPosition(row, leftOne);
//            ChessPiece occupiedPieceLeft = board.getPiece(leftPosition);
//            ChessMove leftMove = new ChessMove(position, leftPosition, null);
//            if (occupiedPieceLeft == null) {
//                moves.add(leftMove);
//            } else if (occupiedPieceLeft.getTeamColor() != myColour) {
//                moves.add(leftMove);
//            }
//        }
        return moves;
    }
}

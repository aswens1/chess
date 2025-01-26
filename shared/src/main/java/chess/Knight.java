package chess;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the PieceMovesCalculator interface. It compiles a list of all the moves a knight can make on its turn.
 * <p>
 * Knight can move in any direction, but only in L shapes. They can take a piece by moving into its square. Knights
 * are the only piece that can jump over other pieces that are in its way.
 */
public class Knight implements PieceMovesCalculator {

    /**
     * Here is where the moves are calculated.
     * <p>
     * It is important to know what team the piece being checked is on.
     * <p>
     * Knights can move in any direction, and they'll have two moves per direction. Two up and one right (+,+),
     * two up and one left (-,+), two right and one up (+,+), two right and one down (+,-), two down and one right (+,-),
     * two down and one left (-,-), two left and one up (-,+), two left and one down (-,-). If the knight is in the
     * middle of an empty board, all the moves possible moves it can make will form a circle around it.
     * <p>
     * Before any move is made, you want to see if that spot is empty or not. If it is, the knight can move to that spot.
     * If it is not empty, that means that the knight either cannot go there (if it's his team) or can take the piece's spot.
     * It doesn't matter if there are pieces in between the knight and the spot. The knight is the only piece
     * that can jump over other pieces.
     * <p>
     * Remember that the list of moves will only let you add a ChessMove type to it, and that a ChessMove
     * needs a ChessPosition to be initialised.
     * <p>
     * Make sure to check every move the knight can make.
     * @param position
     * @param board
     * @return a list of all the possible moves a knight can make
     */
    public static List<ChessMove> possibleMoves(ChessPosition position, ChessBoard board) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor myColour = board.getPiece(position).getTeamColor();

        // checks if the spot is empty
        if (piece == null) {
            return moves;
        }

        int upTwo = position.getRow() + 2;
        int downTwo = position.getRow() - 2;
        int rightTwo = position.getColumn() + 2;
        int leftTwo = position.getColumn() - 2;

        int upOne = position.getRow() + 1;
        int downOne = position.getRow() - 1;
        int rightOne = position.getColumn() + 1;
        int leftOne = position.getColumn() - 1;

        // checks up 2: right 1 and left one
        if (upTwo <= 8) {
            // right 1
            if (rightOne <= 8) {
                ChessPosition upRightPosition = new ChessPosition(upTwo, rightOne);
                ChessPiece occupiedPiece = board.getPiece(upRightPosition);
                ChessMove upRightMove = new ChessMove(position, upRightPosition, null);
                if (occupiedPiece == null) {
                    moves.add(upRightMove);
                } else if (occupiedPiece.getTeamColor() != myColour) {
                    moves.add(upRightMove);
                }
            }

            // left 1
            if (leftOne >= 1) {
                ChessPosition upLeftPosition = new ChessPosition(upTwo, leftOne);
                ChessPiece occupiedPiece = board.getPiece(upLeftPosition);
                ChessMove upLeftMove = new ChessMove(position, upLeftPosition, null);
                if (occupiedPiece == null) {
                    moves.add(upLeftMove);
                } else if(occupiedPiece.getTeamColor() != myColour) {
                    moves.add(upLeftMove);
                }
            }
        }

        // checks right 2 up 1 and down 1
        if (rightTwo <= 8) {
            // up 1
            if (upOne <= 8) {
                ChessPosition rightUpPosition = new ChessPosition(upOne, rightTwo);
                ChessPiece occupiedPiece = board.getPiece(rightUpPosition);
                ChessMove rightUpMove = new ChessMove(position, rightUpPosition, null);
                if (occupiedPiece == null) {
                    moves.add(rightUpMove);
                } else if (occupiedPiece.getTeamColor() != myColour) {
                    moves.add(rightUpMove);
                }
            }

            // down 1
            if (downOne >= 1) {
                ChessPosition rightDownPosition = new ChessPosition(downOne, rightTwo);
                ChessPiece occupiedPiece = board.getPiece(rightDownPosition);
                ChessMove rightDownMove = new ChessMove(position, rightDownPosition, null);
                if (occupiedPiece == null) {
                    moves.add(rightDownMove);
                } else if (occupiedPiece.getTeamColor() != myColour) {
                    moves.add(rightDownMove);
                }
            }
        }

        // checks down 2 right 1 and left 1
        if (downTwo >= 1) {
            // right 1
            if (rightOne <= 8) {
                ChessPosition downRightPosition = new ChessPosition(downTwo, rightOne);
                ChessPiece occupiedPiece = board.getPiece(downRightPosition);
                ChessMove downRightMove = new ChessMove(position, downRightPosition, null);
                if (occupiedPiece == null) {
                    moves.add(downRightMove);
                } else if (occupiedPiece.getTeamColor() != myColour) {
                    moves.add(downRightMove);
                }
            }

            // left 1
            if (leftOne >= 1) {
                ChessPosition downLeftPosition = new ChessPosition(downTwo, leftOne);
                ChessPiece occupiedPiece = board.getPiece(downLeftPosition);
                ChessMove downLeftMove = new ChessMove(position, downLeftPosition, null);
                if (occupiedPiece == null) {
                    moves.add(downLeftMove);
                } else if (occupiedPiece.getTeamColor() != myColour) {
                    moves.add(downLeftMove);
                }
            }
        }

        // checks left 2 up 1 and down 1
        if (leftTwo >= 1) {
            // up 1
            if (upOne <= 8) {
                ChessPosition leftUpPosition = new ChessPosition(upOne, leftTwo);
                ChessPiece occupiedPiece = board.getPiece(leftUpPosition);
                ChessMove leftUpMove = new ChessMove(position, leftUpPosition, null);
                if (occupiedPiece == null) {
                    moves.add(leftUpMove);
                } else if (occupiedPiece.getTeamColor() != myColour) {
                    moves.add(leftUpMove);
                }
            }

            // down 1
            if (downOne >= 1) {
                ChessPosition leftDownPosition = new ChessPosition(downOne, leftTwo);
                ChessPiece occupiedPiece = board.getPiece(leftDownPosition);
                ChessMove leftDownMove = new ChessMove(position, leftDownPosition, null);
                if (occupiedPiece == null) {
                    moves.add(leftDownMove);
                } else if (occupiedPiece.getTeamColor() != myColour) {
                    moves.add(leftDownMove);
                }
            }
        }


        System.out.println("Moves: " + moves);
        return moves;
    }
}

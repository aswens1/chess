package chess;

import java.util.ArrayList;
import java.util.List;

public class Knight implements PieceMovesCalculator {
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
            if (rightOne <= 8) {
                ChessPosition upRightPosition = new ChessPosition(upTwo, rightOne);
                ChessMove upRightMove = new ChessMove(position, upRightPosition, null);
                moves.add(upRightMove);
            }

            if (leftOne >= 1) {
                ChessPosition upLeftPosition = new ChessPosition(upTwo, leftOne);
                ChessMove upLeftMove = new ChessMove(position, upLeftPosition, null);
                moves.add(upLeftMove);
            }
        }

        // checks right 2 up 1 and down 1
        if (rightTwo <= 8) {
            if (upOne <= 8) {
                ChessPosition rightUpPosition = new ChessPosition(upOne, rightTwo);
                ChessMove rightUpMove = new ChessMove(position, rightUpPosition, null);
                moves.add(rightUpMove);
            }

            if (downOne >= 1) {
                ChessPosition rightDownPosition = new ChessPosition(downOne, rightTwo);
                ChessMove rightDownMove = new ChessMove(position, rightDownPosition, null);
                moves.add(rightDownMove);
            }
        }

        // checks down 2 right 1 and left 1
        if (downTwo >= 1) {
            if (rightOne <= 8) {
                ChessPosition downRightPosition = new ChessPosition(downTwo, rightOne);
                ChessMove downRightMove = new ChessMove(position, downRightPosition, null);
                moves.add(downRightMove);
            }

            if (leftOne >= 1) {
                ChessPosition downLeftPosition = new ChessPosition(downTwo, leftOne);
                ChessMove downLeftMove = new ChessMove(position, downLeftPosition, null);
                moves.add(downLeftMove);
            }
        }

        // checks left 2 up 1 and down 1
        if (leftTwo >= 1) {
            if (upOne <= 8) {
                ChessPosition leftUpPosition = new ChessPosition(upOne, leftTwo);
                ChessMove leftUpMove = new ChessMove(position, leftUpPosition, null);
                moves.add(leftUpMove);
            }

            if (downOne >= 1) {
                ChessPosition leftDownPosition = new ChessPosition(downOne, leftTwo);
                ChessMove leftDownMove = new ChessMove(position, leftDownPosition, null);
                moves.add(leftDownMove);
            }
        }


        System.out.println("Moves: " + moves);
        return moves;
    }
}

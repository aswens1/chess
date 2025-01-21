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

        int zeroRow = position.getRow() - 1;
        int zeroColumn = position.getColumn() - 1;

        // checks all the moves going up and to the right
        int upRightRow = position.getRow() + 1;
        int upRightColumn = position.getColumn() + 1;

        while(zeroRow >= 0 && zeroRow < 8 && zeroColumn >= 0 && zeroColumn < 8) {
            ChessPosition zeroPosition = new ChessPosition(zeroRow, zeroColumn);
            ChessPosition newPosition = new ChessPosition(upRightRow, upRightColumn);
            ChessPiece occupiedPiece = board.getPiece(zeroPosition);
            ChessMove newMove = new ChessMove(position, newPosition, null);

            if (occupiedPiece == null) {
                moves.add(newMove);
                upRightRow++;
                upRightColumn++;

                zeroRow++;
                zeroColumn++;
            } else if (occupiedPiece == piece) {
                zeroRow++;
                zeroColumn++;
            } else {
                if (occupiedPiece.getTeamColor() != myColour) {
                    moves.add(newMove);
                }
                break;
            }
            System.out.println("Moves: " + moves);
            }
        return moves;
    }
}
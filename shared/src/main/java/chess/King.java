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

        return moves;
    }
}

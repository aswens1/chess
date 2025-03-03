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

    public static List<ChessMove> createKnightsMoveList(ChessPosition startingPosition, ChessBoard board, ChessGame.TeamColor myTeam) {
        List<ChessMove> moves = new ArrayList<>();

        int row = startingPosition.getRow();
        int col = startingPosition.getColumn();

        // up two right one
        moves.addAll(knightMoveCalculator(startingPosition, board, row + 1, col + 2, myTeam));
        // up two left one
        moves.addAll(knightMoveCalculator(startingPosition, board, row - 1, col + 2, myTeam));

        // right two up one
        moves.addAll(knightMoveCalculator(startingPosition, board, row + 2, col + 1, myTeam));
        // right two down one
        moves.addAll(knightMoveCalculator(startingPosition, board, row + 2, col - 1, myTeam));

        // down two left one
        moves.addAll(knightMoveCalculator(startingPosition, board, row - 1, col - 2, myTeam));
        // down two right one
        moves.addAll(knightMoveCalculator(startingPosition, board, row + 1, col - 2, myTeam));

        // left two down one
        moves.addAll(knightMoveCalculator(startingPosition, board, row - 2, col - 1, myTeam));
        // left two up one
        moves.addAll(knightMoveCalculator(startingPosition, board, row - 2, col + 1, myTeam));


        return moves;
    }

    public static List<ChessMove> knightMoveCalculator(ChessPosition startPosition, ChessBoard board, int rowDirection,
                                                       int colDirection, ChessGame.TeamColor myTeam) {
        List<ChessMove> moves = new ArrayList<>();

        if ((rowDirection > 0 && rowDirection <= 8) && (colDirection > 0 && colDirection <= 8)) {
            ChessPosition newPosition = new ChessPosition(rowDirection, colDirection);
            ChessPiece occupiedPiece = board.getPiece(newPosition);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);

            if (occupiedPiece == null) {
                moves.add(newMove);
            } else {
                if (occupiedPiece.getTeamColor() != myTeam) {
                    moves.add(newMove);
                }
            }
        }

        return moves;
    }
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

        moves.addAll(createKnightsMoveList(position, board, myColour));

        return moves;
    }
}

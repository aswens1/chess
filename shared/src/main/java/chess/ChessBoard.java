package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 * <p>
 * Some important things to note:
 * <p>
 * This class is in charge or resetting and setting up the chess board. A chess board is a 8x8 square.
 * <p>
 * Each team has 8 pawns, 2 rooks, 2 knights, 2 bishops, 1 king, and 1 queen.
 * <p>
 * When counting rows in chess, they go from 1-8 from the bottom up. The columns move from 1-8 from the left to the right.
 * <p>
 * The white pieces begin on the bottom of the board, and move up. The black pieces begin at the top of the board and move down.
 * <p>
 * This class also controls the method for changing how the pieces are outputted to make it readable when debugging and comparing test results.
 */

public class ChessBoard {
    /**
     * Before anything else, we need to declare the size of the board. This is done with an array.
     */
    private final ChessPiece[][] squares = new ChessPiece[8][8];
    /** The chessboard method will ultimately be the one calling for the resetting of the board and the adding the pieces.
     * This isn't implemented yet. */
    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard.
     * <p>
     * Important note about the add piece: remember that chessboards are counted from 1-8, but an array is counted from 0-7.
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     * <p>
     * Just like addPiece, a chessboard is counted from 1-8, but an array is counted from 0-7.
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     * <p>
     * There are a few steps to this one:
     * <p>
     * You want to make sure you set every position on the board to null, so you
     * don't have any straggling pieces where they don't belong.
     * <p>
     * Remember that pawns will all be in the same row, and is
     * the only piece present in that row. The order the rest of the pieces are in goes like this, from left to right:
     * <p>
     * Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook.
     */
    public void resetBoard() {
        for (int row = 1; row < 8; row++) {
            for (int column = 1; column < 8; column++) {
                squares[row][column] = null;
            }
        }

        for (int column = 1; column <= 8; column++) {
            // place pawns
            addPiece(new ChessPosition(2, column), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, column), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        //place white rooks
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        // place black rooks
        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        //place white knights
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));

        //place black knights
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));

        //place white bishops
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));

        //place black bishops
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));

        //place white queen and king
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

        //place black queen and king
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
    }

    /**
     * This is an override that can be generated by right-clicking on the class code.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    /**
     * This is an override that can be generated by right-clicking on the class code.
     * @return
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * This is an override that can be generated by right-clicking on the class code.
     * <p>
     * However, some edits are needed to make it readable to humans. An efficient way to do this is to use a StringBuilder,
     * which allows you to easily append strings and variables and print it out in a readable way. By using the chessboard
     * array declared above, you can easily just put in the board that was already created. You'll need to go through both
     * rows (start from the top) and columns (start from the left). Use a symbol such as - or * or . to represent a
     * blank spot on the chessboard.
     * <p>
     * Make sure to call the ShorterString method to properly display the pieces.
     * @return a readable string displaying what the chessboard looks like
     */
    @Override
    public String toString() {
        StringBuilder readableString = new StringBuilder();
        for (int row = 8; row >= 1; row--) {
            for (int column = 1; column <= 8; column++) {
                ChessPiece piece = squares[row - 1][column - 1];
                if (piece == null) {
                    readableString.append("- ");
                } else {
                    readableString.append(ShorterString(piece)).append(" ");
                }
            }
            readableString.append(row).append("\n");
        }
        readableString.append("1 2 3 4 5 6 7 8\n");
        return readableString.toString();
    }

    /**
     * This method is important for making the toString method work. It works by SWITCHING the piece type with a symbol
     * that represents that piece. K for king, B for bishop, Q for queen, N for knight, P for pawn, R for rook.
     * <p>
     * When returning the symbol, make sure that the white pieces are upper case and the black pieces are lower case. This
     * will make it easier to differentiate.
     * @param piece
     * @return a symbol that represents the piece and what team it is on.
     */
    public String ShorterString(ChessPiece piece) {
        String symbol = "";
        switch (piece.getPieceType()) {
            case KING -> symbol = "K";
            case BISHOP -> symbol = "B";
            case QUEEN -> symbol = "Q";
            case KNIGHT -> symbol = "N";
            case PAWN -> symbol = "P";
            case ROOK -> symbol = "R";
        }
        return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? symbol.toUpperCase() : symbol.toLowerCase();
    }
}
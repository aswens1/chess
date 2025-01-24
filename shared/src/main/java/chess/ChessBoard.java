package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        resetBoard();
        // sets up board with the starting positions
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
        //System.out.println("Adding " + piece + " at row: " + position.getRow() + ", column: " + position.getColumn());
    }

    /**
     * Gets a chess piece on the chessboard
     *
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
     */
    public void resetBoard() {
        for (int row = 1; row < 8; row++) {
            for (int column = 1; column < 8; column++) {
                squares[row][column] = null;
            }
        }

//        for (int column = 1; column <= 8; column++) {
//            // place pawns
//            addPiece(new ChessPosition(2, column), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
//            addPiece(new ChessPosition(7, column), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
//        }
//
//        //place white rooks
//        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
//        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
//
//        // place black rooks
//        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
//        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
//
//        //place white knights
//        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
//        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
//
//        //place black knights
//        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
//        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
//
//        //place white bishops
//        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
//        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
//
//        //place black bishops
//        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
//        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
//
//        //place white queen and king
//        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
//        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
//
//        //place black queen and king
//        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
//        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }


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
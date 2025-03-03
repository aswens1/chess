package chess;

public class ChessMovesCommonMethods {

    private final ChessBoard board;
    private final ChessPosition position;

    public ChessMovesCommonMethods(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    ChessPiece getChessPiece() {
        return board.getPiece(position);
    }

}

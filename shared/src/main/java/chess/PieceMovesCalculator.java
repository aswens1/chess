package chess;

public class PieceMovesCalculator {

    private ChessPiece.PieceType pieceType;
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessBoard chessBoard;

    public PieceMovesCalculator(ChessPiece.PieceType type, ChessPosition startPos,
                                ChessPosition endPos, ChessBoard board) {
        this.pieceType = type;
        this.startPosition = startPos;
        this.endPosition = endPos;
        this.chessBoard = board;
    }
}

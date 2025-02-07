package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private ChessGame.TeamColor turn;

    /**
     * Sets up a new game by creating a new board,
     * resetting it to put all the pieces where they belong,
     * and setting the first turn as white.
     */
    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> legalMoves = new ArrayList<>();

        System.out.println("Raw moves before legality check: " + moves);

        for (ChessMove move : moves) {
            if (testMove(move)) {
                legalMoves.add(move);
            }
        }

        System.out.println("Final legal moves: " + legalMoves);

        return legalMoves;
    }

    boolean testMove(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece startPiece = board.getPiece(move.getStartPosition());
        ChessPiece endPiece = board.getPiece(move.getEndPosition());

        ChessGame.TeamColor turnBeforeMove = getTeamTurn();

        movePiece(startPiece, start, end, move);
        boolean legal = !isInCheck(startPiece.getTeamColor());

        setTeamTurn(turnBeforeMove);

        board.removePiece(end);
        board.addPiece(start, startPiece);
        board.addPiece(end, endPiece);

        System.out.println("Testing move: " + start + " -> " + end + " | Legal? " + legal);
        return legal;
    }

    public void movePiece(ChessPiece startPiece, ChessPosition startPosition, ChessPosition endPosition, ChessMove move) {

        if (move.getPromotionPiece() != null) {
            startPiece = new ChessPiece(startPiece.getTeamColor(), move.getPromotionPiece());
        }

        board.addPiece(endPosition, startPiece);

        board.removePiece(startPosition);

        if (getTeamTurn() == TeamColor.WHITE) {
          setTeamTurn(TeamColor.BLACK);
        } else {
          setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece startPiece = board.getPiece(start);

        Collection<ChessMove> legalMoves = validMoves(start);

        if (startPiece == null) {
            throw new InvalidMoveException("Starting Position is Empty");
        }

        if (startPiece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("It's not your turn");
        }

        if (legalMoves.contains(move)) {
            movePiece(startPiece, start, end, move);
        } else {
            throw new InvalidMoveException("Invalid Move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <=8; column++) {
                ChessPosition position = new ChessPosition(row, column);
                ChessPiece currentPiece = board.getPiece(position);

                if ((currentPiece != null) && (currentPiece.getTeamColor() != teamColor)) {
                    Collection<ChessMove> moves = currentPiece.pieceMoves(board, position);
                    for (ChessMove possibleMove : moves) {
                        ChessPosition endPos = possibleMove.getEndPosition();
                        ChessPiece endPiece = board.getPiece(endPos);
                        if ((endPiece != null) && (endPiece.getPieceType() == ChessPiece.PieceType.KING)
                                && (endPiece.getTeamColor() == teamColor)) {
                            System.out.println("CHECK DETECTED: " + currentPiece + " at " + position);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }


    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}

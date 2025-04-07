package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class GameBoardDrawing {

    private static ServerFacade sf;

    private static final int BOARD_SIZE_IN_SQUARES = 8;

    private static final String DARK_SQUARE = SET_BG_COLOR_BLUE;
    private static final String LIGHT_SQUARE = SET_BG_COLOR_WHITE;

    public static void drawBoard(ChessGame.TeamColor pov, ChessBoard board, ChessPosition highlightPosition) {

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        Collection<ChessMove> validMoves = List.of();
        ChessGame currentGame = sf.getGame();
        if (highlightPosition != null) {
            validMoves = currentGame.validMoves(highlightPosition);
        }

        printLetters(out, pov);
        printSquares(out, pov, BOARD_SIZE_IN_SQUARES, board, validMoves, highlightPosition);
        printLetters(out, pov);

        out.println();
    }

    public static void printLetters(PrintStream out, ChessGame.TeamColor pov) {
        String[] headers = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};

        out.print(SET_BG_COLOR_LIGHT_GREY);

        if (pov == ChessGame.TeamColor.WHITE) {
            for (int i = 0; i <= 9; i++) {
                out.print(headers[i]);
            }
        } else {
            for (int i = headers.length - 1; i >= 0; i--) {
                out.print(headers[i]);
            }
        }

        out.print(RESET_BG_COLOR);
        out.println();
    }

    public static void printNumbers(PrintStream out, int rowNum) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(" " + rowNum + " ");
        out.print(RESET_BG_COLOR);
    }

    public static void printSquares(PrintStream out, ChessGame.TeamColor pov, int row, ChessBoard board, Collection<ChessMove> validMoves, ChessPosition highlightPosition) {

        if (pov == ChessGame.TeamColor.BLACK) {
            for (int i = 1; i <= 8; i++) {
                eachRow(out, row, i, board, pov, validMoves, highlightPosition);
                row--;
            }
        } else {
            for (int i = 8; i >= 1; i--) {
                eachRow(out, row, i, board, pov, validMoves, highlightPosition);
                row--;
            }
        }
    }

    public static void printRowBackground(PrintStream out, int row, ChessBoard board, ChessGame.TeamColor pov, Collection<ChessMove> validMoves, ChessPosition highlightPosition) {

        for (int i = 1; i <= BOARD_SIZE_IN_SQUARES; i++) {

            boolean isDark;
            ChessPosition currentPos;
            String pieceToPrint = "";
            
            if (pov.equals(ChessGame.TeamColor.BLACK)) {
                isDark = (row + (BOARD_SIZE_IN_SQUARES - i + 1)) % 2 == 0;
                currentPos = new ChessPosition(row, BOARD_SIZE_IN_SQUARES - i + 1);
            } else {
                isDark = (row + i) % 2 == 0;
                currentPos = new ChessPosition(row, i);
            }
            pieceToPrint = getPieceToPrint(board, currentPos);

            out.print(SET_TEXT_COLOR_BLACK);
            out.print(isDark ? DARK_SQUARE + pieceToPrint : LIGHT_SQUARE + pieceToPrint);
        }
        out.print(RESET_TEXT_COLOR);
    }

    public static String getPieceToPrint(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position) == null) {
            return EMPTY;
        } else {
            ChessPiece piece = board.getPiece(position);
            boolean whitePiece = (piece.getTeamColor() == ChessGame.TeamColor.WHITE);

            return switch (piece.getPieceType()) {
                case KING -> whitePiece ? WHITE_KING : BLACK_KING;
                case QUEEN -> whitePiece ? WHITE_QUEEN : BLACK_QUEEN;
                case BISHOP -> whitePiece ? WHITE_BISHOP : BLACK_BISHOP;
                case KNIGHT -> whitePiece ? WHITE_KNIGHT : BLACK_KNIGHT;
                case ROOK -> whitePiece ? WHITE_ROOK : BLACK_ROOK;
                case PAWN -> whitePiece ? WHITE_PAWN : BLACK_PAWN;
            };
        }
    }

    public static void eachRow(PrintStream out, int row, int i, ChessBoard board, ChessGame.TeamColor pov, Collection<ChessMove> validMoves, ChessPosition highlightPosition) {
        int adjustForBlackPOV = ((pov == ChessGame.TeamColor.BLACK) ? (BOARD_SIZE_IN_SQUARES - row + 1) : row);
        printNumbers(out, i);
        printRowBackground(out, adjustForBlackPOV, board, pov, validMoves, highlightPosition);
        printNumbers(out, i);
        out.println();
    }
}
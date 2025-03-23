package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GameBoardDrawing {

    private static final int BOARD_SIZE_IN_SQUARES = 8;

    private static final String darkSquare = SET_BG_COLOR_BLUE;
    private static final String lightSquare = SET_BG_COLOR_WHITE;

    public static void DrawBoard(ChessGame.TeamColor pov, ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        printLetters(out, pov);
        printSquares(out, pov, BOARD_SIZE_IN_SQUARES, board);
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

    public static void printSquares(PrintStream out, ChessGame.TeamColor pov, int row, ChessBoard board) {
        if (pov == ChessGame.TeamColor.BLACK) {
            for (int i = 1; i <= 8; i++) {
                eachRow(out, row, i, board, pov);
                row--;
            }
        } else {
            for (int i = 8; i >= 1; i--) {
                eachRow(out, row, i, board, pov);
                row--;
            }
        }
    }

    public static void printRowBackground(PrintStream out, int row, ChessBoard board) {
        for (int column = 1; column <= BOARD_SIZE_IN_SQUARES; column++) {

            ChessPosition currentPos = new ChessPosition(row, column);
            String pieceToPrint = getPieceToPrint(board, currentPos);

            boolean isDark = (row + column) % 2 == 0;
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(isDark ? darkSquare + pieceToPrint : lightSquare + pieceToPrint);
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


    public static void eachRow(PrintStream out, int row, int i, ChessBoard board, ChessGame.TeamColor pov) {
        int adjustForBlackPOV = ((pov == ChessGame.TeamColor.BLACK) ? (BOARD_SIZE_IN_SQUARES - row + 1) : row);
        printNumbers(out, i);
        printRowBackground(out, adjustForBlackPOV, board);
        printNumbers(out, i);
        out.println();
    }
}

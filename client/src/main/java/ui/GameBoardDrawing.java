package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.ChessGame;

import static ui.EscapeSequences.*;

public class GameBoardDrawing {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    private static final String darkSquare = SET_BG_COLOR_BLUE + "   " + RESET_BG_COLOR;
    private static final String lightSquare = SET_BG_COLOR_WHITE + "   " + RESET_BG_COLOR;
    private static final String border = SET_BG_COLOR_LIGHT_GREY + "   " + RESET_BG_COLOR;


    public static void drawBoard(ChessGame.TeamColor pov) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        printLetters(out, pov);
        printSquares(out, pov, BOARD_SIZE_IN_SQUARES);
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
            for (int i = headers.length - 1; i >= 0; i--){
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

    public static void printSquares(PrintStream out, ChessGame.TeamColor pov, int row) {
//        for (int column = 1; column <= BOARD_SIZE_IN_SQUARES; column++) {

        if (pov == ChessGame.TeamColor.BLACK) {
            for (int i = 1; i <= 8; i++) {
                individualSquare(out, row, i);
                row--;
            }
        } else {
            for (int i = 8; i >= 1; i--) {
                individualSquare(out, row, i);
                row--;
            }
        }
    }

    public static void printRowBackground(PrintStream out, int row) {
        for (int column = 1; column <= BOARD_SIZE_IN_SQUARES; column++) {
            boolean isDark = (row + column) % 2 == 0;
            out.print(isDark ? darkSquare : lightSquare);
        }
    }

    public static void individualSquare(PrintStream out, int row, int i) {
            printNumbers(out, i);
            printRowBackground(out, row);
            printNumbers(out, i);
            out.println();
    }
}

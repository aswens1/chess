package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameDataRecord;

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

        printHeaders(out, pov);

        for (int rows = BOARD_SIZE_IN_SQUARES; rows >= 1; rows--) {
            out.print(border);

            for (int columns = 1; columns <= BOARD_SIZE_IN_SQUARES; columns++) { // A to H
                boolean isDark = (rows + columns) % 2 == 0;
                out.print(isDark ? darkSquare : lightSquare);
            }
            out.print(border);
            out.println();
        }

        printHeaders(out, pov);

        out.println();
    }

    public static void printHeaders(PrintStream out, ChessGame.TeamColor pov) {
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


}

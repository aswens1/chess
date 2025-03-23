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


    public static void drawBoard(ChessGame.TeamColor pov) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        if (pov == ChessGame.TeamColor.WHITE) {
            whitePerspective(out);
        }

    }

    public static void whitePerspective(PrintStream out) {
        for (int rows = 1; rows <= BOARD_SIZE_IN_SQUARES; rows++) {
            for (int columns = 1; columns <= BOARD_SIZE_IN_SQUARES; columns++) { // A to H
                boolean isDark = (rows + columns) % 2 == 0;
                out.print(isDark ? darkSquare : lightSquare);
            }
            out.println(RESET_BG_COLOR); // Move to next row
        }

    }

}

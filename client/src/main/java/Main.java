import chess.*;
import ui.GameBoardDrawing;
import ui.PostLoginUI;
import ui.PreLoginUI;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Session session = new Session();
    private static final PreLoginUI pre = new PreLoginUI();
    private static final PostLoginUI post = new PostLoginUI();

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.println(" ♕ Welcome to 240 Chess! Enter a command to get started. ♕ ");

        ChessBoard startingBoard = new ChessBoard();
        startingBoard.resetBoard();

        GameBoardDrawing.DrawBoard(ChessGame.TeamColor.WHITE, startingBoard);
        GameBoardDrawing.DrawBoard(ChessGame.TeamColor.BLACK, startingBoard);

//        while (true) {
//            String command = scanner.nextLine().trim();
//            if (session.loggedIn()) {
//                post.postLoginCommandHandler(out, command);
//            } else {
//                pre.preLoginCommandHandler(out, command);
//            }
//        }
    }
}
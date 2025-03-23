import chess.*;
import ui.PreLoginUI;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Session session = new Session();
    private static final PreLoginUI pre = new PreLoginUI();

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.println(" ♕ Welcome to 240 Chess! Type help to get started. ♕ ");

        while (true) {
            String command = scanner.nextLine().trim();

            if (session.loggedIn()) {
                //Post login UI
            } else {
                if (command.equalsIgnoreCase("HELP")) {
                    pre.displayHelp(out);
                }
                pre.preLoginCommandHandler(out, command);
            }
        }

    }
}
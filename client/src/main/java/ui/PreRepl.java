package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PreRepl {

    private final ChessClient client;

    public PreRepl(String serverURL) {
        client = new PreClient(serverURL, this);
    }


    public void run() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println(" ♕ Welcome to 240 Chess! Enter a command to get started. ♕ ");

        Scanner scanner = new Scanner(System.in);
        var command = "";

        while (!command.equals("quit")) {
            String line = scanner.nextLine();

            try {
                command = client.eval(line);
                System.out.print(command);
                System.out.println();
            } catch (Throwable e) {
                var message = e.toString();
                System.out.print(message);
            }
        }
    }
}

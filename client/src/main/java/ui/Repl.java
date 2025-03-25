package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Repl {

    private final ChessClient preClient;
    private final ChessClient postClient;


    public Repl(String serverURL) {
        State state = new State();
        preClient = new PreClient(serverURL, this, state);
        postClient = new PostClient(serverURL, this, state);
    }


    public void run() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        State state = new State();
        out.println(" ♕ Welcome to 240 Chess! Enter a command to get started. ♕ ");

        Scanner scanner = new Scanner(System.in);
        var command = "";

        while (true) {
            String line = scanner.nextLine();

            try {
                if (!state.isLoggedIn()) {
                    command = preClient.eval(line);
                } else {
                    command = postClient.eval(line);
                }
                System.out.println(command);

                if(line.equalsIgnoreCase("quit")) {
                    System.exit(200);
                }

            } catch (Throwable e) {
                var message = e.toString();
                System.out.print(message);
            }
        }
    }

}

package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PostRepl {

    private final String authToken;
    private final String username;
    private final String serverURL;


    public PostRepl(String serverURL, String authToken, String username) {
        this.serverURL = serverURL;
        this.authToken = authToken;
        this.username = username;
    }

    public void run() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println(" ♕ Welcome to 240 Chess, " + SET_TEXT_COLOR_BLUE + username + RESET_TEXT_COLOR + "! Enter a command to get started. ♕");
    }
}

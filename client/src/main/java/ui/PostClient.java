package ui;

import exception.ResponseException;

import java.util.Arrays;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PostClient implements ChessClient {

    private final String serverURL;
    private final Repl repl;
    private final State state;
    private final ServerFacade sf;

    public PostClient(String serverURL, Repl repl, State state) {
        sf = new ServerFacade(serverURL);
        this.serverURL = serverURL;
        this.repl = repl;
        this.state = state;
    }

    @Override
    public String eval(String input) {
        try {
            String[] partsOfCommand = input.toLowerCase().split(" ");
            var command = (partsOfCommand.length > 0) ? partsOfCommand[0] : "help";
            var params = Arrays.copyOfRange(partsOfCommand, 1, partsOfCommand.length);
            return switch (command) {
                case "logout" -> logout();
                case "create <NAME>" -> create(params);
                case "join" -> join(params);
                case "list" -> list();
                case "observe" -> observe(params);
                case "quit" -> quit();
                default -> displayHelp();
            };

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String logout() {
        return "";
    }

    public String create(String... params) {
        return "";
    }

    public String join(String... params) {
        return "";
    }

    public String list() {
        return "";
    }

    public String observe(String... params) {
        return "";
    }

    public String quit() {
        return "";
    }

    public String displayHelp(){
        String[] helpCommands = {
                SET_TEXT_COLOR_BLUE + "logout" + RESET_TEXT_COLOR + " -> log out of your account",
                SET_TEXT_COLOR_BLUE + "create <NAME>" + RESET_TEXT_COLOR + " -> create a new game",
                SET_TEXT_COLOR_BLUE + "join <ID>" + RESET_TEXT_COLOR + " -> join a game",
                SET_TEXT_COLOR_BLUE + "list" + RESET_TEXT_COLOR + " -> list all games",
                SET_TEXT_COLOR_BLUE + "observe <ID>" + RESET_TEXT_COLOR + " -> watch a game",
                SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " -> exit program",
                SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " -> list possible commands"
        };
        return String.join("\n", helpCommands);
    }
}

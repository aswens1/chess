package ui;
import ui.EscapeSequences.*;
import java.io.PrintStream;
import exception.ResponseException;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PreClient implements ChessClient {

    private final String serverURL;
    private final PreRepl preRepl;

    public PreClient(String serverURL, PreRepl preRepl) {
        this.serverURL = serverURL;
        this.preRepl = preRepl;
    }

    @Override
    public String eval(String input) {
        try {
            String[] partsOfCommand = input.toLowerCase().split(" ");
            var command = (partsOfCommand.length > 0) ? partsOfCommand[0] : "help";

            return switch (command) {
                case "register" -> register(partsOfCommand[1], partsOfCommand[2], partsOfCommand[3]);
                case "login" -> login(partsOfCommand[1], partsOfCommand[2]);
                case "quit" -> quit();
                default -> displayHelp();
            };

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String quit() {
        return "";
    }

    private String login(String username, String password) {
        return "";
    }

    private String register(String username, String password, String email) {
        return "";
    }

    public String displayHelp(){
        String[] helpCommands = {
                SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.RESET_TEXT_COLOR + " -> create an account",
                SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + EscapeSequences.RESET_TEXT_COLOR + " -> play chess",
                SET_TEXT_COLOR_BLUE + "quit" + EscapeSequences.RESET_TEXT_COLOR + " -> exit program",
                SET_TEXT_COLOR_BLUE + "help" + EscapeSequences.RESET_TEXT_COLOR + " -> list possible commands"
        };
        return String.join("\n", helpCommands);
    }

}

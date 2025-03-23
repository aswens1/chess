package ui;
import java.io.PrintStream;

public class PreLoginUI {

    public void displayHelp(PrintStream out){
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.RESET_TEXT_COLOR + " -> create an account"); // Blue text
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + EscapeSequences.RESET_TEXT_COLOR + " -> play chess");
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "quit" + EscapeSequences.RESET_TEXT_COLOR + " -> exit program");
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help" + EscapeSequences.RESET_TEXT_COLOR + " -> list possible commands");
    }

    public void preLoginCommandHandler(PrintStream out, String command) {
        String lowerCase = command.toLowerCase();
        if (lowerCase.contains("register")) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Register a new user: " + EscapeSequences.RESET_TEXT_COLOR);
            // somehow connect to the server and register a user
        } else if (lowerCase.contains("login")) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Login existing user: " + EscapeSequences.RESET_TEXT_COLOR);
            // somehow connect to the server and login the user
        } else if (lowerCase.contains("quit")) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Exiting program." + EscapeSequences.RESET_TEXT_COLOR);
            System.exit(0);
        } else if (lowerCase.contains("help")) {
            displayHelp(out);
        } else {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Invalid command. Type help for a list of commands." + EscapeSequences.RESET_TEXT_COLOR);
        }
    }
}

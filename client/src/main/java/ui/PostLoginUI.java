package ui;

import java.io.PrintStream;

public class PostLoginUI {

    public void displayHelp(PrintStream out){
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "logout" + EscapeSequences.RESET_TEXT_COLOR + " -> log out of your account"); // Blue text
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "create <NAME>" + EscapeSequences.RESET_TEXT_COLOR + " -> create a new chess game");
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]" + EscapeSequences.RESET_TEXT_COLOR + " -> list all games");
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "observe <ID>" + EscapeSequences.RESET_TEXT_COLOR + " -> watch a game");
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help" + EscapeSequences.RESET_TEXT_COLOR + " -> list possible commands");
    }

    public void postLoginCommandHandler(PrintStream out, String command) {

        String lowerCase = command.toLowerCase();

        if (lowerCase.contains("logout")) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Logging out. " + EscapeSequences.RESET_TEXT_COLOR);
        } else if (command.equalsIgnoreCase("create")) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Create a new chess game: " + EscapeSequences.RESET_TEXT_COLOR);
        } else if (lowerCase.contains("join")){
                out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Joining game as player." + EscapeSequences.RESET_TEXT_COLOR);
        } else if (lowerCase.contains("observe")){
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Joining game as observer." + EscapeSequences.RESET_TEXT_COLOR);
        } else if (command.equalsIgnoreCase("quit")) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Exiting program." + EscapeSequences.RESET_TEXT_COLOR);
            System.exit(0);
        } else if (command.equalsIgnoreCase("help")) {
            displayHelp(out);
        } else {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Invalid command. Type help for a list of commands." + EscapeSequences.RESET_TEXT_COLOR);
        }
    }

}

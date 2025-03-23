package ui;

import java.io.PrintStream;

public class PostLoginUI {

    public void displayHelp(PrintStream out){
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "logout" +
                EscapeSequences.RESET_TEXT_COLOR + " -> log out of your account");
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "create <NAME>" +
                EscapeSequences.RESET_TEXT_COLOR + " -> create a new chess game");
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]" +
                EscapeSequences.RESET_TEXT_COLOR + " -> join specific game as the white or black player");
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "list" +
                EscapeSequences.RESET_TEXT_COLOR + " -> list all games");
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "observe <ID>" +
                EscapeSequences.RESET_TEXT_COLOR + " -> watch a game");
        out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help" +
                EscapeSequences.RESET_TEXT_COLOR + " -> list possible commands");
    }

    public void postLoginCommandHandler(PrintStream out, String command) {
        String[] partsOfCommand = command.split(" ");

        if (partsOfCommand.length == 0) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Invalid command. Type help for a list of commands." + EscapeSequences.RESET_TEXT_COLOR);
        }

        String actualCommand = partsOfCommand[0].toLowerCase();

        if (actualCommand.equals("logout") && partsOfCommand.length == 1) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Logging out. " + EscapeSequences.RESET_TEXT_COLOR);



        } else if (command.equals("create") && partsOfCommand.length == 2) {

            String gameName = partsOfCommand[1];
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Creating new chess game: " + EscapeSequences.SET_TEXT_COLOR_YELLOW + gameName + EscapeSequences.RESET_TEXT_COLOR);




        } else if (actualCommand.equals("join") && partsOfCommand.length == 3){
            String id = partsOfCommand[1];
            String playerColour = partsOfCommand[2];

            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Joining " +
                    EscapeSequences.SET_TEXT_COLOR_YELLOW + id + EscapeSequences.RESET_TEXT_COLOR + " as " +
                    EscapeSequences.SET_TEXT_COLOR_YELLOW + playerColour + " player." + EscapeSequences.RESET_TEXT_COLOR);



        } else if (actualCommand.equals("observe") && partsOfCommand.length == 2){
            String id = partsOfCommand[1];

            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Joining " + id +
                    " as observer." + EscapeSequences.RESET_TEXT_COLOR);




        } else if (actualCommand.equals("quit") && partsOfCommand.length == 1) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Exiting program." + EscapeSequences.RESET_TEXT_COLOR);
            System.exit(0);




        } else if (actualCommand.equals("help") && partsOfCommand.length == 1) {
            displayHelp(out);

        } else {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Invalid command. Type " +
                    EscapeSequences.SET_TEXT_COLOR_YELLOW + "help" + " for a list of commands." +
                    EscapeSequences.RESET_TEXT_COLOR);
        }
    }

}

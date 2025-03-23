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
        String[] partsOfCommand = command.split(" ");

        if (partsOfCommand.length == 0) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Invalid command. Type help for a list of commands." + EscapeSequences.RESET_TEXT_COLOR);
        }

        String actualCommand = partsOfCommand[0].toLowerCase();

        if (actualCommand.equals("register") && partsOfCommand.length == 4) {

            String username = partsOfCommand[1];
            String password = partsOfCommand[2];
            String email = partsOfCommand[3];

            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Registering new user: " + EscapeSequences.SET_TEXT_COLOR_YELLOW + username + EscapeSequences.RESET_TEXT_COLOR);





        } else if (actualCommand.equals("login") && partsOfCommand.length == 3) {

            String username = partsOfCommand[1];
            String password = partsOfCommand[2];

            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Logging in: " + EscapeSequences.SET_TEXT_COLOR_YELLOW + username + EscapeSequences.RESET_TEXT_COLOR);




        } else if (actualCommand.equals("quit") && partsOfCommand.length == 1) {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Exiting program." + EscapeSequences.RESET_TEXT_COLOR);
            System.exit(0);
        } else if (actualCommand.equals("help") && partsOfCommand.length == 1) {
            displayHelp(out);
        } else {
            out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Invalid command. Type help for a list of commands." + EscapeSequences.RESET_TEXT_COLOR);
        }
    }
}

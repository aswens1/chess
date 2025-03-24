package ui;
import service.records.LoginRequest;
import service.records.LoginResult;
import service.records.RegisterRequest;
import service.records.RegisterResult;
import java.util.Arrays;

import exception.ResponseException;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PreClient implements ChessClient {

    private final String serverURL;
    private final PreRepl preRepl;
    private State state = State.LOGGED_OUT;
    private final ServerFacade sf;


    public PreClient(String serverURL, PreRepl preRepl) {
        sf = new ServerFacade(serverURL);
        this.serverURL = serverURL;
        this.preRepl = preRepl;
    }

    @Override
    public String eval(String input) {
        try {
            String[] partsOfCommand = input.toLowerCase().split(" ");
            var command = (partsOfCommand.length > 0) ? partsOfCommand[0] : "help";
            var params = Arrays.copyOfRange(partsOfCommand, 1, partsOfCommand.length);
            return switch (command) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> quit();
                default -> displayHelp();
            };

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String register(String... params) {
        if (params.length != 3) {
            throw new ResponseException(400, SET_TEXT_COLOR_BLUE + "Expected: <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR);
        }

        String username = params[0];
        String password = params[1];
        String email = params[2];

        if ((username == null || username.isEmpty()) ||
                (password == null || password.isEmpty()) || (email == null || email.isEmpty())) {
            throw new ResponseException(400, SET_TEXT_COLOR_BLUE + "Expected: <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR);
        }

        RegisterResult register = sf.registerUser(new RegisterRequest(username, password, email));
        String authToken = register.authToken();

        if (!authToken.isEmpty()) {
            sf.setAuth(authToken);
            // move to post login
        } else {
            throw new ResponseException(500, "Server did not return a valid response.");
        }
        return "Welcome " + SET_TEXT_COLOR_BLUE + register.username() + RESET_TEXT_COLOR + "!";
    }

    private String quit() {
        return "";
    }

    private String login(String... params) {
        if (params.length != 2) {
            throw new ResponseException(400, SET_TEXT_COLOR_BLUE + "Expected: <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR);
        }

        String username = params[0];
        String password = params[1];

        if ((username == null || username.isEmpty()) || (password == null || password.isEmpty())) {
            throw new ResponseException(400, SET_TEXT_COLOR_BLUE + "Expected: <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR);
        }

        LoginResult login = sf.loginUser(new LoginRequest(username, password));
        String authToken = login.authToken();
        String user = login.username();

        if (!authToken.isEmpty()) {
            sf.setAuth(authToken);
            sf.setUsername(user);
            // move to post login
        } else {
            throw new ResponseException(500, "Server did not return a valid response.");
        }

        return "Logging " + SET_TEXT_COLOR_BLUE + login.username() + RESET_TEXT_COLOR + " in!";
    }

    public String displayHelp(){
        String[] helpCommands = {
                SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR + " -> create an account",
                SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR + " -> play chess",
                SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " -> exit program",
                SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " -> list possible commands"
        };
        return String.join("\n", helpCommands);
    }
}

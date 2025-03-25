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

    private State state;
    private final ServerFacade sf;


    public PreClient(ServerFacade sf, State state) {
        this.sf = sf;
        this.state = state;
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

        try {
            state.stateLogIn();
            return "Welcome " + SET_TEXT_COLOR_BLUE + register.username() + RESET_TEXT_COLOR + "!";
        } catch (ResponseException exception){
            throw new ResponseException(exception.statusCode(), exception.getMessage());
        }
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
        state.stateLogIn();
        return "Logging " + SET_TEXT_COLOR_BLUE + login.username() + RESET_TEXT_COLOR + " in!";
    }

    public String quit() {
        return "";
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

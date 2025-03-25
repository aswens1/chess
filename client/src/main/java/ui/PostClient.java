package ui;

import exception.ResponseException;
import service.records.ListGamesRequest;
import service.records.ListGamesResult;
import service.records.LogoutRequest;
import service.records.LogoutResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PostClient implements ChessClient {

    private final State state;
    private final ServerFacade sf;

    public PostClient(ServerFacade sf, State state) {
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
        try {
            sf.logoutUser(new LogoutRequest(sf.returnAuth()));
            String tempUserName = sf.getUsername();
            sf.setUsername(null);
            sf.setAuth(null);
            state.stateLogOut();
            return "Goodbye " + SET_TEXT_COLOR_BLUE + tempUserName + RESET_TEXT_COLOR + "!";
        } catch (ResponseException ex) {
            throw new ResponseException(ex.statusCode(), ex.getMessage());
        }
    }

    public String create(String... params) {
        return "";
    }

    public String join(String... params) {
        return "";
    }

    public String list() {
        try {
            ListGamesResult listOfGames = sf.list(new ListGamesRequest(sf.returnAuth()));

            List<String> numberedGames = new ArrayList<>();

            for (var i = 0; i <= listOfGames.games().size(); i++) {
                sf.createGameMap(i + 1, listOfGames.games().get(i));
                numberedGames.add(String.format("%s%d.%s %s%n", SET_TEXT_COLOR_BLUE, i + 1, RESET_TEXT_COLOR, listOfGames.games().get(i)));
            }
            return String.join("\n", numberedGames);
        } catch (ResponseException ex) {
            throw new ResponseException(ex.statusCode(), ex.getMessage());
        }
    }

    public String observe(String... params) {
        return "";
    }

    public String quit() {
        state.stateLogOut();
        System.out.println(logout());
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

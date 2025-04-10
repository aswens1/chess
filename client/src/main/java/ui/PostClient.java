package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.*;
import records.*;
import websocket.WebSocketFacade;
import websocket.commands.UserGameCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PostClient implements ChessClient {

    private final State state;
    private final ServerFacade sf;
    private final GameState gs;
    private final WebSocketFacade wsf;
    private GamePlayClient gamePlayClient;


    public PostClient(ServerFacade sf, State state, GameState gs, WebSocketFacade wsf) {
        this.sf = sf;
        this.state = state;
        this.gs = gs;
        this.wsf = wsf;
    }

    @Override
    public String eval(String input) {
        try {
            String[] partsOfCommand = input.toLowerCase().split(" ");
            var command = (partsOfCommand.length > 0) ? partsOfCommand[0] : "help";
            var params = Arrays.copyOfRange(partsOfCommand, 1, partsOfCommand.length);
            return switch (command) {
                case "logout" -> logout();
                case "create" -> create(params);
                case "join" -> join(params);
                case "list" -> list();
                case "observe" -> observe(params);
                case "quit" -> quit();
                case "help" -> displayHelp();
                case "clear" -> clearDB(params);
                default -> "Invalid command. Use command " + SET_TEXT_COLOR_BLUE + "'help'" + RESET_TEXT_COLOR + " for a list of possible commands.";
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
        if (params.length != 1) {
            throw new ResponseException(400, SET_TEXT_COLOR_BLUE + "Expected: create <NAME>" + RESET_TEXT_COLOR);
        }

        String gameName = params[0];

        if (gameName.isEmpty()) {
            throw new ResponseException(400, SET_TEXT_COLOR_BLUE + "Expected: create <NAME>" + RESET_TEXT_COLOR);
        }

        sf.create(new CreateGameRequest(gameName), sf.returnAuth());
        return "Created new game: " + SET_TEXT_COLOR_BLUE + gameName + RESET_TEXT_COLOR;
    }

    public String join(String... params) {
        if (params.length != 2) {
            throw new ResponseException(400, SET_TEXT_COLOR_BLUE + "Expected: join <ID> <WHITE|BLACK>" + RESET_TEXT_COLOR);
        }

        try {
            try {
                int userInId = Integer.parseInt(params[0]);
                ChessGame.TeamColor teamColor;

                switch (params[1].toLowerCase()) {
                    case "white":
                        teamColor = ChessGame.TeamColor.WHITE;
                        break;
                    case "black":
                        teamColor = ChessGame.TeamColor.BLACK;
                        break;
                    default:
                        return "Invalid team colour. Please choose " + SET_TEXT_COLOR_BLUE + "white" + RESET_TEXT_COLOR +
                                " or " + SET_TEXT_COLOR_BLUE + "black" + RESET_TEXT_COLOR + ".";
                }

                list();
                HashMap<Integer, CondensedGameData> game = sf.getGameMap();

                if (!game.containsKey(userInId)) {
                    return "Game " + SET_TEXT_COLOR_BLUE + userInId + RESET_TEXT_COLOR + " not found.";
                }

                CondensedGameData gameToJoin = game.get(userInId);

                int serverGameID = gameToJoin.gameID();

                if ((teamColor == ChessGame.TeamColor.WHITE && gameToJoin.whiteUsername() != null) ||
                        (teamColor == ChessGame.TeamColor.BLACK && gameToJoin.blackUsername() != null)) {
                    return "The " + SET_TEXT_COLOR_BLUE + teamColor + RESET_TEXT_COLOR + " team is already taken.";
                }

                sf.join(new JoinGameRequest(teamColor, serverGameID), sf.returnAuth());
                sf.setTeamColor(teamColor);
                gs.stateInGame();
                sf.setGameID(serverGameID);
                wsf.sendCommand(new UserGameCommand(UserGameCommand.CommandType.CONNECT, sf.returnAuth(), gameToJoin.gameID(), teamColor, sf.getUsername()));

//                GameBoardDrawing.drawBoard(teamColor, sf.getGame().getBoard(), null, sf.getGame());

                return "Joined " + SET_TEXT_COLOR_BLUE + gameToJoin.gameName() + RESET_TEXT_COLOR + " as " + SET_TEXT_COLOR_BLUE
                        + teamColor + RESET_TEXT_COLOR + " player.";
            } catch (NumberFormatException ex) {
                throw new ResponseException(400, SET_TEXT_COLOR_BLUE + "Expected: join <ID> <WHITE|BLACK>" + RESET_TEXT_COLOR);
            }
        } catch (ResponseException ex) {
            throw new ResponseException(ex.statusCode(), ex.getMessage());
        }
    }

    public String list() {
        try {
            ListGamesResult listOfGames = sf.list(new ListGamesRequest(sf.returnAuth()));

            sf.resetGameMap();

            List<String> numberedGames = new ArrayList<>();

            if (listOfGames != null && !listOfGames.games().isEmpty()) {
                for (var i = 0; i < listOfGames.games().size(); i++) {
                    String info = getGameInfo(listOfGames, i);

                    sf.createGameMap(i + 1, listOfGames.games().get(i));

                    numberedGames.add(info);
                }
            } else {
                return "There are no games to play. Use command " + SET_TEXT_COLOR_BLUE + "'create'" + RESET_TEXT_COLOR + " to make a new game!";
            }

            return String.join("\n", numberedGames);
        } catch (ResponseException ex) {
            throw new ResponseException(ex.statusCode(), ex.getMessage());
        }
    }

    private static String getGameInfo(ListGamesResult listOfGames, int i) {
        var game = listOfGames.games().get(i);

        String gameName = game.gameName();
        String whitePlayer = game.whiteUsername();
        String blackPlayer = game.blackUsername();

        System.out.print(RESET_TEXT_COLOR);

        return String.format("%d. %sname:%s %s, %swhite player:%s %s, %sblack player: %s%s",
                i + 1, SET_TEXT_COLOR_BLUE, RESET_TEXT_COLOR, gameName, SET_TEXT_COLOR_BLUE, RESET_TEXT_COLOR,
                whitePlayer, SET_TEXT_COLOR_BLUE, RESET_TEXT_COLOR, blackPlayer);
    }

    public String observe(String... params) {
        if (params == null || params.length != 1) {
            return "Please enter the " + SET_TEXT_COLOR_BLUE + "gameID" + RESET_TEXT_COLOR + " of the game you would like to observe.";
        }

        try {
            int userInGameID = Integer.parseInt(params[0]);

            list();
            HashMap<Integer, CondensedGameData> game = sf.getGameMap();

            if (!game.containsKey(userInGameID)) {
                return "Game " + SET_TEXT_COLOR_BLUE + userInGameID + RESET_TEXT_COLOR + " not found.";
            }

            CondensedGameData gameToJoin = game.get(userInGameID);
            int serverGameID = gameToJoin.gameID();

            try {
                sf.observe(serverGameID, sf.returnAuth());
                gs.stateInGame();
                return "Now observing game " + SET_TEXT_COLOR_BLUE + userInGameID + RESET_TEXT_COLOR + ".";
            } catch (ResponseException ex) {
                throw new ResponseException(ex.statusCode(), ex.getMessage());
            }
        } catch (NumberFormatException e) {
            throw new ResponseException(400, SET_TEXT_COLOR_BLUE + "Expected: observe <ID>" + RESET_TEXT_COLOR);
        }
    }

    public String quit() {
        state.stateLogOut();
        gs.stateLeaveGame();
        System.out.println(logout());
        return "";
    }

    public String clearDB(String... params) {
        if (params.length != 2 || !params[1].equals("leesie")) {
            return "You are not authorized for this action.";
        }

        sf.clear();
        return "All databases cleared.";
    }


    public String displayHelp(){
        String[] helpCommands = {
                SET_TEXT_COLOR_BLUE + "logout" + RESET_TEXT_COLOR + " -> log out of your account",
                SET_TEXT_COLOR_BLUE + "create <NAME>" + RESET_TEXT_COLOR + " -> create a new game",
                SET_TEXT_COLOR_BLUE + "join <ID> <WHITE|BLACK>" + RESET_TEXT_COLOR + " -> join a game",
                SET_TEXT_COLOR_BLUE + "list" + RESET_TEXT_COLOR + " -> list all games",
                SET_TEXT_COLOR_BLUE + "observe <ID>" + RESET_TEXT_COLOR + " -> watch a game",
                SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " -> exit program",
                SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " -> list possible commands"
        };
        return String.join("\n", helpCommands);
    }
}

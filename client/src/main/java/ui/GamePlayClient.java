package ui;

import chess.*;
import exception.ResponseException;
import websocket.WebSocketFacade;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class GamePlayClient implements ChessClient{

    private final ServerFacade sf;
    private final GameState gs;
    private final WebSocketFacade wsf;


    public GamePlayClient(ServerFacade sf, GameState gs, WebSocketFacade wsf) {
        this.sf = sf;
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
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                case "help" -> displayHelp();
                default -> "Invalid command. Use command " + SET_TEXT_COLOR_BLUE + "'help'" + RESET_TEXT_COLOR + " for a list of possible commands.";
            };

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String redraw() {
        ChessGame currentGame = sf.getGame();
        GameBoardDrawing.drawBoard(sf.getTeamColor(), sf.getGame().getBoard(), null, currentGame);

        return "Board" + SET_TEXT_COLOR_BLUE + " redrawn" + RESET_TEXT_COLOR + ".";
    }

    public String leave() {
        gs.stateLeaveGame();

        UserGameCommand leaveUGC = new UserGameCommand(UserGameCommand.CommandType.LEAVE, sf.returnAuth(), sf.getGameID(), sf.getTeamColor().toString(), sf.getUsername());
        if (wsf != null) {
            try {
                wsf.sendCommand(leaveUGC);
            } catch (Exception exception) {
                System.out.println("Error sending leave command: " + exception.getMessage());
            }
        }

        return SET_TEXT_COLOR_BLUE + "Leaving" + RESET_TEXT_COLOR + " the game.";
    }

    public String move(String... params) {
        return null;

    }

    public String resign() {
        System.out.print("Are you sure? " + SET_TEXT_COLOR_BLUE + "(yes/no)" + RESET_TEXT_COLOR + ": ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim().toLowerCase();

        if (!input.equals("yes")) {
            return SET_TEXT_COLOR_BLUE + "Resignation" + RESET_TEXT_COLOR + " cancelled.";
        }

        String authToken = sf.returnAuth();
        if (authToken != null) {
            UserGameCommand resignUGC = new UserGameCommand(UserGameCommand.CommandType.RESIGN, sf.returnAuth(), sf.getGameID(), sf.getTeamColor().toString(), sf.getUsername());

            if (wsf != null) {
                try {
                    wsf.sendCommand(resignUGC);
                } catch (Exception exception) {
                    System.out.println("Error sending resign command: " + exception.getMessage());
                }
            }
        }

        gs.stateLeaveGame();
        return SET_TEXT_COLOR_BLUE + "You have resigned. " + RESET_TEXT_COLOR + "Returning to game selection.";
    }

    public String highlight(String... params) throws ResponseException {

        if (params.length == 0 || params[0] == null || params[0].length() != 2) {
            return "Invalid position. Example position: " + SET_TEXT_COLOR_BLUE + "a1" + RESET_TEXT_COLOR;
        }

        String highlightString = params[0];

        char columnChar = highlightString.charAt(0);
        int row = Character.getNumericValue(highlightString.charAt(1));

        ChessPosition convertedPosition = convertToChessPosition(columnChar, row, sf.getTeamColor());

        ChessGame currentGame = sf.getGame();
        ChessBoard currentBoard = currentGame.getBoard();

        GameBoardDrawing.drawBoard(sf.getTeamColor(), currentBoard, convertedPosition, currentGame);

//        System.out.println(convertedPosition);

        return "";
    }

    private ChessPosition convertToChessPosition(char columnChar, int row, ChessGame.TeamColor pov) throws ResponseException {

        columnChar = Character.toLowerCase(columnChar);
        int col = columnChar - 'a' + 1;

        if (col < 1 || col > 8) {
            throw new ResponseException(401, "Invalid position. Example position: " + SET_TEXT_COLOR_BLUE + "a1" + RESET_TEXT_COLOR);
        }

        if (row < 1 || row > 8) {
            String error =  "Invalid row number. Example position: " + SET_TEXT_COLOR_BLUE + "a1" + RESET_TEXT_COLOR;
            throw new ResponseException(401, error);
        }


//        if (pov == ChessGame.TeamColor.BLACK) {
//            col = 9 - col;
//            row = 9 - row;
//        }

        return new ChessPosition(row, col);
    }


    public String displayHelp(){
        String[] helpCommands = {
                SET_TEXT_COLOR_BLUE + "redraw" + RESET_TEXT_COLOR + " -> redraw the chess board",
                SET_TEXT_COLOR_BLUE + "leave" + RESET_TEXT_COLOR + " -> leave the game",
                SET_TEXT_COLOR_BLUE + "move <old position> <new position>" + RESET_TEXT_COLOR + " -> move a piece",
                SET_TEXT_COLOR_BLUE + "resign" + RESET_TEXT_COLOR + " -> forfeit the game",
                SET_TEXT_COLOR_BLUE + "highlight <piece position>" + RESET_TEXT_COLOR + " -> highlight all possible moves",
                SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " -> list possible commands"
        };
        return String.join("\n", helpCommands);
    }

}

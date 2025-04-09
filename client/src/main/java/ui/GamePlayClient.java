package ui;

import chess.*;
import exception.ResponseException;

import java.util.Arrays;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class GamePlayClient implements ChessClient{

    private final ServerFacade sf;
    private final GameState gs;


    public GamePlayClient(ServerFacade sf, GameState gs) {
        this.sf = sf;
        this.gs = gs;
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
        return SET_TEXT_COLOR_BLUE + "Redrawing" + RESET_TEXT_COLOR + " the board.";
    }

    public String leave() {
        gs.stateLeaveGame();
        return SET_TEXT_COLOR_BLUE + "Leaving" + RESET_TEXT_COLOR + " the game.";
    }

    public String move(String... params) {
        return null;

    }

    public String resign() {
        return null;

    }

    public String highlight(String... params) {
        return null;

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

import chess.ChessGame;
import ui.GameBoardDrawing;
import ui.Repl;

public class Main {

    public static void main(String[] args) {
        var serverURL = "http://localhost:8080";

//        if (args.length == 1) {
//            serverURL = args[0];
//        }
//
//        new Repl(serverURL).run();
        ChessGame newGame = new ChessGame();
        GameBoardDrawing.drawBoard(ChessGame.TeamColor.WHITE, newGame.getBoard());
        GameBoardDrawing.drawBoard(ChessGame.TeamColor.BLACK, newGame.getBoard());

    }
}
import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import server.Server;
import spark.Spark;

public class Main {
    public static void main(String[] args) throws DataAccessException {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
        new Server().run(8080);

    }
}
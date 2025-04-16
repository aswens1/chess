package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.*;
import model.GameDataRecord;
import gamerecords.*;

public class GameService {

    final SQLUserDataAccess sqlUser;
    final SQLAuthDataAccess sqlAuth;
    final SQLGameDataAccess sqlGame;

    public GameService(SQLUserDataAccess sqlUser, SQLAuthDataAccess sqlAuth, SQLGameDataAccess sqlGame) {
        this.sqlUser = sqlUser;
        this.sqlAuth = sqlAuth;
        this.sqlGame = sqlGame;
    }

    public ListGamesResult listGames() {
        if (sqlUser == null || sqlAuth == null || sqlGame == null) {
            throw new NullPointerException("Cannot pass in null DAO");
        }

        return new ListGamesResult(sqlGame.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        if (sqlUser == null || sqlAuth == null || sqlGame == null) {
            throw new NullPointerException("Cannot pass in null DAO");
        }

        return new CreateGameResult(sqlGame.createGame(createGameRequest.gameName()));
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest, String username) {
        ChessGame game = sqlGame.joinGame(joinGameRequest.playerColor(), joinGameRequest.gameID(), username);
        return new JoinGameResult(game);
    }

    public GetGameResult getGame(int gameID) {
        GameDataRecord gameData = sqlGame.getGame(gameID);
        return new GetGameResult(gameData);
    }

    public MoveResult makeMove(MoveRequest moveRequest) throws InvalidMoveException {
        GameDataRecord gameData = sqlGame.getGame(moveRequest.gameID());
        ChessGame game = gameData.game();

//        System.out.println("original board: " + game.getBoard().toString());

        ChessMove move = moveRequest.move();
        String username = moveRequest.username();
        ChessGame.TeamColor pov = moveRequest.pov();

//        System.out.println("move: " + move);

        game.makeMove(move);

//        System.out.println("move made?" + game.getBoard().toString());

        sqlGame.updateGameState(moveRequest.gameID(), username, pov, game);

        GameDataRecord updatedGame = sqlGame.getGame(moveRequest.gameID());

//        System.out.println("updated board: " + updatedGame.game().getBoard().toString());

        return new MoveResult(updatedGame.game());
    }
}

package service;

import chess.ChessGame;
import dataaccess.*;
import records.*;

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
}

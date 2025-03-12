package service;

import dataaccess.*;
import service.records.*;

public class GameService {

    final SQLUserDataAccess sqlUser;
    final SQLAuthDataAccess sqlAuth;
    final GameDAO gameDAO;

    public GameService(SQLUserDataAccess sqlUser, SQLAuthDataAccess sqlAuth, GameDAO gameDAO) {
        this.sqlUser = sqlUser;
        this.gameDAO = gameDAO;
        this.sqlAuth = sqlAuth;
    }

    public ListGamesResult listGames() {
        if (sqlUser == null || sqlAuth == null || gameDAO == null) {
            throw new NullPointerException("Cannot pass in null DAO");
        }

        return new ListGamesResult(gameDAO.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        if (sqlUser == null || sqlAuth == null || gameDAO == null) {
            throw new NullPointerException("Cannot pass in null DAO");
        }

        return new CreateGameResult(gameDAO.createGame(createGameRequest.gameName()));
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest, String username) {
        gameDAO.joinGame(joinGameRequest.playerColor(), joinGameRequest.gameID(), username);
        return new JoinGameResult();
    }
}

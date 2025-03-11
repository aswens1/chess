package service;

import dataaccess.AuthDataDAO;
import dataaccess.GameDAO;
import dataaccess.SQLAuthDataAccess;
import dataaccess.UserDAO;
import service.records.*;

public class GameService {

    final SQLAuthDataAccess sqlAuth;
    final UserDAO userDAO;
    final GameDAO gameDAO;

    public GameService(UserDAO userDAO, SQLAuthDataAccess sqlAuth, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.sqlAuth = sqlAuth;
    }

    public ListGamesResult listGames() {
        if (userDAO == null || sqlAuth == null || gameDAO == null) {
            throw new NullPointerException("Cannot pass in null DAO");
        }

        return new ListGamesResult(gameDAO.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        if (userDAO == null || sqlAuth == null || gameDAO == null) {
            throw new NullPointerException("Cannot pass in null DAO");
        }

        return new CreateGameResult(gameDAO.createGame(createGameRequest.gameName()));
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest, String username) {
        gameDAO.joinGame(joinGameRequest.playerColor(), joinGameRequest.gameID(), username);
        return new JoinGameResult();
    }
}

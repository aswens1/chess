package service;

import dataaccess.AuthDataDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.records.*;

public class GameService {

    final UserDAO userDAO;
    final AuthDataDAO authDataDAO;
    final GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDataDAO authDataDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDataDAO = authDataDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames() {
        if (userDAO == null || authDataDAO == null || gameDAO == null) {
            throw new NullPointerException("Cannot pass in null DAO");
        }

        return new ListGamesResult(gameDAO.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        if (userDAO == null || authDataDAO == null || gameDAO == null) {
            throw new NullPointerException("Cannot pass in null DAO");
        }

        return new CreateGameResult(gameDAO.createGame(createGameRequest.gameName()));
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest, String username) {
        gameDAO.joinGame(joinGameRequest.playerColor(), joinGameRequest.gameID(), username);
        return new JoinGameResult();
    }
}

package service;

import dataaccess.AuthDataDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthDataRecord;

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
        return new ListGamesResult(gameDAO.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        return new CreateGameResult(gameDAO.createGame(createGameRequest.gameName()));
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) {
        gameDAO.joinGame(joinGameRequest.playerColour(), joinGameRequest.gameID());
        return new JoinGameResult();
    }
}

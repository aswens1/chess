package service;

import dataaccess.AuthDataDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class GameService {

    private final UserDAO userDAO;
    private final AuthDataDAO authDataDAO;
    private final GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDataDAO authDataDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDataDAO = authDataDAO;
        this.gameDAO = gameDAO;
    }

}

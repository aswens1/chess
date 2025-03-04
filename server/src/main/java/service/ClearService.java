package service;

import dataaccess.AuthDataDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.records.ClearResult;

public class ClearService {
    final UserDAO userDAO;
    final AuthDataDAO authDataDAO;
    final GameDAO gameDAO;

    public ClearService(UserDAO userDAO, AuthDataDAO authDataDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDataDAO = authDataDAO;
        this.gameDAO = gameDAO;
    }

    public ClearResult clearAllDatabases() {
        userDAO.clear();
        authDataDAO.clear();
        gameDAO.clear();

        return new ClearResult();
    }
}

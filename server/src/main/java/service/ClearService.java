package service;

import dataaccess.AuthDataDAO;
import dataaccess.GameDAO;
import dataaccess.SQLAuthDataAccess;
import dataaccess.UserDAO;
import service.records.ClearResult;

public class ClearService {
    final UserDAO userDAO;
    final SQLAuthDataAccess sqlAuth;
    final GameDAO gameDAO;

    public ClearService(UserDAO userDAO, SQLAuthDataAccess sqlAuth, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.sqlAuth = sqlAuth;
        this.gameDAO = gameDAO;
    }

    public ClearResult clearAllDatabases() {
        userDAO.clear();
        sqlAuth.clear();
        gameDAO.clear();

        return new ClearResult();
    }
}

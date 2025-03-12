package service;

import dataaccess.*;
import service.records.ClearResult;

public class ClearService {
    final SQLAuthDataAccess sqlAuth;
    final GameDAO gameDAO;
    final SQLUserDataAccess sqlUser;

    public ClearService(SQLUserDataAccess sqlUser, SQLAuthDataAccess sqlAuth, GameDAO gameDAO) {
        this.sqlUser = sqlUser;
        this.sqlAuth = sqlAuth;
        this.gameDAO = gameDAO;
    }

    public ClearResult clearAllDatabases() {
        sqlUser.clear();
        sqlAuth.clear();
        gameDAO.clear();

        return new ClearResult();
    }
}

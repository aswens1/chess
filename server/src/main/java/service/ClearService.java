package service;

import dataaccess.*;
import records.ClearResult;

public class ClearService {
    final SQLAuthDataAccess sqlAuth;
    final SQLUserDataAccess sqlUser;
    final SQLGameDataAccess sqlGame;

    public ClearService(SQLUserDataAccess sqlUser, SQLAuthDataAccess sqlAuth, SQLGameDataAccess sqlGame) {
        this.sqlUser = sqlUser;
        this.sqlAuth = sqlAuth;
        this.sqlGame = sqlGame;
    }

    public ClearResult clearAllDatabases() {
        sqlUser.clear();
        sqlAuth.clear();
        sqlGame.clear();

        return new ClearResult();
    }
}

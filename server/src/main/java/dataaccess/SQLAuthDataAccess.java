package dataaccess;

import model.AuthDataRecord;
import model.UserRecord;

public class SQLAuthDataAccess implements AuthDataDAOInterface {
    @Override
    public AuthDataRecord createAuthData(UserRecord user) {
        return null;
    }

    @Override
    public AuthDataRecord getAuthData(String authToken) {
        return null;
    }

    @Override
    public boolean deleteAuthData(String authToken) {
        return false;
    }

    @Override
    public void clear() {

    }
}

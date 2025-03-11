package handler;
import dataaccess.SQLAuthDataAccess;

public class AuthTokenValidationHandler {

    private final SQLAuthDataAccess sqlAuth;

    public AuthTokenValidationHandler(SQLAuthDataAccess sqlAuth) {
        this.sqlAuth = sqlAuth;
    }

    public boolean isValidToken(String authToken) {
        return sqlAuth.getAuthData(authToken) != null;
    }
}

package handler;
import dataaccess.AuthDataDAO;

public class AuthTokenValidationHandler {

    AuthDataDAO authDataDAO;

    public boolean isValidToken(String authToken) {
        return authDataDAO.getAuthData(authToken) != null;
    }
}

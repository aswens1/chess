package handler;
import dataaccess.AuthDataDAO;

public class AuthTokenValidationHandler {

    AuthDataDAO authDataDAO;

    boolean isValidToken(String authToken) {
        if (authDataDAO.getAuthData(authToken) == null) {
            return false;
        }
        return true;
    }
}

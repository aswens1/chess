package handler;
import dataaccess.AuthDataDAO;

public class AuthTokenValidationHandler {

    private final AuthDataDAO authDataDAO;

    public AuthTokenValidationHandler(AuthDataDAO authDataDAO) {
        this.authDataDAO = authDataDAO;
    }

    public boolean isValidToken(String authToken) {
        return authDataDAO.getAuthData(authToken) != null;
    }
}

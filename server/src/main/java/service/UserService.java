package service;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthDataRecord;
import model.UserRecord;
import dataaccess.AuthDataDAO;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDataDAO authDataDAO;

    public UserService(UserDAO userDAO, AuthDataDAO authDataDAO) {
        this.userDAO = userDAO;
        this.authDataDAO = authDataDAO;
    }

    public RegisterResult register(RegisterRequest registerRequest) {

        if (userDAO.getUser(registerRequest.username()) != null) {
            throw new ResponseException(403, "Error: already taken");
        }

        UserRecord newUser = new UserRecord(registerRequest.username(),
                registerRequest.password(), registerRequest.email());
        userDAO.registerUser(newUser);

        AuthDataRecord authData = authDataDAO.createAuthData(newUser);
        String authToken = authData.authToken();

        return new RegisterResult(registerRequest.username(), authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        try {
            UserRecord user = userDAO.getUser(loginRequest.username());
            if (user == null || !user.password().equals(loginRequest.password())) {
                throw new ResponseException(401, "Error: bad request");
            }

            AuthDataRecord authData = authDataDAO.createAuthData(user);
            String authToken = authData.authToken();

            return new LoginResult(user.username(), authToken);

        } catch (Exception exception){
            throw new DataAccessException(exception.getMessage());
        }
    }

    public LogoutResult logout(String authToken) {
        authDataDAO.deleteAuthData(authToken);
        return new LogoutResult();
    }

}

package service;

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

        if (registerRequest == null ||
            registerRequest.username() == null || registerRequest.username().isBlank() ||
            registerRequest.password() == null || registerRequest.password().isBlank() ||
            registerRequest.email() == null || registerRequest.email().isBlank()) {

            throw new ResponseException(400, "Error: bad request");
        }



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

    public LoginResult login(LoginRequest loginRequest) {
            UserRecord user = userDAO.getUser(loginRequest.username());

            if (user == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            if (!user.password().equals(loginRequest.password())) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            AuthDataRecord authData = authDataDAO.createAuthData(user);
            String authToken = authData.authToken();

            return new LoginResult(user.username(), authToken);
    }

    public LogoutResult logout(String authToken) {
        boolean wasAuthTokenRemoved = authDataDAO.deleteAuthData(authToken);

        if (!wasAuthTokenRemoved) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        return new LogoutResult();
    }

}

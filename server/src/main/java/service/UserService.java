package service;

import dataaccess.SQLAuthDataAccess;
import dataaccess.SQLUserDataAccess;
import exception.ResponseException;
import model.AuthDataRecord;
import model.UserRecord;
import org.mindrot.jbcrypt.BCrypt;
import records.*;

public class UserService {

    private final SQLUserDataAccess sqlUser;
    private final SQLAuthDataAccess sqlAuth;

    public UserService(SQLUserDataAccess sqlUser, SQLAuthDataAccess sqlAuth) {
        this.sqlAuth = sqlAuth;
        this.sqlUser = sqlUser;
    }

    public RegisterResult register(RegisterRequest registerRequest) {

        if (registerRequest == null ||
            registerRequest.username() == null || registerRequest.username().isBlank() ||
            registerRequest.password() == null || registerRequest.password().isBlank() ||
            registerRequest.email() == null || registerRequest.email().isBlank()) {

            throw new ResponseException(400, "Error: bad request");
        }

        if (sqlUser.getUser(registerRequest.username()) != null) {
            throw new ResponseException(403, "Error: already taken");
        }

        UserRecord newUser = new UserRecord(registerRequest.username(),
                registerRequest.password(), registerRequest.email());

        sqlUser.registerUser(newUser);

        AuthDataRecord authData = sqlAuth.createAuthData(newUser);

        String authToken = authData.authToken();

        return new RegisterResult(registerRequest.username(), authToken);
    }

    public LoginResult login(LoginRequest loginRequest) {
            UserRecord user = sqlUser.getUser(loginRequest.username());

            if (user == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            if (!BCrypt.checkpw(loginRequest.password(), user.password())) {
                throw new ResponseException(401, "Error: unauthorized");
            }

        AuthDataRecord authData = sqlAuth.createAuthData(user);
        String authToken = authData.authToken();

        return new LoginResult(user.username(), authToken);
    }

    public LogoutResult logout(String authToken) {

        boolean wasAuthTokenRemoved = sqlAuth.deleteAuthData(authToken);

        if (!wasAuthTokenRemoved) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        return new LogoutResult();
    }
}

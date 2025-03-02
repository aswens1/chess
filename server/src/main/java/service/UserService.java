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

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException{

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

//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}

}

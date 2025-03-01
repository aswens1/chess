package service;
import dataaccess.UserDAO;
import model.AuthDataRecord;
import model.UserRecord;
import dataaccess.AuthDataDAO;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDataDAO authDataDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.authDataDAO = new AuthDataDAO();
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        if (userDAO.doesUserExist(registerRequest.username())) {
            return new RegisterResult(false, "User already exists", null);
        }

        UserRecord newUser = new UserRecord(registerRequest.username(),
                registerRequest.password(), registerRequest.email());
        userDAO.registerUser(newUser);

        AuthDataRecord authData = authDataDAO.createAuthData(newUser);
        String authToken = authData.authToken();

        return new RegisterResult(true, "Registration successful", authToken);

    }

//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}

}

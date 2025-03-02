package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthDataRecord;
import service.*;
import spark.*;

public class UserHandler {
    UserService userService;
    AuthDataRecord authDataRecord;
    AuthTokenValidationHandler validAuthToken;


    public UserHandler(UserService userService, AuthTokenValidationHandler validAuthToken) {
        this.userService = userService;
        this.validAuthToken = validAuthToken;
    }

    public Object register(Request request, Response response) {
        try {
            RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);

            if (registerRequest.username() == null || registerRequest.username().isBlank() ||
                registerRequest.password() == null || registerRequest.password().isBlank() ||
                registerRequest.email() == null || registerRequest.email().isBlank()) {
                throw new ResponseException(400, "Error: bad request");
            }


            RegisterResult registerResult = userService.register(registerRequest);

            response.status(200);
            return new Gson().toJson(registerResult);

        } catch (ResponseException ex) {
            response.status(ex.getStatusCode());
            return new Gson().toJson(ex.getMessage());
        } catch (Exception ex) {
            response.status(500);
            return new Gson().toJson(ex.getMessage());
        }
    }

    public Object login(Request request, Response response) {
        try {
            LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
            LoginResult loginResult = userService.login(loginRequest);
            response.status(200);
            return new Gson().toJson(loginResult);

        } catch (DataAccessException ex) {
            response.status(500);
            return new Gson().toJson(ex.getMessage());
        }
    }

    public Object logout(Request request, Response response) {
        String authToken = request.headers("Authorization");
        System.out.println(authToken);

        if (validAuthToken.isValidToken(authToken)) {
            LogoutResult logoutResult = userService.logout(authToken);
            response.status(200);
            return new Gson().toJson(logoutResult);
        }
        return new Gson().toJson((new ResponseException(401, "Error: unauthorized")));
    }
}
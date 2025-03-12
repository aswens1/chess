package handler;

import com.google.gson.Gson;
import exception.ErrorMessage;
import exception.ResponseException;
import model.AuthDataRecord;
import service.*;
import service.records.*;
import spark.*;

public class UserHandler {
    UserService userService;
    AuthTokenValidationHandler validAuthToken;


    public UserHandler(UserService userService, AuthTokenValidationHandler validAuthToken) {
        this.userService = userService;
        this.validAuthToken = validAuthToken;
    }

    public Object register(Request request, Response response) {
        RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);
        try {
            RegisterResult registerResult = userService.register(registerRequest);
            response.status(200);
            return new Gson().toJson(registerResult);
        } catch (ResponseException ex) {
            response.status(ex.statusCode());
            return new Gson().toJson(new ErrorMessage(ex.statusCode(), ex.getMessage()));
        } catch (Exception ex) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(500, "Error " + ex.getMessage()));
        }
    }

    public Object login(Request request, Response response) {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        try {
            LoginResult loginResult = userService.login(loginRequest);
            response.status(200);
            return new Gson().toJson(loginResult);
        } catch (ResponseException ex) {
            response.status(ex.statusCode());
            return new Gson().toJson(new ErrorMessage(ex.statusCode(), ex.getMessage()));
        } catch (Exception ex) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(500, "Error " + ex.getMessage()));
        }

    }

    public Object logout(Request request, Response response) {
        String authToken = request.headers("Authorization");

        try {
            LogoutResult logoutResult = userService.logout(authToken);
            response.status(200);
            return new Gson().toJson(logoutResult);
        } catch (ResponseException ex) {
            response.status(ex.statusCode());
            return new Gson().toJson(new ErrorMessage(ex.statusCode(), ex.getMessage()));
        } catch (Exception ex) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(500, "Error " + ex.getMessage()));
        }
    }
}
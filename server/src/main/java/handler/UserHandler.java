package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import service.*;
import spark.*;

import java.util.Map;

public class UserHandler {
    UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
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
            response.type("application/json");

            return new Gson().toJson(registerResult);

        } catch (ResponseException ex) {
            response.status(ex.getStatusCode());
            response.type("application/json");
            return new Gson().toJson(Map.of("message", "Error: " + ex.getMessage()));
        } catch (Exception ex) {
            response.status(500);
            return new Gson().toJson(Map.of("message", "Error: " + ex.getMessage()));
        }
    }
}
package handler;

import com.google.gson.Gson;
import exception.ResponseException;
import service.GameService;
import service.ListGamesResult;
import spark.Request;
import spark.Response;

public class GameHandler {
    GameService gameService;
    AuthTokenValidationHandler validAuthToken;

    public GameHandler(GameService gameService, AuthTokenValidationHandler validAuthToken) {
        this.gameService = gameService;
        this.validAuthToken = validAuthToken;
    }

    public Object listGames(Request request, Response response) {
        String authToken = request.headers("Authorization");

        if (validAuthToken.isValidToken(authToken)) {
            ListGamesResult listGamesResult = gameService.listGames();
            response.status(200);
            return new Gson().toJson(listGamesResult);
        } else {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

}

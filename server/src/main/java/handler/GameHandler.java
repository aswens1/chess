package handler;

import com.google.gson.Gson;
import exception.ResponseException;
import service.*;
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
        }

        return new Gson().toJson((new ResponseException(401, "Error: unauthorized")));

    }

    public Object createGame(Request request, Response response) {
        String authToken = request.headers("Authorization");

        if (validAuthToken.isValidToken(authToken)) {
            CreateGameRequest createGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            response.status(200);
            return new Gson().toJson(createGameResult);
        }
        return new Gson().toJson((new ResponseException(401, "Error: unauthorized")));
    }

    public Object joinGame(Request request, Response response) {
        String authToken = request.headers("Authorization");
        if (validAuthToken.isValidToken(authToken)) {
            JoinGameRequest joinGameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
            JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
            response.status(200);
            return new Gson().toJson(joinGameResult);
        }
        return new Gson().toJson((new ResponseException(401, "Error: unauthorized")));
    }

}

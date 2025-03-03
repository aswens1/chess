package handler;

import com.google.gson.Gson;
import dataaccess.AuthDataDAO;
import model.AuthDataRecord;
import exception.ErrorMessage;
import exception.ResponseException;
import model.UserRecord;
import service.*;
import spark.Request;
import spark.Response;

public class GameHandler {
    private final GameService gameService;
    private final AuthTokenValidationHandler validAuthToken;

    public GameHandler(GameService gameService, AuthTokenValidationHandler validAuthToken, AuthDataDAO authDataDAO) {
        this.gameService = gameService;
        this.validAuthToken = validAuthToken;
    }

    public Object listGames(Request request, Response response) {
        String authToken = request.headers("Authorization");
        try {
            if (validAuthToken.isValidToken(authToken)) {
                ListGamesResult listGamesResult = gameService.listGames();
                response.status(200);
                return new Gson().toJson(listGamesResult);
            }
            response.status(401);
            return new Gson().toJson((new ErrorMessage(401, "Error: unauthorized")));
        } catch (ResponseException ex) {
            response.status(ex.statusCode());
            return new Gson().toJson(new ErrorMessage(ex.statusCode(), ex.getMessage()));
        } catch (Exception ex) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(500, "Error " + ex.getMessage()));
        }
    }

    public Object createGame(Request request, Response response) {
        String authToken = request.headers("Authorization");

        try {
            if (validAuthToken.isValidToken(authToken)) {
                CreateGameRequest createGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);
                CreateGameResult createGameResult = gameService.createGame(createGameRequest);
                response.status(200);
                return new Gson().toJson(createGameResult);
            }
            response.status(401);
            return new Gson().toJson((new ErrorMessage(401, "Error: unauthorized")));
        } catch (ResponseException ex) {
            response.status(ex.statusCode());
            return new Gson().toJson(new ErrorMessage(ex.statusCode(), ex.getMessage()));
        } catch (Exception ex) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(500, "Error " + ex.getMessage()));
        }
    }

    public Object joinGame(Request request, Response response) {
        String authToken = request.headers("Authorization");
        try {
            if (validAuthToken.isValidToken(authToken)) {
                JoinGameRequest joinGameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
                JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
                response.status(200);
                return new Gson().toJson(joinGameResult);
            }
            response.status(401);
            return new Gson().toJson((new ErrorMessage(401, "Error: unauthorized")));
        } catch (ResponseException ex) {
            response.status(ex.statusCode());
            return new Gson().toJson(new ErrorMessage(ex.statusCode(), ex.getMessage()));
        } catch (Exception ex) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(500, "Error " + ex.getMessage()));
        }
    }
}

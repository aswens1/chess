package handler;

import com.google.gson.Gson;
import dataaccess.SQLAuthDataAccess;
import model.AuthDataRecord;
import exception.ErrorMessage;
import exception.ResponseException;
import gameRecords.*;
import service.*;
import spark.Request;
import spark.Response;

public class GameHandler {
    private final GameService gameService;
    private final AuthTokenValidationHandler validAuthToken;
    private final SQLAuthDataAccess sqlAuth;


    public GameHandler(GameService gameService, AuthTokenValidationHandler validAuthToken, SQLAuthDataAccess sqlAuth) {
        this.gameService = gameService;
        this.validAuthToken = validAuthToken;
        this.sqlAuth = sqlAuth;
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

                AuthDataRecord authDataRecord = sqlAuth.getAuthData(authToken);
                if (authDataRecord == null) {
                    response.status(401);
                    return new Gson().toJson((new ErrorMessage(401, "Error: unauthorized")));
                }

                JoinGameRequest joinGameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);

                JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest, authDataRecord.username());
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

    public Object getGame(Request request, Response response) {
        String authToken = request.headers("Authorization");
        try {
            if (validAuthToken.isValidToken(authToken)) {

                int gameID = Integer.parseInt(request.params("id"));

//                GetGameRequest getGameRequest = new Gson().fromJson(request.body(), GetGameRequest.class);
                GetGameResult getGameResult = gameService.getGame(gameID);

                response.status(200);
                return new Gson().toJson(getGameResult);
            }


            response.status(401);
            return new Gson().toJson((new ErrorMessage(401, "Error: unauthorized")));
        } catch (ResponseException ex) {
            response.status(ex.statusCode());
            return new Gson().toJson(new ErrorMessage(ex.statusCode(), ex.getMessage()));
        } catch (Exception ex) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(500, "Error: " + ex.getMessage()));
        }
    }

    public Object makeMove(Request request, Response response) {
        String authToken = request.headers("Authorization");

        try {
            if (validAuthToken.isValidToken(authToken)) {
                MoveRequest moveRequest = new Gson().fromJson(request.body(), MoveRequest.class);
                MoveResult moveResult = gameService.makeMove(moveRequest);

                response.status(200);
                return new Gson().toJson(moveResult);
            }

            response.status(401);
            return new Gson().toJson((new ErrorMessage(401, "Error: unauthorized")));
        } catch (ResponseException ex) {
            response.status(ex.statusCode());
            return new Gson().toJson(new ErrorMessage(ex.statusCode(), ex.getMessage()));
        } catch (Exception ex) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(500, "Error: " + ex.getMessage()));
        }
    }
}

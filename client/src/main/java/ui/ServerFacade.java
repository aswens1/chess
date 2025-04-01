package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import records.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;

public class ServerFacade {

    private String authToken;
    private String username;

    private ChessGame chessGame;
    private ChessGame.TeamColor teamColor;

    private final String serverURL;
//    private final int serverPort;

    private final HashMap<Integer, CondensedGameData> gameMap = new HashMap<>();


    public ServerFacade(String url) {
        serverURL = url;
    }

    public void setAuth(String authToken) {
        this.authToken = authToken;
    }

    public void setGame(ChessGame chessGame) {this.chessGame = chessGame;}

    public void setTeamColor(ChessGame.TeamColor teamColor) { this.teamColor = teamColor; }

    public ChessGame getGame() { return chessGame; }

    public ChessGame.TeamColor getTeamColor() { return teamColor; }

    public String returnAuth() { return authToken; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void createGameMap(int i, CondensedGameData gameData) {
        gameMap.put(i, gameData);
    }

    public HashMap<Integer, CondensedGameData> getGameMap() {
        return gameMap;
    }

    public void resetGameMap() {
        gameMap.clear();
    }

    public RegisterResult registerUser(RegisterRequest registerRequest) {
        var path = "/user";
        RegisterResult registerResult = this.makeRequest("POST", path, registerRequest, null, RegisterResult.class);
        setAuth(registerResult.authToken());
        setUsername(registerResult.username());
        return registerResult;
    }

    public LoginResult loginUser(LoginRequest loginRequest) {
        var path = "/session";
        LoginResult loginResult = this.makeRequest("POST", path, loginRequest, null, LoginResult.class);
        setAuth(loginResult.authToken());
        setUsername(loginResult.username());
        return loginResult;
    }

    public void logoutUser(LogoutRequest logoutRequest) {
        var path = "/session";
        this.makeRequest("DELETE", path, logoutRequest, logoutRequest.authToken(), LogoutResult.class);
    }

    public ListGamesResult list(ListGamesRequest listGamesRequest) {
        var path = "/game";
        return this.makeRequest("GET", path, null, listGamesRequest.authToken(), ListGamesResult.class);
    }

    public void create(CreateGameRequest createGameRequest, String authToken) {
        var path = "/game";
        this.makeRequest("POST", path, createGameRequest, authToken, CreateGameResult.class);
    }

    public void join(JoinGameRequest joinGameRequest, String authToken) {
        var path = "/game";
        this.makeRequest("PUT", path, joinGameRequest , authToken, JoinGameResult.class);
    }

    public void observe(int gameID, String authToken) {
        if (authToken == null) {
            throw new ResponseException(400, "Error: unauthorized");
        }

        ChessGame game = new ChessGame();
        GameBoardDrawing.drawBoard(ChessGame.TeamColor.WHITE, game.getBoard());
    }

    public void clear() {
        var path = "/db";
        ClearRequest clearRequest = new ClearRequest();
        this.makeRequest("DELETE", path, clearRequest, null, ClearResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, String requestHeader, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (requestHeader != null) {
                http.setRequestProperty("Authorization", requestHeader);
            }

            writeBody(request, http);
            http.connect();
            failedTry(http);
            return readBody(http, responseClass);

        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException{
        T response = null;

        try (InputStream responseBody = http.getInputStream()) {
            if (responseBody.available() > 0) {
                InputStreamReader inputReader = new InputStreamReader(responseBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(inputReader, responseClass);
                }
            }
        }
        return response;
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String requestData = new Gson().toJson(request);
            try (OutputStream requestBody = http.getOutputStream()) {
                requestBody.write(requestData.getBytes());
            }
        }
    }

    private void failedTry(HttpURLConnection http) throws IOException, ResponseException{
        var status = http.getResponseCode();
        if (!code200(status)) {
            try (InputStream responseError = http.getErrorStream()) {
                if (responseError != null) {

                    String rawError = new String(responseError.readAllBytes());
                    ResponseException error = ResponseException.fromJson(rawError);

                    if (error.statusCode() == 401) {
                        throw new ResponseException(401, EscapeSequences.SET_TEXT_COLOR_BLUE + "Unauthorised." +
                                EscapeSequences.RESET_TEXT_COLOR + " Please check your " +
                                EscapeSequences.SET_TEXT_COLOR_BLUE + "username" + EscapeSequences.RESET_TEXT_COLOR +
                                " and " + EscapeSequences.SET_TEXT_COLOR_BLUE + "password" +
                                EscapeSequences.RESET_TEXT_COLOR + " and try again.");
                    }
                    throw error;
                }
            }
            throw new ResponseException(status, "Error: " + status);
        }
    }

    private boolean code200(int status) {
        return status / 100 == 2;
    }
}

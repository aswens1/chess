package handler;

import service.GameService;

public class GameHandler {
    GameService gameService;
    AuthTokenValidationHandler validAuthToken;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }


}

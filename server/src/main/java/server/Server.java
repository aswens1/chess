package server;
import dataaccess.*;
import exception.ResponseException;
import handler.*;
import service.*;
import spark.*;

public class Server {
    static UserService userService;
    private final UserHandler userHandler;
    static GameService gameService;
    private final GameHandler gameHandler;
    static ClearService clearService;
    private final ClearHandler clearHandler;

    public Server() throws DataAccessException {
        GameDAO gameDAO = new GameDAO();

        SQLAuthDataAccess sqlAuthDataAccess = new SQLAuthDataAccess();
        SQLUserDataAccess sqlUserDataAccess = new SQLUserDataAccess();
        SQLGameDataAccess sqlGameDataAccess = new SQLGameDataAccess();


        userService = new UserService(sqlUserDataAccess, sqlAuthDataAccess);
        gameService = new GameService(sqlUserDataAccess, sqlAuthDataAccess, gameDAO);
        clearService = new ClearService(sqlUserDataAccess, sqlAuthDataAccess, gameDAO);


        AuthTokenValidationHandler validAuthToken = new AuthTokenValidationHandler(sqlAuthDataAccess);
        userHandler = new UserHandler(userService, validAuthToken);
        gameHandler = new GameHandler(gameService, validAuthToken, sqlAuthDataAccess);
        clearHandler = new ClearHandler(clearService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", userHandler::register);

        Spark.post("/session", userHandler::login);

        Spark.delete("/session", userHandler::logout);

        Spark.get("/game", gameHandler::listGames);

        Spark.post("/game", gameHandler::createGame);

        Spark.put("/game", gameHandler::joinGame);

        Spark.delete("/db", clearHandler::clearAllDatabases);



        Spark.exception(ResponseException.class, this::responseExHandler);

        Spark.awaitInitialization();
        System.out.println("Server is running on port " +  desiredPort);
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void responseExHandler(ResponseException exception, Request request, Response response) {
        response.status(exception.statusCode());
        response.type("application/json");
        response.body(exception.getMessage());
    }
}

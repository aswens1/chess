package server;
import dataaccess.*;
import exception.ResponseException;
import handler.*;
import model.UserRecord;
import service.*;
import spark.*;

public class Server {
    static UserService userService;
    private final UserHandler userHandler;
    static GameService gameService;
    private final GameHandler gameHandler;
    static ClearService clearService;
    private final ClearHandler clearHandler;

    public Server() {
        AuthDataDAO authDataDAO = new AuthDataDAO();
        UserDAO userDAO = new UserDAO();
        GameDAO gameDAO = new GameDAO();

        userService = new UserService(userDAO, authDataDAO);
        gameService = new GameService(userDAO, authDataDAO, gameDAO);
        clearService = new ClearService(userDAO, authDataDAO, gameDAO);


        AuthTokenValidationHandler validAuthToken = new AuthTokenValidationHandler(authDataDAO);
        userHandler = new UserHandler(userService, validAuthToken);
        gameHandler = new GameHandler(gameService, validAuthToken, authDataDAO);
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

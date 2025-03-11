package service;
import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import service.records.JoinGameRequest;
import service.records.LoginRequest;
import service.records.LoginResult;
import service.records.RegisterRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    static UserService userService;
    static GameService gameService;
    static ClearService clearService;


    @Order(1)
    @Test
    void allDatabasesShouldBeClear() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");
        testUserDao.registerUser(testUser);

        GameDAO testGameDao = new GameDAO();
        testGameDao.createGame("testGame");

        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        AuthDataRecord testAuth = testSQLAuth.createAuthData(testUser);

        clearService = new ClearService(testUserDao, testSQLAuth, testGameDao);

        clearService.clearAllDatabases();

        assertTrue(testGameDao.listGames().isEmpty());
        assertFalse(testUserDao.doesUserExist(testUser.username()));
        assertNull(testSQLAuth.getAuthData(testAuth.authToken()));
    }


    @Order(2)
    @Test
    void registerUserTestPositive() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");

        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();

        RegisterRequest registerRequest = new RegisterRequest("testUser", "testUserPassword", "testUserEmail");

        userService = new UserService(testUserDao, testSQLUser, testSQLAuth);
        userService.register(registerRequest);

        assertEquals(testUser, testUserDao.getUser("testUser"));
    }


    @Order(3)
    @Test
    void registerUserNullUsernameTestNegative() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();

        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();



        RegisterRequest registerRequest = new RegisterRequest(null, "testUserPassword", "testUserEmail");

        userService = new UserService(testUserDao, testSQLUser, testSQLAuth);

        ResponseException ex = assertThrows(ResponseException.class, () -> userService.register(registerRequest));
        assertEquals("Error: bad request", ex.getMessage());
    }


    @Order(4)
    @Test
    void loginUserTestPositive() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");
        testUserDao.registerUser(testUser);

        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();

        testSQLUser.registerUser(testUser);


        LoginRequest loginRequest = new LoginRequest("testUser", "testUserPassword");

        userService = new UserService(testUserDao, testSQLUser, testSQLAuth);
        LoginResult loginResult = userService.login(loginRequest);

        String username = loginResult.username();
        String authToken = loginResult.authToken();

        assertEquals(username, testSQLAuth.getAuthData(authToken).username());
    }


    @Order(5)
    @Test
    void loginUserTestWrongPasswordNegative() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();
        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();



        testUserDao.registerUser(new UserRecord("testUser", "testUserPassword", "testEmailUser"));

        LoginRequest loginRequest = new LoginRequest("testUser", "testUser");

        userService = new UserService(testUserDao, testSQLUser, testSQLAuth);

        ResponseException ex = assertThrows(ResponseException.class, () -> userService.login(loginRequest));
        assertEquals("Error: unauthorized", ex.getMessage());
    }


    @Order(6)
    @Test
    void logoutUserTestPositive() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");
        testUserDao.registerUser(testUser);

        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();



        userService = new UserService(testUserDao, testSQLUser, testSQLAuth);

        AuthDataRecord testSQL = testSQLAuth.createAuthData(testUser);
        String authToken = testSQL.authToken();

        userService.logout(authToken);

        assertNull(testSQLAuth.getAuthData(authToken));
    }


    @Order(7)
    @Test
    void logoutUserTestAuthDataNotRemovedNegative() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");
        testUserDao.registerUser(testUser);

        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();



        userService = new UserService(testUserDao, testSQLUser, testSQLAuth);

        AuthDataRecord testAuth = testSQLAuth.createAuthData(testUser);
        String authToken = testAuth.authToken();

        userService.logout(authToken);

        assertNull(testSQLAuth.getAuthData(authToken));

        ResponseException ex = assertThrows(ResponseException.class, () -> userService.logout(authToken));
        assertEquals("Error: unauthorized", ex.getMessage());
    }


    @Order(8)
    @Test
    void listGamesTestPositive() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();

        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();


        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testSQLAuth, testGameDao);

        int newGame = testGameDao.createGame("testGame");
        GameDataRecord game = testGameDao.getGame(newGame);

        List<CondensedGameData> listGamesResult = gameService.listGames().games();

        boolean gameInList = false;
        for (CondensedGameData g : listGamesResult) {
            if (game.gameID() == g.gameID()) {
                gameInList = true;
                break;
            }
        }

        assertNotNull(listGamesResult);
        assertTrue(gameInList);
    }


    @Order(9)
    @Test
    void listGamesTestNegative() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();

        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();


        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testSQLAuth, testGameDao);

        List<CondensedGameData> listGamesResult = gameService.listGames().games();

        assertNotNull(listGamesResult);
        assertTrue(listGamesResult.isEmpty());

        GameService error = new GameService(null, testSQLAuth, testGameDao);

        assertThrows(NullPointerException.class, error::listGames);
    }


    @Order(10)
    @Test
    void createGameTestPositive() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();
        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();


        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testSQLAuth, testGameDao);

        int newGame = testGameDao.createGame("testGame");
        GameDataRecord game = testGameDao.getGame(newGame);

        assertNotNull(game);
    }

    @Order(11)
    @Test
    void createGameTestNegative() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();
        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();


        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testSQLAuth, testGameDao);

        int newGame = testGameDao.createGame("testGame");
        GameDataRecord game = testGameDao.getGame(newGame);
        assertNotNull(game);

        GameService error = new GameService(testUserDao, testSQLAuth, null);
        assertThrows(NullPointerException.class, error::listGames);
    }


    @Order(12)
    @Test
    void joinGameTestPositive() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");

        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();


        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testSQLAuth, testGameDao);

        int newGame = testGameDao.createGame("testGame");
        GameDataRecord game = testGameDao.getGame(newGame);

        assertNotNull(game);

        JoinGameRequest joinRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, game.gameID());
        gameService.joinGame(joinRequest, testUser.username());

        GameDataRecord updatedGame = testGameDao.getGame(game.gameID());

        assertEquals(testUser.username(), updatedGame.whiteUsername());
    }

    @Order(13)
    @Test
    void joinGameTestNegative() throws DataAccessException {
        UserDAO testUserDao = new UserDAO();

        SQLAuthDataAccess testSQLAuth = new SQLAuthDataAccess();
        SQLUserDataAccess testSQLUser = new SQLUserDataAccess();


        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testSQLAuth, testGameDao);

        int newGame = testGameDao.createGame("testGame");
        GameDataRecord game = testGameDao.getGame(newGame);

        JoinGameRequest joinRequestWhite = new JoinGameRequest(ChessGame.TeamColor.WHITE, game.gameID());
        gameService.joinGame(joinRequestWhite, "testUserName");

        assertNotNull(game);


        ResponseException ex = assertThrows(ResponseException.class, () -> gameService.joinGame(joinRequestWhite, "tryingToStealWhite"));
        assertEquals("Error: already taken", ex.getMessage());
    }

}
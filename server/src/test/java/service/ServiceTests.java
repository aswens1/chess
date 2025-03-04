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
    void allDatabasesShouldBeClear() {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");
        testUserDao.registerUser(testUser);

        GameDAO testGameDao = new GameDAO();
        testGameDao.createGame("testGame");

        AuthDataDAO testAuthDataDao = new AuthDataDAO();
        AuthDataRecord testAuth = testAuthDataDao.createAuthData(testUser);

        clearService = new ClearService(testUserDao, testAuthDataDao, testGameDao);

        clearService.clearAllDatabases();

        assertTrue(testGameDao.listGames().isEmpty());
        assertFalse(testUserDao.doesUserExist(testUser.username()));
        assertNull(testAuthDataDao.getAuthData(testAuth.authToken()));
    }


    @Order(2)
    @Test
    void registerUserTestPositive() {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");

        AuthDataDAO testAuthDataDao = new AuthDataDAO();

        RegisterRequest registerRequest = new RegisterRequest("testUser", "testUserPassword", "testUserEmail");

        userService = new UserService(testUserDao, testAuthDataDao);
        userService.register(registerRequest);

        assertEquals(testUser, testUserDao.getUser("testUser"));
    }


    @Order(3)
    @Test
    void registerUserNullUsernameTestNegative() {
        UserDAO testUserDao = new UserDAO();

        AuthDataDAO testAuthDataDao = new AuthDataDAO();

        RegisterRequest registerRequest = new RegisterRequest(null, "testUserPassword", "testUserEmail");

        userService = new UserService(testUserDao, testAuthDataDao);

        ResponseException ex = assertThrows(ResponseException.class, () -> userService.register(registerRequest));
        assertEquals("Error: bad request", ex.getMessage());
    }


    @Order(4)
    @Test
    void loginUserTestPositive() {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");
        testUserDao.registerUser(testUser);

        AuthDataDAO testAuthDataDao = new AuthDataDAO();

        LoginRequest loginRequest = new LoginRequest("testUser", "testUserPassword");

        userService = new UserService(testUserDao, testAuthDataDao);
        LoginResult loginResult = userService.login(loginRequest);

        String username = loginResult.username();
        String authToken = loginResult.authToken();

        assertEquals(username, testAuthDataDao.getAuthData(authToken).username());
    }


    @Order(5)
    @Test
    void loginUserTestWrongPasswordNegative() {
        UserDAO testUserDao = new UserDAO();
        AuthDataDAO testAuthDataDao = new AuthDataDAO();

        testUserDao.registerUser(new UserRecord("testUser", "testUserPassword", "testEmailUser"));

        LoginRequest loginRequest = new LoginRequest("testUser", "testUser");

        userService = new UserService(testUserDao, testAuthDataDao);

        ResponseException ex = assertThrows(ResponseException.class, () -> userService.login(loginRequest));
        assertEquals("Error: unauthorized", ex.getMessage());
    }


    @Order(6)
    @Test
    void logoutUserTestPositive() {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");
        testUserDao.registerUser(testUser);

        AuthDataDAO testAuthDataDao = new AuthDataDAO();

        userService = new UserService(testUserDao, testAuthDataDao);

        AuthDataRecord testAuth = testAuthDataDao.createAuthData(testUser);
        String authToken = testAuth.authToken();

        userService.logout(authToken);

        assertNull(testAuthDataDao.getAuthData(authToken));
    }


    @Order(7)
    @Test
    void logoutUserTestAuthDataNotRemovedNegative() {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");
        testUserDao.registerUser(testUser);

        AuthDataDAO testAuthDataDao = new AuthDataDAO();

        userService = new UserService(testUserDao, testAuthDataDao);

        AuthDataRecord testAuth = testAuthDataDao.createAuthData(testUser);
        String authToken = testAuth.authToken();

        userService.logout(authToken);

        assertNull(testAuthDataDao.getAuthData(authToken));

        ResponseException ex = assertThrows(ResponseException.class, () -> userService.logout(authToken));
        assertEquals("Error: unauthorized", ex.getMessage());
    }


    @Order(8)
    @Test
    void listGamesTestPositive(){
        UserDAO testUserDao = new UserDAO();
        AuthDataDAO testAuthDataDao = new AuthDataDAO();

        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testAuthDataDao, testGameDao);

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
    void listGamesTestNegative(){
        UserDAO testUserDao = new UserDAO();

        AuthDataDAO testAuthDataDao = new AuthDataDAO();
        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testAuthDataDao, testGameDao);

        List<CondensedGameData> listGamesResult = gameService.listGames().games();

        assertNotNull(listGamesResult);
        assertTrue(listGamesResult.isEmpty());

        GameService error = new GameService(null, testAuthDataDao, testGameDao);

        assertThrows(NullPointerException.class, error::listGames);
    }


    @Order(10)
    @Test
    void createGameTestPositive() {
        UserDAO testUserDao = new UserDAO();
        AuthDataDAO testAuthDataDao = new AuthDataDAO();
        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testAuthDataDao, testGameDao);

        int newGame = testGameDao.createGame("testGame");
        GameDataRecord game = testGameDao.getGame(newGame);

        assertNotNull(game);
    }

    @Order(11)
    @Test
    void createGameTestNegative() {
        UserDAO testUserDao = new UserDAO();
        AuthDataDAO testAuthDataDao = new AuthDataDAO();
        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testAuthDataDao, testGameDao);

        int newGame = testGameDao.createGame("testGame");
        GameDataRecord game = testGameDao.getGame(newGame);
        assertNotNull(game);

        GameService error = new GameService(testUserDao, testAuthDataDao, null);
        assertThrows(NullPointerException.class, error::listGames);
    }


    @Order(12)
    @Test
    void joinGameTestPositive() {
        UserDAO testUserDao = new UserDAO();
        UserRecord testUser = new UserRecord("testUser", "testUserPassword", "testUserEmail");

        AuthDataDAO testAuthDataDao = new AuthDataDAO();
        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testAuthDataDao, testGameDao);

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
    void joinGameTestNegative() {
        UserDAO testUserDao = new UserDAO();

        AuthDataDAO testAuthDataDao = new AuthDataDAO();
        GameDAO testGameDao = new GameDAO();

        gameService = new GameService(testUserDao, testAuthDataDao, testGameDao);

        int newGame = testGameDao.createGame("testGame");
        GameDataRecord game = testGameDao.getGame(newGame);

        JoinGameRequest joinRequestWhite = new JoinGameRequest(ChessGame.TeamColor.WHITE, game.gameID());
        gameService.joinGame(joinRequestWhite, "testUserName");

        assertNotNull(game);


        ResponseException ex = assertThrows(ResponseException.class, () -> gameService.joinGame(joinRequestWhite, "tryingToStealWhite"));
        assertEquals("Error: already taken", ex.getMessage());
    }

}
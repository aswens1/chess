package service;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDataDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import handler.*;
import model.AuthDataRecord;
import model.GameDataRecord;
import model.UserRecord;
import org.junit.jupiter.api.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    static UserService userService;
//    private final UserHandler userHandler;
    static GameService gameService;
//    private final GameHandler gameHandler;
    static ClearService clearService;
//    private final ClearHandler clearHandler;


//    @BeforeEach
//    void setUp() {
//        UserDAO testUserDao = new UserDAO();
//        GameDAO testGameDao = new GameDAO();
//        AuthDataDAO testAuthDataDao = new AuthDataDAO();
//
//        userService = new UserService(testUserDao, testAuthDataDao);
//        gameService = new GameService(testUserDao, testAuthDataDao, testGameDao);
//        clearService = new ClearService(testUserDao, testAuthDataDao, testGameDao);
//    }


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

    @Disabled
    void loginUserTestNegative() {

    }

    @Disabled
    void logoutUserTestPositive() {

    }

    @Disabled
    void logoutUserTestNegative() {

    }

    @Disabled
    void listGamesTestPositive(){

    }

    @Disabled
    void listGamesTestNegative(){

    }

    @Disabled
    void createGameTestPositive() {

    }

    @Disabled
    void createGameTestNegative() {

    }

    @Disabled
    void joinGameTestPositive() {

    }

    @Disabled
    void joinGameTestNegative() {

    }
}
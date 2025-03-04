package service;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;

import java.util.HashMap;

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
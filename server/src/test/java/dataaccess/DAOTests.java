package dataaccess;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.*;

public class DAOTests {
    static SQLGameDataAccess testSQLGame;
    static SQLUserDataAccess testSQLUser;
    static SQLAuthDataAccess testSQLAuth;

    static ClearService clearService;

    static UserRecord testUser;
    static AuthDataRecord testAuth;

    @BeforeAll
    public static void setUp() {
        try {
            testSQLGame = new SQLGameDataAccess();
            testSQLUser = new SQLUserDataAccess();
            testSQLAuth = new SQLAuthDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        clearService = new ClearService(testSQLUser, testSQLAuth, testSQLGame);
        clearService.clearAllDatabases();
    }

    @AfterEach
    void cleanUp() {
        clearService.clearAllDatabases();
    }

    @Order(1)
    @Test
    void positiveRegisterUser() {
        testUser = new UserRecord("testUserName", "testUserPassword", "testUserEmail");
        testSQLUser.registerUser(testUser);
        assertTrue(testSQLUser.doesUserExist(testUser.username()));
    }

    @Order(2)
    @Test
    void negativeRegisterUser() {
        testUser = new UserRecord(null, "testUserPassword", "testUserEmail");
        ResponseException ex = assertThrows(ResponseException.class, () -> testSQLUser.registerUser(testUser));
        assertEquals("UserData Database Error: Column 'username' cannot be null", ex.getMessage());
    }

    @Order(3)
    @Test
    void positiveGetUser() {
        testUser = new UserRecord("testUserName", "testUserPassword", "testUserEmail");
        testSQLUser.registerUser(testUser);

        UserRecord tester = testSQLUser.getUser(testUser.username());
        assertEquals(tester.username(), testUser.username());
    }

    @Order(4)
    @Test
    void negativeGetUser() {
        ResponseException ex = assertThrows(ResponseException.class, () -> testSQLUser.getUser(null));
        assertEquals("Error: bad request", ex.getMessage());
    }

    @Order(5)
    @Test
    void positiveDoesUserExist() {
        testUser = new UserRecord("testUserName", "testUserPassword", "testUserEmail");
        testSQLUser.registerUser(testUser);

        assertTrue(testSQLUser.doesUserExist(testUser.username()));
    }

    @Order(6)
    @Test
    void negativeDoesUserExist() {
        ResponseException ex = assertThrows(ResponseException.class, () -> testSQLUser.doesUserExist(null));
        assertEquals("Error: bad request", ex.getMessage());
    }

    @Order(7)
    @Test
    void clearUserData() {
        testUser = new UserRecord("testUserName", "testUserPassword", "testUserEmail");
        testSQLUser.registerUser(testUser);

        testSQLUser.clear();

        assertFalse(testSQLUser.doesUserExist(testUser.username()));
    }

    @Order(8)
    @Test
    void positiveCreateAuthData(){
        testUser = new UserRecord("testUserName", "testUserPassword", "testUserEmail");
        testAuth = testSQLAuth.createAuthData(testUser);

        String authToken = testAuth.authToken();
        assertNotNull(authToken);

        assertEquals(testUser.username(), testSQLAuth.getAuthData(testAuth.authToken()).username());
    }

    @Order(9)
    @Test
    void negativeCreateAuthData() {
        testUser = new UserRecord(null, "testUserPassword", "testUserEmail");

        ResponseException ex = assertThrows(ResponseException.class, () -> testSQLAuth.createAuthData(testUser));
        assertEquals("Error: bad request", ex.getMessage());
    }

    @Order(10)
    @Test
    void positiveGetAuthData() {
        testUser = new UserRecord("testUserName", "testUserPassword", "testUserEmail");
        testAuth = testSQLAuth.createAuthData(testUser);

        String authToken = testAuth.authToken();
        assertNotNull(authToken);

        assertEquals(testUser.username(), testSQLAuth.getAuthData(testAuth.authToken()).username());
    }

    @Order(11)
    @Test
    void negativeGetAuthData() {
        ResponseException ex = assertThrows(ResponseException.class, () -> testSQLAuth.getAuthData(null));
        assertEquals("Error: bad request", ex.getMessage());
    }

    @Order(12)
    @Test
    void positiveDeleteAuthData() {
        testUser = new UserRecord("testUserName", "testUserPassword", "testUserEmail");
        testAuth = testSQLAuth.createAuthData(testUser);

        String authToken = testAuth.authToken();
        assertNotNull(authToken);

        assertTrue(testSQLAuth.deleteAuthData(authToken));
    }

    @Order(13)
    @Test
    void negativeDeleteAuthData() {
        ResponseException ex = assertThrows(ResponseException.class, () -> testSQLAuth.deleteAuthData(null));
        assertEquals("Error: bad request", ex.getMessage());
    }

    @Order(14)
    @Test
    void clearAuthData() {
        testUser = new UserRecord("testUserName", "testUserPassword", "testUserEmail");
        testAuth = testSQLAuth.createAuthData(testUser);

        String authToken = testAuth.authToken();
        assertNotNull(authToken);

        testSQLAuth.clear();

        assertNull(testSQLAuth.getAuthData(authToken));
    }

    @Order(15)
    @Test
    void positiveCreateGame() {
        int gameID = testSQLGame.createGame("testingCreateGame");
        String gamename = testSQLGame.getGame(gameID).gameName();

        assertEquals("testingCreateGame", gamename);
    }

    @Order(16)
    @Test
    void negativeCreateGame() {
        ResponseException ex = assertThrows(ResponseException.class, () -> testSQLGame.createGame(null));
        assertEquals("Error: bad request", ex.getMessage());
    }

    @Order(15)
    @Disabled
    void positiveListGames() {

    }

    @Order(16)
    @Disabled
    void negativeListGames() {

    }

    @Order(14)
    @Disabled
    void positiveUpdateGame() {

    }
    @Order(14)
    @Disabled
    void negativeUpdateGame() {

    }
    @Order(14)
    @Disabled
    void positiveJoinGame() {

    }
    @Order(14)
    @Disabled
    void negativeJoinGame() {

    }
    @Order(14)
    @Disabled
    void clearGameData() {

    }
}

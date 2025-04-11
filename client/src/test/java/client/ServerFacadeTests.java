package client;

import chess.ChessGame;
import exception.ResponseException;
import model.CondensedGameData;
import org.junit.jupiter.api.*;
import records.*;
import server.*;
import ui.ServerFacade;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade sf;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        sf = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearDatabase() {
        assertDoesNotThrow(() -> sf.clear());
    }

    @Test
    public void goodRegisterUser() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);

        assertNotNull(result);
        assertNotNull(result.authToken());
        assertEquals("testUser", result.username());
    }

    @Test
    public void badRegisterUser() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);

        RegisterRequest badRequest = new RegisterRequest("testUser", "", "");

        assertNotNull(result);

        assertThrows(ResponseException.class, () -> sf.registerUser(badRequest));
    }

    @Test
    public void goodLogin() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);
        sf.logoutUser(new LogoutRequest(result.authToken()));

        LoginResult loginResult = sf.loginUser(new LoginRequest("testUser", "testPass"));

        assertNotNull(loginResult);
        assertNotNull(loginResult.authToken());
    }

    @Test
    public void badLogin() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);
        sf.logoutUser(new LogoutRequest(result.authToken()));

        assertThrows(ResponseException.class, () -> sf.loginUser(new LoginRequest("testUser", "badPass")));
    }

    @Test
    public void goodLogout() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);

        assertDoesNotThrow(()-> sf.logoutUser(new LogoutRequest(result.authToken())));
    }

    @Test
    public void badLogout() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        sf.registerUser(request);

        assertThrows(ResponseException.class, ()-> sf.logoutUser(new LogoutRequest(null)));
    }

    @Test
    public void goodList() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);

        sf.create(new CreateGameRequest("testGame"), result.authToken());

        ListGamesResult listGamesResult = sf.list(new ListGamesRequest(result.authToken()));
        assertNotNull(listGamesResult);
        assertFalse(listGamesResult.games().isEmpty());
    }

    @Test
    public void badList() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);

        sf.create(new CreateGameRequest("testGame"), result.authToken());

        assertThrows(ResponseException.class, ()-> sf.list(new ListGamesRequest(null)));
    }

    @Test
    public void goodCreate() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);

        sf.create(new CreateGameRequest("testGame"), result.authToken());

        ListGamesResult listGamesResult = sf.list(new ListGamesRequest(result.authToken()));

        String gameName = "";
        assertNotNull(listGamesResult);
        for (var i = 0; i < listGamesResult.games().size(); i++) {
            var game = listGamesResult.games().get(i);
            gameName = game.gameName();
        }
        assertEquals("testGame", gameName);
    }

    @Test
    public void badCreate() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        sf.registerUser(request);

        assertThrows(ResponseException.class, ()-> sf.create(new CreateGameRequest("testGame"), null));
    }

    @Test
    public void goodJoin() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);

        sf.create(new CreateGameRequest("testGame"), result.authToken());

        ListGamesResult listOne = sf.list(new ListGamesRequest(result.authToken()));
        assertNotNull(listOne);

        int gameInListId = 0;
        CondensedGameData gameToJoin = listOne.games().get(gameInListId);
        int serverGameID = gameToJoin.gameID();

        sf.join(new JoinGameRequest(ChessGame.TeamColor.WHITE, serverGameID), result.authToken());

        ListGamesResult listTwo = sf.list(new ListGamesRequest(result.authToken()));
        assertNotNull(listTwo);

        CondensedGameData findWhitePlayer = listTwo.games().get(gameInListId);
        String whiteUser = findWhitePlayer.whiteUsername();

        assertEquals("testUser", whiteUser);
    }

    @Test
    public void badJoin() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);

        sf.create(new CreateGameRequest("testGame"), result.authToken());

        ListGamesResult listOne = sf.list(new ListGamesRequest(result.authToken()));
        assertNotNull(listOne);

        int gameInListId = 0;
        CondensedGameData gameToJoin = listOne.games().get(gameInListId);
        int serverGameID = gameToJoin.gameID();

        assertThrows(ResponseException.class, () -> sf.join(new JoinGameRequest(ChessGame.TeamColor.WHITE, serverGameID), null));
    }

    @Test
    public void goodObserve() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);

        sf.create(new CreateGameRequest("testGame"), result.authToken());

        ListGamesResult listOne = sf.list(new ListGamesRequest(result.authToken()));
        assertNotNull(listOne);
        assertFalse(listOne.games().isEmpty());

        int actualGameID = listOne.games().getFirst().gameID();

        assertDoesNotThrow(() -> sf.observe(actualGameID, result.authToken()));
    }

    @Test
    public void badObserve() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);

        sf.create(new CreateGameRequest("testGame"), result.authToken());

        ListGamesResult listOne = sf.list(new ListGamesRequest(result.authToken()));
        assertNotNull(listOne);

        int actualGameID = listOne.games().getFirst().gameID();

        assertThrows(ResponseException.class, () -> sf.observe(actualGameID, null));
    }

    @Test
    public void goodClearDB() {
        RegisterRequest request = new RegisterRequest("testUser", "testPass", "test@example.com");
        RegisterResult result = sf.registerUser(request);
        assertNotNull(result);

        sf.clear();
        assertThrows(ResponseException.class, () -> sf.loginUser(new LoginRequest("testUser", "testPass")));
    }
}

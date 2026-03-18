package client;

import com.google.gson.Gson;
import model.ListGamesData;
import org.junit.jupiter.api.*;
import server.ResponseException;
import server.Server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {


    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = String.format("http://localhost:%d", port);
        facade = new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setup() throws ResponseException{
        facade.clear();
    }

    @Test
    void testRegisterSuccess() throws ResponseException {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void testRegisterFailure() throws ResponseException{
        assertThrows(ResponseException.class, ()->facade.register(null, "password", "p1@email.com"));
    }

    @Test
    void testLoginSuccess() throws ResponseException{
        facade.register("player1", "password", "p1@email.com");

        var authData = facade.login("player1", "password");
        assertTrue(authData.authToken().length() > 10);
        assertEquals("player1", authData.username());
    }

    @Test
    void testLoginInvalidInput() throws ResponseException{
        facade.register("player1", "password", "p1@email.com");

        assertThrows(ResponseException.class, ()->facade.login(null, "password"));
        assertThrows(ResponseException.class, ()->facade.login("player1", null));
        assertThrows(ResponseException.class, ()->facade.login(null, null));
        assertThrows(ResponseException.class, ()->facade.login("badUser", "password"));
        assertThrows(ResponseException.class, ()->facade.login("player1", "badpassword"));
    }

    @Test
    void testLogoutSuccess() throws ResponseException{
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");

        facade.logout("player1");

    }

    @Test
    void testLogoutTwiceFailure() throws ResponseException{
        var authData = facade.register("player1", "password", "p1@email.com");

        facade.logout("player1");
        assertThrows(ResponseException.class, ()-> facade.logout("player1"));
    }

    @Test
    void testCreateGameSuccess() throws ResponseException{
        facade.register("player1", "password", "p1@email.com");

        var createGame = facade.createGame("game1", "player1");
        assertNotNull(createGame);
        assertEquals(1, createGame.gameID());
    }

    @Test
    void testCreateGameInvalidInput() throws ResponseException{
        facade.register("player1", "password", "p1@email.com");

        assertThrows(ResponseException.class, () -> facade.createGame(null, "player1"));
        assertThrows(ResponseException.class, () -> facade.createGame("game1", "player2"));

        facade.register("player2", "password2", "p2@email.com");
        facade.logout("player2");
        assertThrows(ResponseException.class, () -> facade.createGame("game2", "player2"));
    }

    @Test
    void testListGameSuccess() throws ResponseException{
        Collection<ListGamesData> gamesComparison = new ArrayList<>();

        ListGamesData testGame1 = new ListGamesData(1, null, null, "game1");
        ListGamesData testGame2 = new ListGamesData(2, null, null, "game2");
        ListGamesData testGame3 = new ListGamesData(3, null, null, "game3");

        gamesComparison.add(testGame1);
        gamesComparison.add(testGame2);
        gamesComparison.add(testGame3);

        facade.register("player1", "password", "p1@email.com");

        facade.createGame("game1", "player1");
        facade.createGame("game2", "player1");
        facade.createGame("game3", "player1");

        var listGames = facade.listGames("player1");
        assertNotNull(listGames);
        assertEquals(gamesComparison, listGames.games());
    }

    @Test
    void testListGameUnauthorized() throws ResponseException{

    }

    @Test
    void testJoinGameSuccess() throws ResponseException{

    }

    @Test
    void testJoinGameDoesNotExist() throws ResponseException{

    }
}

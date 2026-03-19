package client;

import chess.ChessGame;
import com.google.gson.Gson;
import model.ListGamesData;
import model.ListGamesResponse;
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

        facade.logout();
        assertNull(facade.getAuthToken());
    }

    @Test
    void testLogoutTwiceFailure() throws ResponseException{
        facade.register("player1", "password", "p1@email.com");

        facade.logout();
        assertThrows(ResponseException.class, ()-> facade.logout());
    }

    @Test
    void testCreateGameSuccess() throws ResponseException{
        facade.register("player1", "password", "p1@email.com");

        var createGame = facade.createGame("game1");
        assertNotNull(createGame);
        assertEquals(1, createGame.gameID());
    }

    @Test
    void testCreateGameInvalidInput() throws ResponseException{
        facade.register("player1", "password", "p1@email.com");
        assertThrows(ResponseException.class, () -> facade.createGame(null));
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

        facade.createGame("game1");
        facade.createGame("game2");
        facade.createGame("game3");

        var listGames = facade.listGames();
        assertNotNull(listGames);
        assertEquals(gamesComparison, listGames.games());
    }

    @Test
    void testListGameUnauthorized() throws ResponseException{
        assertThrows(ResponseException.class, () -> facade.listGames());
    }

    @Test
    void testJoinGameSuccess() throws ResponseException{
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("game1");
        facade.joinGame(ChessGame.TeamColor.WHITE, 1);
        var listGames = facade.listGames();

        ListGamesData gameComparison = new ListGamesData(1, "player1", null, "game1");
        Collection<ListGamesData> gamesComparison = new ArrayList<>();
        gamesComparison.add(gameComparison);

        assertEquals(gamesComparison, listGames.games());
    }

    @Test
    void testJoinGameDoesNotExist() throws ResponseException{
        facade.register("player1", "password", "p1@email.com");
        assertThrows(ResponseException.class, () -> facade.joinGame(ChessGame.TeamColor.WHITE, 1));
    }
}

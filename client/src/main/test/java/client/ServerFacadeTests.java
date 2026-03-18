package client;

import org.junit.jupiter.api.*;
import server.ResponseException;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = String.format("http://localhost:%d/", port);
        facade = new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setup(){

    }

    @Test
    void testRegisterSuccess() throws ResponseException {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void testRegisterFailure() throws ResponseException{

    }

    @Test
    void testLoginSuccess() throws ResponseException{

    }

    @Test
    void testLoginInvalidInput() throws ResponseException{

    }

    @Test
    void testLogoutSuccess() throws ResponseException{

    }

    @Test
    void testLogoutTwiceFailure() throws ResponseException{

    }

    @Test
    void testCreateGameSuccess() throws ResponseException{

    }

    @Test
    void testCreateGameInvalidInput() throws ResponseException{

    }

    @Test
    void testListGameSuccess() throws ResponseException{

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

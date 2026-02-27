package passoff.server;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.*;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryGameDAO;
import dataaccess.Memory.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import service.GameService;
import service.UserService;
import model.*;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    static final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static final MemoryGameDAO gameDAO = new MemoryGameDAO();
    static final MemoryUserDAO userDAO = new MemoryUserDAO();
    static final GameService service = new GameService(gameDAO, authDAO);
    static final UserService userService = new UserService(authDAO, userDAO);

    @BeforeEach
    public void setup() throws ResponseException{
        service.deleteAllGames();
    }

    @Test
    public void testGetGameList() throws ResponseException{
        try{
            AuthData authTest = userService.createUser("test", "test", "test@test.com");

            Collection<GameData> gamesComparison = new ArrayList<>();
            GameData game1 = gameDAO.createGame(new GameData(1, "white1", "black1", "game1", new ChessGame()));
            GameData game2 = gameDAO.createGame(new GameData(2, "white2", "black2", "game2", new ChessGame()));
            GameData game3 = gameDAO.createGame(new GameData(3, "white3", "black3", "game3", new ChessGame()));
            gamesComparison.add(game1);
            gamesComparison.add(game2);
            gamesComparison.add(game3);

            assertEquals(service.getListGames(authTest.authToken()), gamesComparison);
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }


    }

    @Test
    public void testUnauthorizedGetGamesList() throws ResponseException{
        assertThrows(ResponseException.class, ()-> service.getListGames("fakeAuth"));
    }

    @Test
    public void testCreateGame() throws ResponseException{
        try{
            AuthData authTest = userService.createUser("test", "test", "test@test.com");
            GameData newGame = service.createGame(authTest.authToken(), "newGame");

            assertNotNull(gameDAO.getGame(newGame.gameID()));
            assertEquals("newGame", newGame.gameName());
            assertNull(newGame.blackUsername());
            assertNull(newGame.whiteUsername());
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }

    @Test
    public void testCreateGameNoName() throws ResponseException{
        AuthData authTest = userService.createUser("test", "test", "test@test.com");

        assertThrows(ResponseException.class, () -> service.createGame(authTest.authToken(), null));
    }

    @Test
    public void testJoinGame() throws ResponseException{
        try{
            AuthData authTest = userService.createUser("test", "test", "test@test.com");
            GameData testGame = service.createGame(authTest.authToken(), "newGame");

            service.joinGame(authTest.authToken(), ChessGame.TeamColor.WHITE, testGame.gameID());
            GameData joinedGame = gameDAO.getGame(testGame.gameID());

            assertNotNull(joinedGame);
            assertEquals("test", joinedGame.whiteUsername());
            assertNull(joinedGame.blackUsername());

        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }

    @Test
    public void testUnauthorizedJoinGame() throws ResponseException{
        assertThrows(ResponseException.class, () -> service.joinGame("fakeAuth", ChessGame.TeamColor.WHITE, 1));
    }

    @Test
    public void testTakenColorJoinGame() throws ResponseException{
        AuthData authTest = userService.createUser("test", "test", "test@test.com");
        AuthData testUser = userService.createUser("test1", "test1", "test1@test.com");
        GameData testGame = service.createGame(authTest.authToken(), "newGame");

        service.joinGame(authTest.authToken(), ChessGame.TeamColor.WHITE, testGame.gameID());
        assertThrows(ResponseException.class, ()-> service.joinGame(testUser.authToken(), ChessGame.TeamColor.WHITE, testGame.gameID()));
    }

    @Test
    public void testDeleteAllGames() throws ResponseException{
        try{
            gameDAO.createGame(new GameData(1, "white1", "black1", "game1", new ChessGame()));
            gameDAO.createGame(new GameData(2, "white2", "black2", "game2", new ChessGame()));
            service.deleteAllGames();

            assertNull(gameDAO.getGame(1));
            assertNull(gameDAO.getGame(2));
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }
}

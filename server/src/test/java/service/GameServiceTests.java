package service;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.response.CreateGameResponse;
import server.ResponseException;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    static final MemoryAuthDAO AUTH_DAO = new MemoryAuthDAO();
    static final MemoryGameDAO GAME_DAO = new MemoryGameDAO();
    static final MemoryUserDAO USER_DAO = new MemoryUserDAO();
    static final GameService SERVICE = new GameService(GAME_DAO, AUTH_DAO);
    static final UserService USER_SERVICE = new UserService(AUTH_DAO, USER_DAO);

    @BeforeEach
    public void setup() throws ResponseException{
        SERVICE.deleteAllGames();
        USER_SERVICE.deleteAllUsers();

    }

    @Test
    public void testGetGameList() throws ResponseException{
        try{
            AuthData authTest = USER_SERVICE.createUser("test", "test", "test@test.com");
            AUTH_DAO.getAuth(authTest.authToken());

            Collection<ListGamesData> gamesComparison = new ArrayList<>();
            GAME_DAO.createGame(new GameData(1, "white1", "black1", "game1", new ChessGame()));
            GAME_DAO.createGame(new GameData(2, "white2", "black2", "game2", new ChessGame()));
            GAME_DAO.createGame(new GameData(3, "white3", "black3", "game3", new ChessGame()));

            ListGamesData testGame1 = new ListGamesData(1, "white1", "black1", "game1");
            ListGamesData testGame2 = new ListGamesData(2, "white2", "black2", "game2");
            ListGamesData testGame3 = new ListGamesData(3, "white3", "black3", "game3");

            gamesComparison.add(testGame1);
            gamesComparison.add(testGame2);
            gamesComparison.add(testGame3);

            AUTH_DAO.getAuth(authTest.authToken());

            assertEquals(new Gson().toJson(Map.of("games", gamesComparison)), SERVICE.getListGames(authTest.authToken()));
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }


    }

    @Test
    public void testUnauthorizedGetGamesList() throws ResponseException{
        assertThrows(ResponseException.class, ()-> SERVICE.getListGames("fakeAuth"));
    }

    @Test
    public void testCreateGame() throws ResponseException{
        try{
            AuthData authTest = USER_SERVICE.createUser("test", "test", "test@test.com");
            CreateGameResponse newGame = SERVICE.createGame(authTest.authToken(), "newGame");

            GameData game = GAME_DAO.getGame(newGame.gameID());

            assertNotNull(game);
            assertEquals("newGame", game.gameName());
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }

    @Test
    public void testCreateGameNoName() throws ResponseException{
        AuthData authTest = USER_SERVICE.createUser("test", "test", "test@test.com");

        assertThrows(ResponseException.class, () -> SERVICE.createGame(authTest.authToken(), null));
    }

    @Test
    public void testJoinGame() throws ResponseException{
        try{
            AuthData authTest = USER_SERVICE.createUser("test", "test", "test@test.com");
            CreateGameResponse testGame = SERVICE.createGame(authTest.authToken(), "newGame");

            SERVICE.joinGame(authTest.authToken(), ChessGame.TeamColor.WHITE, testGame.gameID());
            GameData joinedGame = GAME_DAO.getGame(testGame.gameID());

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
        assertThrows(ResponseException.class, () -> SERVICE.joinGame("fakeAuth", ChessGame.TeamColor.WHITE, 1));
    }

    @Test
    public void testTakenColorJoinGame() throws ResponseException{
        AuthData authTest = USER_SERVICE.createUser("test", "test", "test@test.com");
        AuthData testUser = USER_SERVICE.createUser("test1", "test1", "test1@test.com");
        CreateGameResponse testGame = SERVICE.createGame(authTest.authToken(), "newGame");

        SERVICE.joinGame(authTest.authToken(), ChessGame.TeamColor.WHITE, testGame.gameID());
        assertThrows(ResponseException.class, ()-> SERVICE.joinGame(testUser.authToken(), ChessGame.TeamColor.WHITE, testGame.gameID()));
    }

    @Test
    public void testDeleteAllGames() throws ResponseException{
        try{
            GAME_DAO.createGame(new GameData(1, "white1", "black1", "game1", new ChessGame()));
            GAME_DAO.createGame(new GameData(2, "white2", "black2", "game2", new ChessGame()));
            SERVICE.deleteAllGames();

            assertNull(GAME_DAO.getGame(1));
            assertNull(GAME_DAO.getGame(2));
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }
}

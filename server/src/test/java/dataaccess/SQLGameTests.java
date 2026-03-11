package dataaccess;
import chess.ChessGame;
import dataaccess.sql.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameTests {
    
    private SQLGameDAO game_DAO;

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        game_DAO = new SQLGameDAO();
        game_DAO.deleteAllGames();
    }

    @Test
    public void testCreateGame() throws DataAccessException{
        GameData comparison = new GameData(1,
                null,
                null,
                "testGame",
                new ChessGame());

        GameData result = game_DAO.createGame(comparison);

        assertNotNull(result);
        assertNull(result.whiteUsername());
        assertNull(result.blackUsername());
        assertEquals(comparison, result);
    }

    @Test
    public void testCreateGameNullFields() throws DataAccessException{
        GameData badGame = new GameData(1,
                null,
                null,
                null,
                new ChessGame());

        assertThrows(DataAccessException.class, () -> game_DAO.createGame(badGame));
    }

    @Test
    public void testListGames() throws DataAccessException{
        GameData comparisonWhite = new GameData(1,
                "whiteUser",
                null,
                "testGameWhite",
                new ChessGame());

        GameData comparisonBlack = new GameData(2,
                null,
                "blackUser",
                "testGameBlack",
                new ChessGame());

        GameData comparisonBoth = new GameData(3,
                "whiteUser",
                "blackUser",
                "testGameBoth",
                new ChessGame());

        HashMap<Integer, GameData> comparison = new HashMap<>();
        comparison.put(1, comparisonWhite);
        comparison.put(2, comparisonBlack);
        comparison.put(3, comparisonBoth);

        game_DAO.createGame(comparisonWhite);
        game_DAO.createGame(comparisonBlack);
        game_DAO.createGame(comparisonBoth);

        HashMap<Integer, GameData> result = game_DAO.listGames();

        assertEquals(comparison, result);
    }

    @Test
    public void testListGamesNoList() throws DataAccessException{
        HashMap<Integer, GameData> comparison = new HashMap<>();
        assertEquals(comparison, game_DAO.listGames());
    }

    @Test
    public void testGetGame() throws DataAccessException{
        GameData comparison = new GameData(1,
                null,
                null,
                "testGame",
                new ChessGame());

        game_DAO.createGame(comparison);
        GameData result = game_DAO.getGame(1);

        assertNotNull(result);
        assertEquals(comparison, result);
    }

    @Test
    public void testGetGameDoesNotExist() throws DataAccessException{
        assertNull(game_DAO.getGame(1));
    }

    @Test
    public void testUpdatePlayer() throws DataAccessException{
        GameData comparison1 = new GameData(1,
                "user1",
                null,
                "testGameWhite",
                new ChessGame());

        GameData comparison2 = new GameData(2,
                null,
                "user2",
                "testGameBlack",
                new ChessGame());

        GameData comparison3 = new GameData(3,
                "user1",
                "user2",
                "testGameBoth",
                new ChessGame());

        GameData testGameWhite = new GameData(1,
                null,
                null,
                "testGameWhite",
                new ChessGame());

        GameData testGameBlack = new GameData(2,
                null,
                null,
                "testGameBlack",
                new ChessGame());

        GameData testGameBoth = new GameData(3,
                null,
                null,
                "testGameBoth",
                new ChessGame());

        game_DAO.createGame(testGameWhite);
        game_DAO.createGame(testGameBlack);
        game_DAO.createGame(testGameBoth);

        game_DAO.updateGamePlayers(testGameWhite.gameID(), "user1", null);
        game_DAO.updateGamePlayers(testGameBlack.gameID(), null, "user2");
        game_DAO.updateGamePlayers(testGameBoth.gameID(), "user1", "user2");

        GameData updatedGameWhite = game_DAO.getGame(1);
        GameData updatedGameBlack = game_DAO.getGame(2);
        GameData updatedGameBoth = game_DAO.getGame(3);

        assertNotNull(updatedGameWhite.whiteUsername());
        assertNull(updatedGameWhite.blackUsername());
        assertEquals(comparison1, updatedGameWhite);

        assertNull(updatedGameBlack.whiteUsername());
        assertNotNull(updatedGameBlack.blackUsername());
        assertEquals(comparison2, updatedGameBlack);

        assertNotNull(updatedGameBoth.whiteUsername());
        assertNotNull(updatedGameBoth.blackUsername());
        assertEquals(comparison3, updatedGameBoth);

    }

    @Test
    public void testUpdatePlayerNoGame() throws DataAccessException{
        GameData fakeGame = new GameData(1, null, null, "fake", new ChessGame());
        game_DAO.createGame(fakeGame);
        game_DAO.deleteAllGames();

        assertThrows(DataAccessException.class, () -> game_DAO.updateGamePlayers(1, "test1", "test2"));
    }

    @Test
    public void testDeleteAllGames() throws DataAccessException{
        GameData game1 = new GameData(1,
                null,
                null,
                "testGame1",
                new ChessGame());
        GameData game2 = new GameData(2,
                null,
                null,
                "testGame2",
                new ChessGame());
        GameData game3 = new GameData(3,
                null,
                null,
                "testGame3",
                new ChessGame());

        game_DAO.createGame(game1);
        game_DAO.createGame(game2);
        game_DAO.createGame(game3);

        game_DAO.deleteAllGames();

        assertNull(game_DAO.getGame(1));
        assertNull(game_DAO.getGame(2));
        assertNull(game_DAO.getGame(3));

    }
}

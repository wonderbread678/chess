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
    
    private SQLGameDAO GAME_DAO;

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        GAME_DAO = new SQLGameDAO();
        GAME_DAO.deleteAllGames();
    }

    @Test
    public void testCreateGame() throws DataAccessException{
        GameData comparison = new GameData(1,
                null,
                null,
                "testGame",
                new ChessGame());

        GameData result = GAME_DAO.createGame(comparison);

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

        assertThrows(DataAccessException.class, () -> GAME_DAO.createGame(badGame));
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

        GAME_DAO.createGame(comparisonWhite);
        GAME_DAO.createGame(comparisonBlack);
        GAME_DAO.createGame(comparisonBoth);

        HashMap<Integer, GameData> result = GAME_DAO.listGames();

        assertEquals(comparison, result);
    }

    @Test
    public void testListGamesNoList() throws DataAccessException{
        HashMap<Integer, GameData> comparison = new HashMap<>();
        assertEquals(comparison, GAME_DAO.listGames());
    }

    @Test
    public void testGetGame() throws DataAccessException{
        GameData comparison = new GameData(1,
                null,
                null,
                "testGame",
                new ChessGame());

        GAME_DAO.createGame(comparison);
        GameData result = GAME_DAO.getGame(1);

        assertNotNull(result);
        assertEquals(comparison, result);
    }

    @Test
    public void testGetGameDoesNotExist() throws DataAccessException{
        assertThrows(DataAccessException.class, () -> GAME_DAO.getGame(1));
    }

    @Test
    public void testUpdatePlayer() throws DataAccessException{
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

        GAME_DAO.createGame(testGameWhite);
        GAME_DAO.createGame(testGameBlack);
        GAME_DAO.createGame(testGameBoth);

        GAME_DAO.updateGamePlayers(testGameWhite.gameID(), "whiteUser", null);
        GAME_DAO.updateGamePlayers(testGameBlack.gameID(), null, "blackUser");
        GAME_DAO.updateGamePlayers(testGameBoth.gameID(), "whiteUser", "blackUser");

        GameData updatedGameWhite = GAME_DAO.getGame(1);
        GameData updatedGameBlack = GAME_DAO.getGame(2);
        GameData updatedGameBoth = GAME_DAO.getGame(3);

        assertNotNull(updatedGameWhite.whiteUsername());
        assertNull(updatedGameWhite.blackUsername());
        assertEquals(comparisonWhite, updatedGameWhite);

        assertNull(updatedGameBlack.whiteUsername());
        assertNotNull(updatedGameBlack.blackUsername());
        assertEquals(comparisonBlack, updatedGameBlack);

        assertNotNull(updatedGameBoth.whiteUsername());
        assertNotNull(updatedGameBoth.blackUsername());
        assertEquals(comparisonBoth, updatedGameBoth);

    }

    @Test
    public void testUpdatePlayerNoGame() throws DataAccessException{
        GameData fakeGame = new GameData(1, null, null, "fake", new ChessGame());
        GAME_DAO.createGame(fakeGame);
        GAME_DAO.deleteAllGames();

        assertThrows(DataAccessException.class, () -> GAME_DAO.updateGamePlayers(1, "test1", "test2"));
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

        GAME_DAO.createGame(game1);
        GAME_DAO.createGame(game2);
        GAME_DAO.createGame(game3);

        GAME_DAO.deleteAllGames();

        assertThrows(DataAccessException.class, () -> GAME_DAO.getGame(1));
        assertThrows(DataAccessException.class, () -> GAME_DAO.getGame(2));
        assertThrows(DataAccessException.class, () -> GAME_DAO.getGame(3));

    }
}

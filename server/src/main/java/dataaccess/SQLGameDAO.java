package dataaccess;

import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException{
        configureGameDatabase();
    }

    public GameData createGame(GameData gameData) throws DataAccessException {
        return null;
    }

    public HashMap<Integer, GameData> listGames() throws DataAccessException {
        return null;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    public void updateGamePlayers(GameData game, String whiteUsername, String blackUsername) throws DataAccessException {

    }

    public void deleteAllGames() throws DataAccessException {

    }

    private final String[] createGameStatements = {
            """
            CREATE TABLE IF NOT EXISTS GameDataTable (
            `gameID` int NOT NULL,
            `whiteUsername` varchar(256) DEFAULT NULL,
            `blackUsername` varchar(256) DEFAULT NULL,
            `gameName` varchar(256) NOT NULL,
            `game` TEXT NOT NULL,
            PRIMARY KEY(`gameID`),
            INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureGameDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createGameStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}

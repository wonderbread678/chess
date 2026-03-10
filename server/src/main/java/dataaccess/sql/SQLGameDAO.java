package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureGameDatabase();
    }

    public GameData createGame(GameData gameData) throws DataAccessException {
        String sql = "INSERT INTO gameDataTable (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            stmt.setInt(1, gameData.gameID());
            stmt.setString(2, gameData.whiteUsername());
            stmt.setString(3, gameData.blackUsername());
            stmt.setString(4, gameData.gameName());
            stmt.setString(5, new Gson().toJson(gameData.game()));
            stmt.executeUpdate();

            return gameData;
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public HashMap<Integer, GameData> listGames() throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameDataTable";

        HashMap<Integer, GameData> gamesList = new HashMap<>();

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int gameID = rs.getInt(1);
                String whiteUsername = rs.getString(2);
                String blackUsername = rs.getString(3);
                String gameName = rs.getString(4);
                ChessGame game = new Gson().fromJson(rs.getString(5), ChessGame.class);

                gamesList.put(gameID, new GameData(gameID, whiteUsername, blackUsername, gameName, game));
            }
            return gamesList;
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameDataTable WHERE gameID = ?";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            return new GameData(rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    new Gson().fromJson(rs.getString(5), ChessGame.class));
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void updateGamePlayers(int gameID, String whiteUsername, String blackUsername) throws DataAccessException {
        String sql = "UPDATE gameDataTable" + " SET whiteUsername = ?, blackUsername = ?" + " WHERE gameID = ?";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            stmt.setString(1, whiteUsername);
            stmt.setString(2, blackUsername);
            stmt.setInt(3, gameID);
            int count = stmt.executeUpdate();
            if(count == 0){
                throw new DataAccessException("Error: No rows to update");
            }

            System.out.println("Updated game " + gameID + "\n");
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void deleteAllGames() throws DataAccessException {
        String sql = "DELETE FROM gameDataTable";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            int count = stmt.executeUpdate();
            System.out.printf("Deleted %d games\n", count);
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    private final String[] createGameStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameDataTable (
            `gameID` int NOT NULL,
            `whiteUsername` varchar(256) DEFAULT NULL,
            `blackUsername` varchar(256) DEFAULT NULL,
            `gameName` varchar(256) NOT NULL,
            `game` TEXT NOT NULL,
            PRIMARY KEY(`gameID`),
            INDEX(gameName)
            )
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

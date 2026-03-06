package dataaccess;

import model.AuthData;
import server.ResponseException;

import java.sql.*;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException{
        configureDatabase();
    }

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        return null;
    }

    public AuthData getAuth(String username) throws DataAccessException {
        return null;
    }

    public void deleteAuth(String username) throws DataAccessException {

    }

    public void deleteAllAuth() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authDAO (
            'authToken' varchar(256) NOT NULL,
            'username' varchar(256) NOT NULL
            PRIMARY KEY('authToken'),
            INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}

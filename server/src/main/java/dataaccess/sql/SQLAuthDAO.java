package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import java.sql.*;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureAuthDatabase();
    }

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        String sql = "INSERT INTO authDataTable (authToken, username) VALUES (?, ?)";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            stmt.setString(1, authData.authToken());
            stmt.setString(2, authData.username());
            stmt.executeUpdate();
            System.out.println("Inserted authData: " + authData + "\n");
            return authData;
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public AuthData getAuth(String username) throws DataAccessException {
        String sql = "SELECT authToken, username from authDataTable WHERE username = ?";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            return new AuthData(rs.getString("authToken"), rs.getString("username"));
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void deleteAuth(String username) throws DataAccessException {
        String sql = "DELETE FROM authDataTable WHERE username = ?";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            stmt.setString(1, username);
            int count = stmt.executeUpdate();
            System.out.printf("Deleted %d auth values\n", count);
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void deleteAllAuth() throws DataAccessException {
        String sql = "delete from authDataTable";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            int count = stmt.executeUpdate();
            System.out.printf("Deleted %d auth values\n", count);
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    private final String[] createAuthStatements = {
            """
            CREATE TABLE IF NOT EXISTS authDataTable (
                `authToken` varchar(256) NOT NULL,
                `username` varchar(256) NOT NULL,
                PRIMARY KEY(`authToken`),
                INDEX(username)
            )
            """
    };

    private void configureAuthDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createAuthStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}

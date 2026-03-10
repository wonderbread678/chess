package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureUserDatabase();
    }

    public UserData createUser(UserData userData) throws DataAccessException {
        String sql = "INSERT INTO userDataTable (username, password, email) VALUES (?, ?, ?)";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            stmt.setString(1, userData.username());
            stmt.setString(2, userData.password());
            stmt.setString(3, userData.email());
            stmt.executeUpdate();

            return userData;
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, password, email FROM userDataTable WHERE username = ?";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            UserData forDebug = new UserData(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3));

            return forDebug;
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void deleteAllUsers() throws DataAccessException {
        String sql = "DELETE FROM userDataTable";
        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)){
            int count = stmt.executeUpdate();
            System.out.printf("Deleted %d users\n", count);
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    void storeUserPassword(String username, String clearTextPassword) {
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());

        // write the hashed password in database along with the user's other information
        writeHashedPasswordToDatabase(username, hashedPassword);
    }

    boolean verifyUser(String username, String providedClearTextPassword) {
        // read the previously hashed password from the database
        var hashedPassword = readHashedPasswordFromDatabase(username);

        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

    private final String[] createUserStatements = {
            """
            CREATE TABLE IF NOT EXISTS userDataTable (
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY(`username`),
            INDEX(password)
            )
            """
    };

    private void configureUserDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createUserStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}

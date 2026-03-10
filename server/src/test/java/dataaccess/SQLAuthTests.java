package dataaccess;
import dataaccess.sql.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthTests {

    private SQLAuthDAO AUTH_DAO;

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        AUTH_DAO = new SQLAuthDAO();
        AUTH_DAO.deleteAllAuth();
    }

    @Test
    public void testCreateAuth() throws DataAccessException{
        AuthData testData = new AuthData("1", "test");
        AuthData result = AUTH_DAO.createAuth(testData);

        assertNotNull(result);
        assertEquals(testData, result);
    }

    @Test
    public void testCreateAuthInvalid() throws DataAccessException{
        AuthData testData = new AuthData("1", null);

        assertThrows(DataAccessException.class, () -> AUTH_DAO.createAuth(testData));
    }

    @Test
    public void testGetAuth() throws DataAccessException{
        AuthData testData = new AuthData("1", "test");
        AuthData comparison = AUTH_DAO.createAuth(testData);

        AuthData result = AUTH_DAO.getAuth("test");

        assertEquals(comparison, result);
    }

    @Test
    public void testGetAuthInvalidUsername() throws DataAccessException{

    }

    @Test
    public void testDeleteAuth() throws DataAccessException{

    }

    @Test
    public void testDeleteAuthTwice() throws DataAccessException{

    }

    @Test
    public void testDeleteAllAuth() throws DataAccessException{

    }
}

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

        assertNotNull(result);
        assertEquals(comparison, result);
    }

    @Test
    public void testGetAuthInvalidUsername() throws DataAccessException{
        assertThrows(DataAccessException.class, () -> AUTH_DAO.getAuth("test"));
    }

    @Test
    public void testDeleteAuth() throws DataAccessException{
        AuthData testData = new AuthData("1", "test");
        AUTH_DAO.createAuth(testData);

        AUTH_DAO.deleteAuth("test");
        assertThrows(DataAccessException.class, ()-> AUTH_DAO.getAuth("test"));
    }

    @Test
    public void testDeleteAuthTwice() throws DataAccessException{
        AuthData testData = new AuthData("1", "test");
        AUTH_DAO.createAuth(testData);

        AUTH_DAO.deleteAuth("test");
        assertThrows(DataAccessException.class, ()-> AUTH_DAO.deleteAuth("test"));
    }

    @Test
    public void testDeleteAllAuth() throws DataAccessException{
        AuthData testData1 = new AuthData("1", "test1");
        AuthData testData2 = new AuthData("2", "test2");
        AuthData testData3 = new AuthData("3", "test3");

        AUTH_DAO.createAuth(testData1);
        AUTH_DAO.createAuth(testData2);
        AUTH_DAO.createAuth(testData3);

        AUTH_DAO.deleteAllAuth();

        assertThrows(DataAccessException.class, ()-> AUTH_DAO.getAuth("test1"));
        assertThrows(DataAccessException.class, ()-> AUTH_DAO.getAuth("test2"));
        assertThrows(DataAccessException.class, ()-> AUTH_DAO.getAuth("test3"));
    }
}

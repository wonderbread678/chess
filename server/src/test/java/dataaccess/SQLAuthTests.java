package dataaccess;
import dataaccess.sql.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthTests {

    private SQLAuthDAO authDAO;

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        authDAO = new SQLAuthDAO();
        authDAO.deleteAllAuth();
    }

    @Test
    public void testCreateAuth() throws DataAccessException{
        AuthData testData = new AuthData("1", "test");
        AuthData result = authDAO.createAuth(testData);

        assertNotNull(result);
        assertEquals(testData, result);
    }

    @Test
    public void testCreateAuthInvalid() throws DataAccessException{
        AuthData testData = new AuthData("1", null);

        assertThrows(DataAccessException.class, () -> authDAO.createAuth(testData));
    }

    @Test
    public void testGetAuth() throws DataAccessException{
        AuthData testData = new AuthData("1", "test");
        AuthData comparison = authDAO.createAuth(testData);

        AuthData result = authDAO.getAuth("1");

        assertNotNull(result);
        assertEquals(comparison, result);
    }

    @Test
    public void testGetAuthInvalidUsername() throws DataAccessException{
        assertNull(authDAO.getAuth("1"));
    }

    @Test
    public void testDeleteAuth() throws DataAccessException{
        AuthData testData = new AuthData("1", "test");
        authDAO.createAuth(testData);

        authDAO.deleteAuth("1");
        assertNull(authDAO.getAuth("test"));
    }

    @Test
    public void testDeleteAuthTwice() throws DataAccessException{
        AuthData testData = new AuthData("1", "test");
        authDAO.createAuth(testData);

        authDAO.deleteAuth("1");
        assertThrows(DataAccessException.class, ()-> authDAO.deleteAuth("test"));
    }

    @Test
    public void testDeleteAllAuth() throws DataAccessException{
        AuthData testData1 = new AuthData("1", "test1");
        AuthData testData2 = new AuthData("2", "test2");
        AuthData testData3 = new AuthData("3", "test3");

        authDAO.createAuth(testData1);
        authDAO.createAuth(testData2);
        authDAO.createAuth(testData3);

        authDAO.deleteAllAuth();

        assertNull(authDAO.getAuth("1"));
        assertNull(authDAO.getAuth("2"));
        assertNull(authDAO.getAuth("3"));
    }
}

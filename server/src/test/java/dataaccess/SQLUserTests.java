package dataaccess;
import dataaccess.DataAccessException;
import dataaccess.sql.*;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserTests {
    
    private SQLUserDAO userDAO;

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        userDAO = new SQLUserDAO();
        userDAO.deleteAllUsers();
    }

    @Test
    public void testCreateUser() throws DataAccessException{
        UserData testUser = new UserData("test", "test", "test@test.com");
        UserData testData = userDAO.createUser(testUser);

        assertNotNull(testData.username());
        assertNotNull(testData.password());
        assertNotNull(testData.email());
        assertEquals(testUser, testData);
    }

    @Test
    public void testCreateUserNullFields() throws DataAccessException{
        UserData testBadUser = new UserData("test", null, "test@test.com");

        assertThrows(DataAccessException.class, () -> userDAO.createUser(testBadUser));
    }

    @Test
    public void testGetUser() throws DataAccessException{
        UserData testUser = new UserData("test", "test", "test@test.com");
        UserData comparison = userDAO.createUser(testUser);

        UserData testGetData = userDAO.getUser(testUser.username());

        assertNotNull(testGetData);
        assertNotNull(testGetData.username());
        assertNotNull(testGetData.password());
        assertNotNull(testGetData.email());
        assertEquals(comparison, testGetData);
    }

    @Test
    public void testGetUserDoesNotExist() throws DataAccessException{
        assertNull(userDAO.getUser("IDon'tExist12"));
    }

    @Test
    public void testDeleteAllUsers() throws DataAccessException{
        UserData testUser1 = new UserData("test1", "test1", "test1@test.com");
        UserData testUser2 = new UserData("test2", "test2", "test2@test.com");
        UserData testUser3 = new UserData("test3", "test3", "test3@test.com");

        userDAO.createUser(testUser1);
        userDAO.createUser(testUser2);
        userDAO.createUser(testUser3);

        userDAO.deleteAllUsers();

        assertNull(userDAO.getUser(testUser1.username()));
        assertNull(userDAO.getUser(testUser2.username()));
        assertNull(userDAO.getUser(testUser3.username()));
    }
}

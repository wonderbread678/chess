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
    
    private SQLUserDAO user_DAO;

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        user_DAO = new SQLUserDAO();
        user_DAO.deleteAllUsers();
    }

    @Test
    public void testCreateUser() throws DataAccessException{
        UserData testUser = new UserData("test", "test", "test@test.com");
        UserData testData = user_DAO.createUser(testUser);

        assertNotNull(testData.username());
        assertNotNull(testData.password());
        assertNotNull(testData.email());
        assertEquals(testUser, testData);
    }

    @Test
    public void testCreateUserNullFields() throws DataAccessException{
        UserData testBadUser = new UserData("test", null, "test@test.com");

        assertThrows(DataAccessException.class, () -> user_DAO.createUser(testBadUser));
    }

    @Test
    public void testGetUser() throws DataAccessException{
        UserData testUser = new UserData("test", "test", "test@test.com");
        UserData comparison = user_DAO.createUser(testUser);

        UserData testGetData = user_DAO.getUser(testUser.username());

        assertNotNull(testGetData);
        assertNotNull(testGetData.username());
        assertNotNull(testGetData.password());
        assertNotNull(testGetData.email());
        assertEquals(comparison, testGetData);
    }

    @Test
    public void testGetUserDoesNotExist() throws DataAccessException{
        assertNull(user_DAO.getUser("IDon'tExist12"));
    }

    @Test
    public void testDeleteAllUsers() throws DataAccessException{
        UserData testUser1 = new UserData("test1", "test1", "test1@test.com");
        UserData testUser2 = new UserData("test2", "test2", "test2@test.com");
        UserData testUser3 = new UserData("test3", "test3", "test3@test.com");

        user_DAO.createUser(testUser1);
        user_DAO.createUser(testUser2);
        user_DAO.createUser(testUser3);

        user_DAO.deleteAllUsers();

        assertNull(user_DAO.getUser(testUser1.username()));
        assertNull(user_DAO.getUser(testUser2.username()));
        assertNull(user_DAO.getUser(testUser3.username()));
    }
}

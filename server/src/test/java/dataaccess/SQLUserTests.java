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
    
    private SQLUserDAO USER_DAO;

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        USER_DAO = new SQLUserDAO();
        USER_DAO.deleteAllUsers();
    }

    @Test
    public void testCreateUser() throws DataAccessException{
        UserData testUser = new UserData("test", "test", "test@test.com");
        UserData testData = USER_DAO.createUser(testUser);

        assertNotNull(testData.username());
        assertNotNull(testData.password());
        assertNotNull(testData.email());
        assertEquals(testUser, testData);
    }

    @Test
    public void testCreateUserNullFields() throws DataAccessException{
        UserData testBadUser = new UserData("test", null, "test@test.com");

        assertThrows(DataAccessException.class, () -> USER_DAO.createUser(testBadUser));
    }

    @Test
    public void testGetUser() throws DataAccessException{
        UserData testUser = new UserData("test", "test", "test@test.com");
        UserData comparison = USER_DAO.createUser(testUser);

        UserData testGetData = USER_DAO.getUser(testUser.username());

        assertNotNull(testGetData);
        assertNotNull(testGetData.username());
        assertNotNull(testGetData.password());
        assertNotNull(testGetData.email());
        assertEquals(comparison, testGetData);
    }

    @Test
    public void testGetUserDoesNotExist() throws DataAccessException{
        assertThrows(DataAccessException.class, () -> USER_DAO.getUser("IDon'tExist12"));
    }

    @Test
    public void testDeleteAllUsers() throws DataAccessException{
        UserData testUser1 = new UserData("test1", "test1", "test1@test.com");
        UserData testUser2 = new UserData("test2", "test2", "test2@test.com");
        UserData testUser3 = new UserData("test3", "test3", "test3@test.com");

        USER_DAO.createUser(testUser1);
        USER_DAO.createUser(testUser2);
        USER_DAO.createUser(testUser3);

        USER_DAO.deleteAllUsers();

        assertThrows(DataAccessException.class, () -> USER_DAO.getUser(testUser1.username()));
        assertThrows(DataAccessException.class, () -> USER_DAO.getUser(testUser2.username()));
        assertThrows(DataAccessException.class, () -> USER_DAO.getUser(testUser3.username()));
    }
}

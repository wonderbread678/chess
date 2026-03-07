package dataaccess;
import dataaccess.DataAccessException;
import dataaccess.sql.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import model.*;
import service.AuthService;
import service.UserService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthTests {
    static final SQLAuthDAO AUTH_DAO = new SQLAuthDAO();
    static final AuthService SERVICE = new AuthService(AUTH_DAO);
    static final UserService USER_SERVICE = new UserService(AUTH_DAO, new SQLUserDAO());

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws ResponseException{
        SERVICE.deleteAllAuth();
    }

    @Test
    public void testIsAuth() throws ResponseException{
        AuthData testUser = USER_SERVICE.createUser("test", "test", "test");
    }
}

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
//    static final SQLAuthDAO AUTH_DAO;
//    static final AuthService SERVICE;
//    static final UserService USER_SERVICE;
//    static final Connection connection;

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws ResponseException{

    }

    @Test
    public void testIsAuth() throws ResponseException{

    }

    @Test
    public void testUsernameWithNoAuth() throws ResponseException{

    }

    @Test
    public void testDeleteAllAuth() throws ResponseException{

    }
}

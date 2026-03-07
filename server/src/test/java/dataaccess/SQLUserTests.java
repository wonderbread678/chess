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

public class SQLUserTests {

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws ResponseException{

    }

    @Test
    public void testCreateUser() throws ResponseException{

    }

    @Test
    public void testCreateUserNullFields() throws ResponseException{

    }

    @Test
    public void testGetUser() throws ResponseException{

    }

    @Test
    public void testGetUserDoesNotExist() throws ResponseException{

    }

    @Test
    public void testDeleteAllUsers() throws ResponseException{

    }
}

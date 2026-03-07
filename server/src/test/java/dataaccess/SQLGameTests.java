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
import service.GameService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameTests {

    @BeforeAll
    static void connectionSetUp() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws ResponseException{

    }

    @Test
    public void testCreateGame() throws ResponseException{

    }

    @Test
    public void testCreateGameNullFields() throws ResponseException{

    }

    @Test
    public void testListGames() throws ResponseException{

    }

    @Test
    public void testListGamesNoList() throws ResponseException{

    }

    @Test
    public void testGetGame() throws ResponseException{

    }

    @Test
    public void testGetGameDoesNotExist() throws ResponseException{

    }

    @Test
    public void testUpdatePlayer() throws ResponseException{

    }

    @Test
    public void testUpdatePlayerNoGame() throws ResponseException{

    }

    @Test
    public void testDeleteAllGames() throws ResponseException{

    }
}

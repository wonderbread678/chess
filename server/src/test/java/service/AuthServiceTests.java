package service;
import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import model.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {

    static final MemoryAuthDAO AUTH_DAO = new MemoryAuthDAO();
    static final AuthService SERVICE = new AuthService(AUTH_DAO);
    static final UserService USER_SERVICE = new UserService(AUTH_DAO, new MemoryUserDAO());

    @BeforeEach
    public void setup() throws ResponseException{
        SERVICE.deleteAllAuth();
    }

    @Test
    public void testIsAuth() throws ResponseException{
        AuthData testUser = USER_SERVICE.createUser("test", "test", "test");

        assertTrue(SERVICE.isAuth(testUser.authToken()));
    }

    @Test
    public void testIsAuthInvalid() throws ResponseException{
        assertFalse(SERVICE.isAuth("fakeAuth"));
    }

    @Test
    public void testDeleteAllAuth() throws ResponseException{
        try{
            AUTH_DAO.createAuth(new AuthData("auth1", "user1"));
            AUTH_DAO.createAuth(new AuthData("auth2", "user2"));
            AUTH_DAO.createAuth(new AuthData("auth3", "user3"));

            SERVICE.deleteAllAuth();

            assertNull(AUTH_DAO.getAuth("auth1"));
            assertNull(AUTH_DAO.getAuth("auth2"));
            assertNull(AUTH_DAO.getAuth("auth3"));
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }
}

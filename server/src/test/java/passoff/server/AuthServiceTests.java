package passoff.server;
import dataaccess.DataAccessException;
import dataaccess.*;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import service.AuthService;
import service.UserService;
import model.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {

    static final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static final AuthService service = new AuthService(authDAO);
    static final UserService userService = new UserService(authDAO, new MemoryUserDAO());

    @BeforeEach
    public void setup() throws ResponseException{
        service.deleteAllAuth();
    }

    @Test
    public void testIsAuth() throws ResponseException{
        AuthData testUser = userService.createUser("test", "test", "test");

        assertTrue(service.isAuth(testUser.authToken()));
    }

    @Test
    public void testIsAuthInvalid() throws ResponseException{
        assertFalse(service.isAuth("fakeAuth"));
    }

    @Test
    public void testDeleteAllAuth() throws ResponseException{
        try{
            authDAO.createAuth(new AuthData("auth1", "user1"));
            authDAO.createAuth(new AuthData("auth2", "user2"));
            authDAO.createAuth(new AuthData("auth3", "user3"));

            service.deleteAllAuth();

            assertNull(authDAO.getAuth("auth1"));
            assertNull(authDAO.getAuth("auth2"));
            assertNull(authDAO.getAuth("auth3"));
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }
}

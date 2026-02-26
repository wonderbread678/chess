package passoff.server;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import service.UserService;
import model.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    static final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static final MemoryUserDAO userDAO = new MemoryUserDAO();
    static final UserService service = new UserService(authDAO, userDAO);

    @BeforeEach
    public void setup() throws ResponseException {
        service.deleteAllUsers();
    }

    @Test
    public void testValidRegister() throws ResponseException{
        AuthData user = service.createUser("newUser", "password", "bballs@booger.com");
        AuthData comparison = new AuthData(user.authToken(), "newUser");

        assertEquals(comparison, user);
        try{
            assertNotNull(userDAO.getUser("newUser"));
            assertNotNull(authDAO.getAuth(user.authToken()));
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }

    @Test
    public void testRegisterTakenUsername() throws ResponseException{
        AuthData user = service.createUser("newUser", "password", "bballs@booger.com");
        assertThrows(ResponseException.class, () ->
                service.createUser("newUser", "newPassword", "newEmail@email.com"));
    }

    @Test
    public void testValidLogin() throws ResponseException{
        try{
            AuthData newUser= service.createUser("newUser", "password", "bballs@booger.com");
            service.logout(newUser.authToken());
            AuthData loginTry = service.createAuth("newUser", "password");

            AuthData comparison = new AuthData(loginTry.authToken(), "newUser");

            assertNotNull(authDAO.getAuth(loginTry.authToken()));
            assertEquals(loginTry, comparison);
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }

    @Test
    public void testLoginBadPassword() throws ResponseException{

    }

    @Test
    public void testValidLogout() throws ResponseException{

    }

    @Test
    public void testLogoutInvalidToken() throws ResponseException{

    }

    @Test
    public void testDeleteAllUsers() throws ResponseException{
        try{
            userDAO.createUser(new UserData("user1", "password1", "email1@email.com"));
            userDAO.createUser(new UserData("user2", "password2", "email2@email.com"));
            userDAO.createUser(new UserData("user3", "password3", "email3@email.com"));

            service.deleteAllUsers();
            assertNull(userDAO.getUser("user1"));
            assertNull(userDAO.getUser("user2"));
            assertNull(userDAO.getUser("user3"));
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }
}

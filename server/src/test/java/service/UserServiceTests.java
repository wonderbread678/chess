package service;
import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import model.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    static final MemoryAuthDAO AUTH_DAO = new MemoryAuthDAO();
    static final MemoryUserDAO USER_DAO = new MemoryUserDAO();
    static final UserService SERVICE = new UserService(AUTH_DAO, USER_DAO);

    @BeforeEach
    public void setup() throws ResponseException {
        SERVICE.deleteAllUsers();
    }

    @Test
    public void testValidRegister() throws ResponseException{
        AuthData user = SERVICE.createUser("newUser", "password", "bballs@booger.com");
        AuthData comparison = new AuthData(user.authToken(), "newUser");

        assertEquals(comparison, user);
        try{
            assertNotNull(USER_DAO.getUser("newUser"));
            assertNotNull(AUTH_DAO.getAuth(user.authToken()));
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }

    @Test
    public void testRegisterTakenUsername() throws ResponseException{
        AuthData user = SERVICE.createUser("newUser", "password", "bballs@booger.com");
        assertThrows(ResponseException.class, () ->
                SERVICE.createUser("newUser", "newPassword", "newEmail@email.com"));
    }

    @Test
    public void testValidLogin() throws ResponseException{
        try{
            AuthData newUser= SERVICE.createUser("newUser", "password", "bballs@booger.com");
            SERVICE.logout(newUser.authToken());
            AuthData loginTry = SERVICE.createAuth("newUser", "password");

            AuthData comparison = new AuthData(loginTry.authToken(), "newUser");

            assertNotNull(AUTH_DAO.getAuth(loginTry.authToken()));
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
            USER_DAO.createUser(new UserData("user1", "password1", "email1@email.com"));
            USER_DAO.createUser(new UserData("user2", "password2", "email2@email.com"));
            USER_DAO.createUser(new UserData("user3", "password3", "email3@email.com"));

            SERVICE.deleteAllUsers();
            assertNull(USER_DAO.getUser("user1"));
            assertNull(USER_DAO.getUser("user2"));
            assertNull(USER_DAO.getUser("user3"));
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }
}

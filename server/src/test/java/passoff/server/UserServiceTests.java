package passoff.server;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    static final UserService service = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());

    @BeforeEach
    public void setup(){
        service.
    }

    @Test
    public void testValidRegister(){

    }

    @Test
    public void testRegisterMissingParameters(){

    }

    @Test
    public void testValidLogin(){

    }

    @Test
    public void testLoginBadPassword(){

    }

    @Test
    public void testValidLogout(){

    }

    @Test
    public void testLogoutInvalidToken(){

    }
}

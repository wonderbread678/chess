package passoff.server;
import dataaccess.DataAccessException;
import dataaccess.*;
import dataaccess.Memory.MemoryAuthDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import service.AuthService;
import service.GameService;
import service.UserService;
import model.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {

    static final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static final AuthService service = new AuthService(authDAO);

    @BeforeEach
    public void setup() throws ResponseException{
        service.deleteAllAuth();
    }

    @Test
    public void testIsAuth() throws ResponseException{

    }

    @Test
    public void testIsAuthInvalid() throws ResponseException{

    }

    @Test
    public void testDeleteAllGames() throws ResponseException{

    }
}

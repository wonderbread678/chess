package passoff.server;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.*;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryGameDAO;
import dataaccess.Memory.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import service.GameService;
import service.UserService;
import model.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    static final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static final MemoryGameDAO gameDAO = new MemoryGameDAO();
    static final MemoryUserDAO userDAO = new MemoryUserDAO();
    static final GameService service = new GameService(gameDAO, authDAO);

    @BeforeEach
    public void setup() throws ResponseException{
        service.deleteAllGames();
    }

    @Test
    public void testGetGameList() throws ResponseException{
        try{
            gameDAO.createGame(new GameData(1, "white1", "black1", "game1", new ChessGame()));
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }


    }

    @Test
    public void testUnauthorizedGetGamesList() throws ResponseException{

    }

    @Test
    public void testCreateGame() throws ResponseException{

    }

    @Test
    public void testCreateGameNoName() throws ResponseException{

    }

    @Test
    public void testJoinGame() throws ResponseException{

    }

    @Test
    public void testUnauthorizedJoinGame() throws ResponseException{

    }

    @Test
    public void testDeleteAllGames() throws ResponseException{

    }
}

package service;
import dataaccess.*;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryGameDAO;
import dataaccess.Memory.MemoryUserDAO;

public class GameService {

    private final MemoryGameDAO gameDAO;
    private final MemoryUserDAO userDAO;
    private final MemoryAuthDAO authDAO;

    public GameService(MemoryGameDAO gameDAO, MemoryUserDAO userDAO, MemoryAuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

}

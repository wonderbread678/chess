package service;

import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryUserDAO;
import dataaccess.*;

public class AuthService {

    private final MemoryAuthDAO authDAO;

    public AuthService(MemoryAuthDAO authDAO){
        this.authDAO = authDAO;
    }

    public boolean isAuth(String authToken) throws DataAccessException{
        return authDAO.getAuth(authToken) != null;
    }
}

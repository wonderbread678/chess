package service;

import dataaccess.*;
import dataaccess.sql.SQLAuthDAO;
import server.ResponseException;

public class AuthService {

    private final SQLAuthDAO authDAO;

    public AuthService(SQLAuthDAO authDAO){
        this.authDAO = authDAO;
    }

    public boolean isAuth(String authToken) throws ResponseException {
        try{
            return authDAO.getAuth(authToken) != null;
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void deleteAllAuth() throws ResponseException{
        try{
            authDAO.deleteAllAuth();
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }
}

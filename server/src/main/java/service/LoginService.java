package service;

import dataaccess.*;
import model.*;

import java.util.Objects;
import java.util.UUID;

public class LoginService {
    private final MemoryAuthDAO authDAO;
    private final MemoryUserDAO userDAO;

    public LoginService(MemoryAuthDAO authData, MemoryUserDAO userData){
        this.authDAO = authData;
        this.userDAO = userData;
    }

    public UserData getUser(String username) throws DataAccessException{
        if
        return userDAO.getUser(username);
    }

    public AuthData createAuth(String username, String password) throws DataAccessException{
        if(getUser(username) != null){
            if(isLoginInfoCorrect(username, password)){

            }
        }
    }

    public boolean isLoginInfoCorrect(String username, String password){
        UserData userData = getUser(username);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LoginService that)) {
            return false;
        }
        return Objects.equals(authDAO, that.authDAO) && Objects.equals(userDAO, that.userDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authDAO, userDAO);
    }
}

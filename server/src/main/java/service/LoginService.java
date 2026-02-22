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
        if(userDAO.getUser(username) == null){
            throw new DataAccessException("[400] Bad Request: Username not found");
        }
        return userDAO.getUser(username);
    }

    public AuthData createAuth(String username, String password) throws DataAccessException{
        if(getUser(username) == null){
            throw new DataAccessException("[400] Bad Request: Username not found");
        }
        if(!isLoginInfoCorrect(username, password)){
            throw new DataAccessException("[401] Unauthorized: Incorrect password");
        }
        AuthData newAuth = new AuthData(username, generateToken());
        return authDAO.createAuth(newAuth);
    }

    public boolean isLoginInfoCorrect(String username, String password) throws DataAccessException{
        UserData user = userDAO.getUser(username);
        if(username.equals(user.username()) && password.equals(user.password())){
            return true;
        }
        return false;
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

}

package service;

import dataaccess.*;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryUserDAO;
import model.*;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final MemoryAuthDAO authDAO;
    private final MemoryUserDAO userDAO;

    public UserService(MemoryAuthDAO authData, MemoryUserDAO userData){
        this.authDAO = authData;
        this.userDAO = userData;
    }

    public AuthData createUser(String username, String password, String email) throws DataAccessException{
        if(userDAO.getUser(username) !=  null){
            throw new DataAccessException("Username already taken");
        }
        userDAO.createUser(new UserData(username, password, email));
        return createAuth(username, password);
    }

    public AuthData createAuth(String username, String password) throws DataAccessException{
        UserData user = userDAO.getUser(username);
        if(user == null){
            throw new DataAccessException("[400] Bad Request: Username not found");
        }
        if(!isLoginInfoCorrect(password, user)){
            throw new DataAccessException("[401] Unauthorized: Incorrect password");
        }
        AuthData newAuth = new AuthData(username, generateToken());
        return authDAO.createAuth(newAuth);
    }

    public void logout(String authToken) throws DataAccessException{
        authDAO.deleteAuth(authToken);
    }

    public boolean isLoginInfoCorrect(String password, UserData user) throws DataAccessException{
        return password.equals(user.password());
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserService that)) {
            return false;
        }
        return Objects.equals(authDAO, that.authDAO) && Objects.equals(userDAO, that.userDAO);
    }

}

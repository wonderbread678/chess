package service;

import dataaccess.*;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryUserDAO;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLUserDAO;
import model.*;
import server.ResponseException;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final SQLAuthDAO authDAO;
    private final SQLUserDAO userDAO;

    public UserService(SQLAuthDAO authData, SQLUserDAO userData){
        this.authDAO = authData;
        this.userDAO = userData;
    }

    public AuthData createUser(String username, String password, String email) throws ResponseException {
        try {
            if (userDAO.getUser(username) != null) {
                throw new ResponseException(403, "Error: Username already taken");
            }
        userDAO.createUser(new UserData(username, password, email));
        return createAuth(username, password);
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public AuthData createAuth(String username, String password) throws ResponseException{
        try{
            UserData user = userDAO.getUser(username);
            if(user == null){
                throw new ResponseException(401, "Error: Unauthorized");
            }
            if(!isLoginInfoCorrect(password, user)){
                throw new ResponseException(401, "Error: Unauthorized");
            }
            AuthData newAuth = new AuthData(generateToken(), username);
            return authDAO.createAuth(newAuth);
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void logout(String authToken) throws ResponseException{
        try{
            if(authDAO.getAuth(authToken) == null){
                throw new ResponseException(401, "Error: User already logged out");
            }
            authDAO.deleteAuth(authToken);
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public boolean isLoginInfoCorrect(String password, UserData user){
        return password.equals(user.password());
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    //    void storeUserPassword(String username, String clearTextPassword) {
//        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
//
//        // write the hashed password in database along with the user's other information
//        writeHashedPasswordToDatabase(username, hashedPassword);
//    }
//
//    boolean verifyUser(String username, String providedClearTextPassword) {
//        // read the previously hashed password from the database
//        var hashedPassword = readHashedPasswordFromDatabase(username);
//
//        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
//    }

    public void deleteAllUsers() throws ResponseException{
        try{
            userDAO.deleteAllUsers();
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserService that)) {
            return false;
        }
        return Objects.equals(authDAO, that.authDAO) && Objects.equals(userDAO, that.userDAO);
    }

}

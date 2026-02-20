package dataaccess;

import dataaccess.DataAccessException;
import model.*;

public interface UserDAO {

    UserData createUser(String username, String password, String email) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;
}

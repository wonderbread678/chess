package dataaccess;

import dataaccess.DataAccessException;
import model.*;

public interface UserDAO {

    UserData createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;
}

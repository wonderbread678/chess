package dataaccess;

import dataaccess.DataAccessException;
import model.*;

public interface AuthDAO {

    AuthData createAuth(String username) throws DataAccessException;

    AuthData getAuth(String username) throws DataAccessException;

    void deleteAuth(String username) throws DataAccessException;

    void deleteAllAuth() throws DataAccessException;
}

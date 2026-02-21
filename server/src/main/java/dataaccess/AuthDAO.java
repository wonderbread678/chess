package dataaccess;

import dataaccess.DataAccessException;
import model.*;

public interface AuthDAO {

    AuthData createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String username) throws DataAccessException;

    void deleteAuth(String username) throws DataAccessException;

    void deleteAllAuth() throws DataAccessException;
}

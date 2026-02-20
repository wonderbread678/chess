package dataaccess;

import dataaccess.DataAccessException;
import model.*;

public interface AuthDAO {

    AuthData createAuth(String username) throws DataAccessException;

    AuthData getAuth() throws DataAccessException;

    void deleteAuth() throws DataAccessException;
}

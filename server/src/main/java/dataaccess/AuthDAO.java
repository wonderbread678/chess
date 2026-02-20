package dataaccess;

import dataaccess.DataAccessException;
import model.*;

public interface AuthDAO {

    AuthData createAuth() throws DataAccessException;

    AuthData getAuth() throws DataAccessException;

    void deleteAuth() throws DataAccessException;
}

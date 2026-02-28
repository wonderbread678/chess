package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.*;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authDataList = new HashMap<>();

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        authDataList.put(authData.authToken(), authData);
        return authData;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return authDataList.get(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        authDataList.remove(authToken);
    }

    public void deleteAllAuth() throws DataAccessException{
        authDataList.clear();
    }

}

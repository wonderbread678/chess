package dataaccess;

import model.*;
import java.util.UUID;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authDataList = new HashMap<>();

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        authDataList.put(authData.username(), authData);
        return authData;
    }

    public AuthData getAuth(String username) throws DataAccessException {
        return authDataList.get(username);
    }

    public void deleteAuth(String username) throws DataAccessException {
        authDataList.remove(username);
    }

    public void deleteAllAuth() throws DataAccessException{
        authDataList.clear();
    }

}

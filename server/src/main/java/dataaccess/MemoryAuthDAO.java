package dataaccess;

import model.*;
import java.util.UUID;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authDataList = new HashMap<>();

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        AuthData auth = new AuthData(authData.username(), authData.authToken());

        authDataList.put(authData.username(), auth);
        return auth;
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

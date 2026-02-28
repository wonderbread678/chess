package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> allUsers = new HashMap<>();

    public UserData createUser(UserData userData) throws DataAccessException {
        allUsers.put(userData.username(), userData);
        return userData;
    }

    public UserData getUser(String username) throws DataAccessException {
        return allUsers.get(username);
    }

    public void deleteAllUsers() throws DataAccessException {
        allUsers.clear();
    }
}

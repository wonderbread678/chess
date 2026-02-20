package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> allUsers = new HashMap<>();

    public UserData createUser(String username, String password, String email) throws DataAccessException {
        UserData user = new UserData(username, password, email);
        allUsers.put(username, user);
        return user;
    }

    public UserData getUser(String username) throws DataAccessException {
        return allUsers.get(username);
    }

    public void deleteAllUsers() throws DataAccessException {
        allUsers.clear();
    }
}

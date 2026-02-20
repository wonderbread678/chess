package dataaccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> allUsers = new HashMap<>();

    public UserData createUser(UserData userData) throws DataAccessException {
        UserData user = new UserData(userData.username(), userData.password(), userData.email());
        allUsers.put(user.username(), user);
        return user;
    }

    public UserData getUser(String username) throws DataAccessException {
        return allUsers.get(username);
    }

    public void deleteAllUsers() throws DataAccessException {
        allUsers.clear();
    }
}

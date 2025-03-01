package dataaccess;
import model.UserRecord;

import java.util.HashMap;

public class UserDAO implements UserDAOInterface {

    private final HashMap<String, UserRecord> users = new HashMap<>();

    @Override
    public void registerUser(UserRecord user) {
        users.put(user.username(), user);
    }

    @Override
    public UserRecord getUser(String username) {
        return users.get(username);
    }

    @Override
    public boolean doesUserExist(String username) {
        return users.containsKey(username);
    }

    @Override
    public void clear() {
        users.clear();
    }
}

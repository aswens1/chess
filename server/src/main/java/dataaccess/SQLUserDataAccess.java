package dataaccess;

import model.UserRecord;

public class SQLUserDataAccess implements  UserDAOInterface{
    @Override
    public void registerUser(UserRecord user) {

    }

    @Override
    public UserRecord getUser(String username) {
        return null;
    }

    @Override
    public boolean doesUserExist(String username) {
        return false;
    }

    @Override
    public void clear() {

    }
}

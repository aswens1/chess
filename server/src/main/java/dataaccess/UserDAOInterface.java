package dataaccess;
import model.UserRecord;

public interface UserDAOInterface {

    void registerUser(UserRecord user);
    UserRecord getUser(String username);
    boolean doesUserExist(String username);
    void clear();


}

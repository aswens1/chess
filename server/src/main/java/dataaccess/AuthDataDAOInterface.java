package dataaccess;
import model.*;

public interface AuthDataDAOInterface {
    // create a new authorisation
    AuthDataRecord createAuthData(UserRecord user);

    // retrieve an authorisation given an authToken
    AuthDataRecord getAuthData(String authToken);

    // delete an authorisation so its no longer valid
    boolean deleteAuthData(String authToken);

    // clear all stored auth data
    void clear();
}

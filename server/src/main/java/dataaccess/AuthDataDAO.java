package dataaccess;
import model.AuthDataRecord;
import model.UserRecord;

import java.util.UUID;
import java.util.HashMap;

public class AuthDataDAO implements AuthDataDAOInterface {

    private final HashMap<String, AuthDataRecord> authentication = new HashMap<>();

    @Override
    public AuthDataRecord createAuthData(UserRecord user) {
        String authToken = UUID.randomUUID().toString();

        AuthDataRecord authData = new AuthDataRecord(authToken, user.username());
        authentication.put(authToken, authData);

        return authData;
    }

    @Override
    public AuthDataRecord getAuthData(String authToken) {
        return authentication.get(authToken);
    }

    @Override
    public boolean deleteAuthData(String authToken) {
        Object authTokenRemoved = authentication.remove(authToken);
        return authTokenRemoved != null;
    }

    @Override
    public void clear() {
        authentication.clear();
    }
}

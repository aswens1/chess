package dataaccess;

import exception.ResponseException;
import model.UserRecord;
import java.sql.SQLException;

public class SQLUserDataAccess implements  UserDAOInterface {

//    private void configureDatabase() throws ResponseException, DataAccessException {
//        DatabaseManager.createDatabase();
//        try (var conn = DatabaseManager.getConnection()) {
//            for (var statement : createStatements) {
//                try (var preparedStatement = conn.prepareStatement(statement)) {
//                    preparedStatement.executeUpdate();
//                }
//            }
//        } catch (Exception ex) {
//            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
//        }
//    }

    public SQLUserDataAccess() throws ResponseException, DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  UserData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        ConfigureDatabase.ConfigureDatabase(createStatements);
    }


    @Override
    public void registerUser(UserRecord user) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "INSTERT INTO UserData (username, password, email) VALUES (?, ?, ?)";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setString(1, user.username());
                ps.setString(2, user.password());
                ps.setString(3, user.email());
            }
        } catch (SQLException e) {
        throw new ResponseException(500, "UserData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserRecord getUser(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT username, password, email FROM UserData WHERE username=?";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserRecord(username, rs.getString("password"), rs.getString("email"));
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
        throw new ResponseException(500, "UserData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean doesUserExist(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String doesUserExist = "SELECT COUNT(*) FROM UserData WHERE username=?";
            try (var checkPs = conn.prepareStatement(doesUserExist)) {
                try (var rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "UserData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM UserData";
            try (var ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "UserData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


    }
}

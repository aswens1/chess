package dataaccess;

import exception.ResponseException;
import model.UserRecord;
import java.sql.SQLException;

public class SQLUserDataAccess implements  UserDAOInterface {

    public SQLUserDataAccess() throws ResponseException, DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS userdata (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        ConfigureDatabase.ConfigureDatabase(createStatements);
    }


    @Override
    public void registerUser(UserRecord user) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setString(1, user.username());
                ps.setString(2, user.password());
                ps.setString(3, user.email());
                ps.executeUpdate();
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
            String sql = "SELECT username, password, email FROM userdata WHERE username=?";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserRecord(username, rs.getString("password"), rs.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
        throw new ResponseException(500, "UserData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean doesUserExist(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String doesUserExist = "SELECT COUNT(*) FROM userdata WHERE username=?";
            try (var checkPs = conn.prepareStatement(doesUserExist)) {
                try (var rs = checkPs.executeQuery()) {
                    return rs.next();
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
            String sql = "DELETE FROM userdata";
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

package dataaccess;

import exception.ResponseException;
import model.AuthDataRecord;
import model.UserRecord;

import java.sql.*;

public class SQLAuthDataAccess implements AuthDataDAOInterface {

    public SQLAuthDataAccess() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  AuthData (
              `authToken` varchar(256) NOT NULL,
              `userName` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @Override
    public AuthDataRecord createAuthData(UserRecord user) throws ResponseException {
        String authToken = java.util.UUID.randomUUID().toString();
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO AuthData (authToken, userName) VALUES (?, ?)";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setString(1, authToken);
                ps.setString(2, user.username());
                ps.executeUpdate();

                return new AuthDataRecord(authToken, user.username());
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "AuthData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthDataRecord getAuthData(String authToken) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT authToken, userName FROM AuthToken WHERE authToken=?";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery(sql)) {
                    if (rs.next()) {
                        return new AuthDataRecord(authToken, rs.getString("userName"));
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "AuthData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteAuthData(String authToken) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM AuthData WHERE authToken = ?";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setString(1, authToken);
                int rowsDeleted = ps.executeUpdate();
                return rowsDeleted == 1;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "AuthData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM AuthData";
            try (var ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "AuthData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

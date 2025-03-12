package dataaccess;

import chess.ChessGame;

import com.google.gson.Gson;
import exception.ResponseException;
import model.CondensedGameData;
import model.GameDataRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDataAccess implements GameDAOInterface{

    public SQLGameDataAccess() throws ResponseException, DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS gamedata (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `white_user` varchar(256) NOT NULL,
              `black_user` varchar(256) NOT NULL,
              `game_name` varchar(256) NOT NULL,
              `chess_game` LONGTEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        ConfigureDatabase.configureDatabase(createStatements);
    }

    @Override
    public List<CondensedGameData> listGames() {

        List<CondensedGameData> games = new ArrayList<>();

        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT gameID, white_user, black_user, game_name FROM gamedata";
            try (var ps = conn.prepareStatement(sql)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(new CondensedGameData(rs.getInt("gameID"),
                                rs.getString("white_user"), rs.getString("black_user"),
                                rs.getString("game_name")));
                    }
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "GameData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return games;
    }

    @Override
    public int createGame(String gameName) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO gamedata (white_user, black_user, game_name, chess_game) VALUES (?,?,?,?)";
            try (var ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "");
                ps.setString(2, "");
                ps.setString(3, gameName);
                ps.setString(4, new Gson().toJson(new ChessGame()));
                ps.executeUpdate();

                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "GameData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    @Override
    public GameDataRecord getGame(Integer gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT gameID, white_user, black_user, game_name, chess_game FROM gamedata WHERE gameID=?";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameDataRecord(rs.getInt("gameID"),
                                rs.getString("white_user"), rs.getString("black_user"),
                                rs.getString("game_name"),
                                new Gson().fromJson(rs.getString("chess_game"), ChessGame.class));
                    }
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "GameData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateGame(Integer id, String username, ChessGame.TeamColor playerColour) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql;

            if (playerColour == ChessGame.TeamColor.WHITE) {
                sql = "UPDATE gamedata SET white_user =? WHERE gameID=?";
            } else if (playerColour == ChessGame.TeamColor.BLACK) {
                sql = "UPDATE gamedata SET black_user =? WHERE gameID=?";
            } else {
                throw new ResponseException(400, "Error: bad request");
            }

            try (var ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setInt(2, id);

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "GameData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinGame(ChessGame.TeamColor playerColor, Integer gameID, String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT white_user, black_user FROM gamedata WHERE gameID=?";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()){
                        String white = rs.getString("white_user");
                        String black = rs.getString("black_user");


                        if (playerColor == ChessGame.TeamColor.BLACK && !black.isEmpty()) {
                            throw new ResponseException(403, "Error: already taken");
                        } else if (playerColor == ChessGame.TeamColor.WHITE && !white.isEmpty()) {
                            throw new ResponseException(403, "Error: already taken");
                        }

                        if (playerColor == ChessGame.TeamColor.BLACK || playerColor == ChessGame.TeamColor.WHITE) {
                            updateGame(gameID, username, playerColor);
                        } else {
                            throw new ResponseException(400, "Error: bad request");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "GameData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM gamedata";
            try (var ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "GameData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

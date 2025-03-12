package dataaccess;

import chess.ChessGame;

import com.google.gson.Gson;
import exception.ResponseException;
import model.CondensedGameData;
import model.GameDataRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SQLGameDataAccess implements GameDAOInterface{

    private int nextGame = 0;


    public SQLGameDataAccess() throws ResponseException, DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS gamedata (
              `gameID` int NOT NULL,
              `white_user` varchar(256) NOT NULL,
              `black_user` varchar(256) NOT NULL,
              `game_name` varchar(256) NOT NULL,
              `chess_game` varchar(256) NOT NULL,
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
                try (var rs = ps.executeQuery(sql)) {
                    while (rs.next()) {
                        games.add(new CondensedGameData(Integer.parseInt(rs.getString("gameID")),
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
        String formattedGameID = String.format("%04d", nextGame);
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO gamedata (gameID, white_user, black_user, game_name, chess_game)";
            try (var ps = conn.prepareStatement(sql)) {
                ps.setString(1, formattedGameID);
                ps.setString(2, null);
                ps.setString(3, null);
                ps.setString(4, gameName);
                ps.setString(5, new Gson().toJson(new ChessGame()));
                ps.executeUpdate();
                nextGame++;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, "GameData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return Integer.parseInt(formattedGameID);
    }

    @Override
    public GameDataRecord getGame(Integer gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT gameID, white_user, black_user, game_name, chess_game FROM gamedata WHERE gameID=?";
            try (var ps = conn.prepareStatement(sql)) {
                try (var rs = ps.executeQuery(sql)) {
                    if (rs.next()) {
                        return new GameDataRecord(Integer.parseInt(rs.getString("gameID")),
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
    public void updateGame(Integer gameID, String username, ChessGame.TeamColor playerColour) {
        try (var conn = DatabaseManager.getConnection()) {

        } catch (SQLException e) {
            throw new ResponseException(500, "GameData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void joinGame(ChessGame.TeamColor playerColour, Integer gameID, String username) {
        try (var conn = DatabaseManager.getConnection()) {

        } catch (SQLException e) {
            throw new ResponseException(500, "GameData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {

        } catch (SQLException e) {
            throw new ResponseException(500, "GameData Database Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
}

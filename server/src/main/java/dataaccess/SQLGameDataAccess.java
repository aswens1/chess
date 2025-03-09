package dataaccess;

import chess.ChessGame;
import model.CondensedGameData;
import model.GameDataRecord;

import java.util.List;

public class SQLGameDataAccess implements GameDAOInterface{
    @Override
    public List<CondensedGameData> listGames() {
        return List.of();
    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameDataRecord getGame(Integer gameID) {
        return null;
    }

    @Override
    public void updateGame(Integer gameID, String username, ChessGame.TeamColor playerColour) {

    }

    @Override
    public void joinGame(ChessGame.TeamColor playerColour, Integer gameID, String username) {

    }

    @Override
    public void clear() {

    }
}

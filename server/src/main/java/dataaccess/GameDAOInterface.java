package dataaccess;
import chess.*;
import model.GameDataRecord;

import java.util.HashMap;

public interface GameDAOInterface {
    HashMap<Integer, GameDataRecord> listGames();
    int createGame(String gameName);
    GameDataRecord getGame(Integer gameID);
    void updateGame(Integer gameID, String username, ChessGame.TeamColor playerColour);
    void joinGame(ChessGame.TeamColor playerColour, Integer gameID);
    void clear();
}

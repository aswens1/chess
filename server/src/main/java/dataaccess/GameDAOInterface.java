package dataaccess;
import chess.*;
import model.CondensedGameData;
import model.GameDataRecord;

import java.util.List;

public interface GameDAOInterface {
    List<CondensedGameData> listGames();
    int createGame(String gameName);
    GameDataRecord getGame(Integer gameID);
    void updateGamePlayer(Integer gameID, String username, ChessGame.TeamColor playerColour, ChessGame game);
    void updateGameState(Integer gameID, String username, ChessGame.TeamColor playerColour, ChessGame game);
    ChessGame joinGame(ChessGame.TeamColor playerColour, Integer gameID, String username);
    void clear();
}

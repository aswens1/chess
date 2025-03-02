package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameDataRecord;
import model.UserRecord;

import java.util.HashMap;
import java.util.Random;

public class GameDAO implements GameDAOInterface {

    private final HashMap<Integer, GameDataRecord> gameDataMap = new HashMap<>();
    UserRecord userRecord;

    @Override
    public HashMap<Integer, GameDataRecord> listGames() {
        return gameDataMap;
    }

    @Override
    public int createGame(String gameName) {
        Random generateGameID = new Random();
        int gameID = 1000 + generateGameID.nextInt(9000);

        GameDataRecord newGame = new GameDataRecord(gameID, null, null,
                                                    gameName, new ChessGame());

        gameDataMap.put(gameID, newGame);
        return gameID;
    }

    @Override
    public GameDataRecord getGame(Integer gameID) {
        return gameDataMap.get(gameID);
    }

    @Override
    public void updateGame(Integer gameID, String username, ChessGame.TeamColor playerColour) {
        GameDataRecord gameToUpdate = getGame(gameID);

        if (gameToUpdate != null) {
            if(playerColour == ChessGame.TeamColor.BLACK) {
                GameDataRecord updatedBlackPlayer = new GameDataRecord(gameID, gameToUpdate.whiteUsername(), username,
                                                                        gameToUpdate.gameName(), gameToUpdate.game());
                gameDataMap.put(gameID, updatedBlackPlayer);

            } else if (playerColour == ChessGame.TeamColor.WHITE) {
                GameDataRecord updatedWhitePlayer = new GameDataRecord(gameID, username, gameToUpdate.blackUsername(),
                        gameToUpdate.gameName(), gameToUpdate.game());
                gameDataMap.put(gameID, updatedWhitePlayer);
            }
        }
    }

    @Override
    public void joinGame(ChessGame.TeamColor playerColour, Integer gameID) {
        GameDataRecord gameToJoin = getGame(gameID);

        if (gameToJoin == null) {
            throw new ResponseException(400, "Error: bad request");
        }

        if (playerColour == ChessGame.TeamColor.BLACK && gameToJoin.blackUsername() != null) {
            throw new ResponseException(403, "Error: already taken");
        } else if (playerColour == ChessGame.TeamColor.WHITE && gameToJoin.blackUsername() != null) {
            throw new ResponseException(403, "Error: already taken");
        }

        if (playerColour == ChessGame.TeamColor.BLACK) {
            updateGame(gameID, userRecord.username(), playerColour);
        } else if (playerColour == ChessGame.TeamColor.WHITE) {
            updateGame(gameID, userRecord.username(), playerColour);
        }
    }

    @Override
    public void clear() {
        gameDataMap.clear();
    }
}

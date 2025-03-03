package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameDataRecord;
import model.UserRecord;

import java.util.HashMap;
import java.util.Random;

public class GameDAO implements GameDAOInterface {

    private final HashMap<Integer, GameDataRecord> gameDataMap = new HashMap<>();
    private UserRecord userRecord;


    @Override
    public HashMap<Integer, GameDataRecord> listGames() {
        return gameDataMap;
    }

    @Override
    public int createGame(String gameName) {
        Random generateGameID = new Random();
        int gameID;

        do {
            gameID = 1000 + generateGameID.nextInt(9000);
        } while (gameDataMap.containsKey(gameID));

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
            GameDataRecord updatedGame = null;
            if(playerColour == ChessGame.TeamColor.BLACK) {
                updatedGame = new GameDataRecord(gameID, gameToUpdate.whiteUsername(), username,
                                                                        gameToUpdate.gameName(), gameToUpdate.game());
            } else if (playerColour == ChessGame.TeamColor.WHITE) {
                updatedGame = new GameDataRecord(gameID, username, gameToUpdate.blackUsername(),
                        gameToUpdate.gameName(), gameToUpdate.game());
            } else {
                throw new ResponseException(400, "Error: bad request");
            }
            gameDataMap.put(gameID, updatedGame);
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
        } else if (playerColour == ChessGame.TeamColor.WHITE && gameToJoin.whiteUsername() != null) {
            throw new ResponseException(403, "Error: already taken");
        }

        String username = userRecord.username();

        if (playerColour == ChessGame.TeamColor.BLACK) {
            updateGame(gameID, username, playerColour);
        } else if (playerColour == ChessGame.TeamColor.WHITE) {
            updateGame(gameID, username, playerColour);
        }
    }

    @Override
    public void clear() {
        gameDataMap.clear();
    }
}

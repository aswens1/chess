package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthDataRecord;
import model.CondensedGameData;
import model.GameDataRecord;
import model.UserRecord;
import service.ListGamesResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameDAO implements GameDAOInterface {

    private final HashMap<Integer, GameDataRecord> gameDataMap = new HashMap<>();
    private final AuthDataDAO authDataDAO = new AuthDataDAO();
    private UserRecord userRecord;

    @Override
    public List<CondensedGameData> listGames() {
        List<CondensedGameData> condensedGameDataList = new ArrayList<>();

        for (GameDataRecord game : gameDataMap.values()) {
            CondensedGameData condensedGame = new CondensedGameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());

            condensedGameDataList.add(condensedGame);
        }
        return condensedGameDataList;

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
    public void updateGame(Integer gameID, String username, ChessGame.TeamColor playerColor) {
        GameDataRecord gameToUpdate = getGame(gameID);

        if (gameToUpdate != null) {
            GameDataRecord updatedGame = null;
            if(playerColor == ChessGame.TeamColor.BLACK) {
                updatedGame = new GameDataRecord(gameID, gameToUpdate.whiteUsername(), username,
                                                                        gameToUpdate.gameName(), gameToUpdate.game());
            } else if (playerColor == ChessGame.TeamColor.WHITE) {
                updatedGame = new GameDataRecord(gameID, username, gameToUpdate.blackUsername(),
                        gameToUpdate.gameName(), gameToUpdate.game());
            } else {
                throw new ResponseException(400, "Error: bad request");
            }
            gameDataMap.put(gameID, updatedGame);
        }
    }

    @Override
    public void joinGame(ChessGame.TeamColor playerColor, Integer gameID, String username) {
        GameDataRecord gameToJoin = getGame(gameID);

        if (gameToJoin == null) {
            throw new ResponseException(400, "Error: bad request");
        }

        if (playerColor == ChessGame.TeamColor.BLACK && gameToJoin.blackUsername() != null) {
            throw new ResponseException(403, "Error: already taken");
        } else if (playerColor == ChessGame.TeamColor.WHITE && gameToJoin.whiteUsername() != null) {
            throw new ResponseException(403, "Error: already taken");
        }

        if (playerColor == ChessGame.TeamColor.BLACK || playerColor == ChessGame.TeamColor.WHITE) {
            updateGame(gameID, username, playerColor);
        } else {
            throw new ResponseException(400, "Error: bad request");
        }
    }


    @Override
    public void clear() {
        gameDataMap.clear();
    }
}

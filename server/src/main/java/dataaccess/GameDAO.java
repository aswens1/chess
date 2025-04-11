package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.CondensedGameData;
import model.GameDataRecord;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameDAO implements GameDAOInterface {

    private final HashMap<Integer, GameDataRecord> gameDataMap = new HashMap<>();


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
    public void updateGamePlayer(Integer gameID, String username, ChessGame.TeamColor playerColor, ChessGame game) {
        GameDataRecord gameToUpdate = getGame(gameID);

        if (gameToUpdate != null) {
            GameDataRecord updatedGame;
            if(playerColor == ChessGame.TeamColor.BLACK) {
                updatedGame = new GameDataRecord(gameID, gameToUpdate.whiteUsername(), username,
                                                                        gameToUpdate.gameName(), game);
            } else if (playerColor == ChessGame.TeamColor.WHITE) {
                updatedGame = new GameDataRecord(gameID, username, gameToUpdate.blackUsername(),
                        gameToUpdate.gameName(), game);
            } else {
                throw new ResponseException(400, "Error: bad request");
            }
            gameDataMap.put(gameID, updatedGame);
        }
    }

    @Override
    public void updateGameState(Integer gameID, String username, ChessGame.TeamColor playerColor, ChessGame updatedGame) {
        GameDataRecord gameToUpdate = getGame(gameID);

        if (gameToUpdate != null) {

            GameDataRecord newGame = new GameDataRecord(gameID, gameToUpdate.whiteUsername(),
                    gameToUpdate.blackUsername(), gameToUpdate.gameName(), updatedGame);

            gameDataMap.put(gameID, newGame);

        }
    }

    @Override
    public ChessGame joinGame(ChessGame.TeamColor playerColor, Integer gameID, String username) {
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
            updateGamePlayer(gameID, username, playerColor, gameToJoin.game());
        } else {
            throw new ResponseException(400, "Error: bad request");
        }
        return gameToJoin.game();
    }


    @Override
    public void clear() {
        gameDataMap.clear();
    }
}

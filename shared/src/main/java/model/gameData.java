package model;
import chess.ChessGame;

record gameDataRecord(int gameID, String whiteUsername, String blackUsername,
                      String gameName, ChessGame game) {}
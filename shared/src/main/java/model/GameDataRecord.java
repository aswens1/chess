package model;
import chess.ChessGame;

record GameDataRecord(int gameID, String whiteUsername, String blackUsername,
                      String gameName, ChessGame game) {}
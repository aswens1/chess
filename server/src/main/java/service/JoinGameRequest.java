package service;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor playerColour, Integer gameID) {
}

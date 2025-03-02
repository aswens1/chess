package service;

import model.GameDataRecord;

import java.util.HashMap;

public record ListGamesResult(HashMap<Integer, GameDataRecord> listOfGames) {}

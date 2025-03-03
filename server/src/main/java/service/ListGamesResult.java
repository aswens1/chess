package service;

import model.CondensedGameData;
import model.GameDataRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public record ListGamesResult(List<CondensedGameData> games) { }

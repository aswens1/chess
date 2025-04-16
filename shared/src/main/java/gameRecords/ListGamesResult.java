package gameRecords;

import model.CondensedGameData;

import java.util.List;

public record ListGamesResult(List<CondensedGameData> games) { }

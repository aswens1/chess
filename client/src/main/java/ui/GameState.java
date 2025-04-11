package ui;

import chess.ChessGame;

public class GameState {

    private boolean inGame;
    private boolean isPlayer;

    private ChessGame game;

    public GameState() {
        this.inGame = false;
        this.isPlayer = false;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    public void stateInGame() {
        this.inGame = true;
    }

    public void stateLeaveGame() {
        this.inGame = false;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void statePlayer() {
        this.isPlayer = true;
    }

    public void stateObserver() {
        this.isPlayer = false;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

}

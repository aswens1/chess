package ui;

public class GameState {

    private boolean inGame;
    private boolean isPlayer;

    public GameState() {
        this.inGame = false;
        this.isPlayer = false;
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

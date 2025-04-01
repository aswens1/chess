package ui;

public class GameState {

    private boolean inGame;

    public GameState() {
        this.inGame = false;
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

}

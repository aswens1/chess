package ui;

public class State {

    private boolean loggedIn;

    public State() {
        this.loggedIn = false;
    }

    public void stateLogIn() {
        this.loggedIn = true;
    }

    public void stateLogOut() {
        this.loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
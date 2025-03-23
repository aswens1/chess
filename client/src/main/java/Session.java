public class Session {

    private boolean loggedIn = false;

    public boolean loggedIn() {
        return loggedIn;
    }

    public void logIn() {
        this.loggedIn = true;
    }

    public void logOut() {
        this.loggedIn = false;
    }

}

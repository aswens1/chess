package websocket.messages;

public class Notifications extends ServerMessage{

    private final String message;

    public Notifications(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

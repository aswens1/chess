package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;


public class Connection {

    public int gameID;
    public Session session;
    public String username;

    public Connection(int gameID, String username, Session session) {
        this.gameID = gameID;
        this.session = session;
        this.username = username;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

}

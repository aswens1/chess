package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    Gson gson = new Gson();

    public void add(int gameID, String username, Session session) {
        var connection = new Connection(gameID, username, session);
        connections.putIfAbsent(gameID, new ArrayList<>());

        connections.get(gameID).removeIf(c -> c.username.equals(username));
        connections.get(gameID).add(connection);
    }

    public void remove(int gameID, String username) {
        if (connections.containsKey(gameID)) {
            connections.get(gameID).removeIf(c -> c.username.equals(username));
            if (connections.get(gameID).isEmpty()) {
                connections.remove(gameID);
            }
        }
    }

    public void broadcast(Integer gameID, String excludeUser, ServerMessage notification) throws IOException {
//        System.out.println("Broadcasting to gameID " + gameID + ": " + notification);

        if (!connections.containsKey(gameID)) {
            return;
        }

        var notifyJSON = gson.toJson(notification);

        var removeList = new ArrayList<Connection>();
        for (var c : connections.get(gameID)) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUser)) {
                    c.send(notifyJSON);
                    System.out.println("Sending " + notification.getNotificationMessage() + " to " + c.username);
                }
            } else {
                removeList.add(c);
            }
        }

        connections.get(gameID).removeAll(removeList);
        if (connections.get(gameID).isEmpty()) {
            connections.remove(gameID);
        }
    }

    public ArrayList<Session> getSession(int gameID, String excludeUser) {
        ArrayList<Session> sessions = new ArrayList<>();

        if (!connections.containsKey(gameID)) {
            return sessions;
        }

        for (var c : connections.get(gameID)) {
            if (!c.username.equals(excludeUser) && c.session.isOpen()) {
                sessions.add(c.session);
            }
        }
        return sessions;
    }

    @Override
    public String toString() {
        return "ConnectionManager{" +
                "connections=" + connections +
                ", gson=" + gson +
                '}';
    }
}
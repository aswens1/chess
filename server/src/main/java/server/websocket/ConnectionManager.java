package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.Notifications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String username, Session session) {
        var connection = new Connection(gameID, username, session);
        connections.putIfAbsent(gameID, new ArrayList<>());

        connections.get(gameID).removeIf(c -> c.username.equals(username));
        connections.get(gameID).add(connection);
    }

    public void remove(int gameID, String username) {
        if (connections.contains(gameID)) {
            connections.get(gameID).removeIf(c -> c.username.equals(username));
            if (connections.get(gameID).isEmpty()) {
                connections.remove(gameID);
            }
        }
    }

    public void broadcast(Integer gameID, String excludeUser, Notifications notification) throws IOException {
//        System.out.println("Broadcasting to gameID " + gameID + ": " + notification);

        if (!connections.containsKey(gameID)) {
            return;
        }

        var removeList = new ArrayList<Connection>();
        for (var c : connections.get(gameID)) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUser)) {
                    c.send(notification.toString());
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
}
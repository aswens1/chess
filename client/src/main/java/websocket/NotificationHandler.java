package websocket;

import websocket.messages.*;

public interface NotificationHandler {
    void notify(Notifications notification);
}
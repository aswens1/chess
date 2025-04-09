package websocket;

import javax.websocket.*;
import java.net.URI;

public class WebSocketClient extends Endpoint {

    private final NotificationHandler notificationHandler;
    private Session session;

    public WebSocketClient(NotificationHandler notificationHandler) throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.notificationHandler = notificationHandler;

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
                //should do load game so far?
            }
        });
    }


    public void send(String msg) throws Exception {
        if (session != null && session.isOpen()) {
            this.session.getBasicRemote().sendText(msg);
        } else {
            throw new IllegalStateException("Websocket is not open.");
        }
    }
}



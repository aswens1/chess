package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.*;

public class WebSocketFacade extends Endpoint {

    private final NotificationHandler notificationHandler;
    private Session session;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.notificationHandler = notificationHandler;

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        notificationHandler.notify(message);
                    } catch (Exception exception) {
                        ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null);
                        notificationHandler.notify(error.toString());
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connectingToGame(String authToken, Integer gameID, String pov, String username) throws IOException {
        try {
            UserGameCommand UGC = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, pov,
                    username, null, null, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(UGC));
        } catch (IOException exception) {
            throw new ResponseException(500, exception.getMessage());
        }
    }


    public void sendCommand(UserGameCommand UGC) {
        if (session != null && session.isOpen()) {
            String json = new Gson().toJson(UGC);
            session.getAsyncRemote().sendText(json);
        } else {
            System.out.println("Session is closed, cannot send message.");
        }
    }

}

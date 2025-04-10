package websocket;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.GameBoardDrawing;
import websocket.commands.UserGameCommand;
import websocket.messages.Notifications;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.*;

public class WebSocketFacade extends Endpoint {

    private final NotificationHandler notificationHandler;
    private Session session;
    private final Gson gson = new Gson();
    private ChessBoard currentBoard;
    private ChessGame.TeamColor playerColor;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notifications notifications = new Gson().fromJson(message, Notifications.class);
                    notificationHandler.notify(notifications);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connectingToGame(String authToken, Integer gameID, ChessGame.TeamColor pov, String username) throws IOException {
        try {
            UserGameCommand UGC = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, pov, username);
            System.out.println("Sending command: " + UGC);
            this.session.getBasicRemote().sendText(new Gson().toJson(UGC));
        } catch (IOException exception) {
            throw new ResponseException(500, exception.getMessage());
        }
    }


    public void sendCommand(UserGameCommand UGC) {
        if (session != null && session.isOpen()) {
            String json = new Gson().toJson(UGC);
            System.out.println("Sending to server: " + json);
            session.getAsyncRemote().sendText(json);
        } else {
            System.out.println("Session is closed, cannot send message.");
        }
    }

    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        } else {
            throw new IllegalStateException("Websocket is not open.");
        }
    }
}

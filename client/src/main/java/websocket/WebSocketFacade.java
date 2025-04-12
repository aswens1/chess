package websocket;

import chess.ChessBoard;
import chess.ChessGame;
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
    private final Gson gson = new Gson();
    private ChessBoard currentBoard;
    private ChessGame.TeamColor playerColor;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

//            System.out.println(socketURI);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
//            System.out.println("Connected to server? Session: " + this.session);
            this.notificationHandler = notificationHandler;

            //set message handler
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
//            System.out.println("Sending command: " + UGC);
            this.session.getBasicRemote().sendText(new Gson().toJson(UGC));
        } catch (IOException exception) {
            throw new ResponseException(500, exception.getMessage());
        }
    }


    public void sendCommand(UserGameCommand UGC) {
        if (session != null && session.isOpen()) {
            String json = new Gson().toJson(UGC);
//            System.out.println("Sending to server: " + json);
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

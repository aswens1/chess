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

//            System.out.println(socketURI);

            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = gson.fromJson(message, ServerMessage.class);

                    switch (notification.getServerMessageType()) {
                        case NOTIFICATION:
                            Notifications notifications = gson.fromJson(message, Notifications.class);
                            notificationHandler.notify(notifications);
                            break;
                        case LOAD_GAME:
                            ServerMessage loadGame = gson.fromJson(message, ServerMessage.class);
                            currentBoard = loadGame.getGame().getBoard();
                            GameBoardDrawing.drawBoard(playerColor, currentBoard, null, loadGame.getGame());
                            break;
                        case ERROR:
                            ServerMessage errorMessage = gson.fromJson(message, ServerMessage.class);
                            errorMessage.setMessage(errorMessage.getMessage());
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void sendCommand(UserGameCommand UGC) {
        if (session != null && session.isOpen()) {
            String json = new Gson().toJson(UGC);
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

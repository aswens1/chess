package ui;

import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.Notifications;
import websocket.messages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class Repl {

    private final ChessClient preClient;
    private final ChessClient postClient;
    private final ChessClient gameClient;
    private final State state = new State();
    private final GameState gameState = new GameState();

    private final NotificationHandler notificationHandler = new NotificationHandler() {
        @Override
        public void notify(ServerMessage message) {
        }
    };


    public Repl(String serverURL) {
        ServerFacade sf = new ServerFacade(serverURL);
        WebSocketFacade wsf = new WebSocketFacade(serverURL, notificationHandler);
        preClient = new PreClient(sf, state);
        postClient = new PostClient(sf, state, gameState, wsf);
        gameClient = new GamePlayClient(sf, gameState, wsf);
    }


    public void run() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println(" ♕ Welcome to 240 Chess! Enter a command to get started. ♕ ");

        Scanner scanner = new Scanner(System.in);
        var command = "";

        while (true) {
            String line = scanner.nextLine();

            try {
                if (!state.isLoggedIn()) {
                    command = preClient.eval(line);
                } else {
                    command = postClient.eval(line);
                    if (gameState.isInGame()) {
                        command = gameClient.eval(line);
                    }
                }

                System.out.println(command);

                if(line.equalsIgnoreCase("quit")) {
                    out.println(SET_TEXT_COLOR_BLUE + " ♕ Thank you for playing! ♕ " + RESET_TEXT_COLOR);
                    System.exit(200);
                }

            } catch (Throwable e) {
                var message = e.toString();
                System.out.print(message);
            }
        }
    }

}

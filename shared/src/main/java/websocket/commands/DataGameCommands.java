package websocket.commands;

public record DataGameCommands(websocket.commands.UserGameCommand.CommandType commandType, String authToken,
                               Integer gameID, String playerColor, String username, String ogPos, String newPos) {}

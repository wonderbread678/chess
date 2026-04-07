package websocket.commands;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;

public class MakeMoveCommand {

    private final UserGameCommand.CommandType commandType;
    private final String authToken;
    private final int gameID;
    private final ChessMove move;

    public MakeMoveCommand(UserGameCommand.CommandType commandType,
                           String authToken,
                           int gameID,
                           ChessMove move){
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.move = move;

    }

    public ChessMove getMove() {
        return move;
    }

    public int getGameID() {
        return gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public UserGameCommand.CommandType getCommandType() {
        return commandType;
    }

    @Override
    public String toString() {
        return "MakeMoveCommand{" +
                "commandType=" + commandType +
                ", authtoken='" + authToken + '\'' +
                ", gameID=" + gameID +
                ", move=" + move +
                '}';
    }
}

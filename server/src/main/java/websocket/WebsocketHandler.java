package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IO;
import java.io.IOException;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler{

    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public WebsocketHandler(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @Override
    public void handleConnect(WsConnectContext ctx){
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx){
        try {
            UserGameCommand userCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (userCommand.getCommandType()) {
                case CONNECT -> connectWS(userCommand, ctx.session);
                case MAKE_MOVE -> makeMoveWS(ctx.message(), ctx.session);
                case LEAVE -> leaveWS(userCommand, ctx.session);
                case RESIGN -> resignWS(userCommand, ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx){
        System.out.print("Websocket closed");
    }

    public void connectWS(UserGameCommand userCommand, Session session) throws IOException{
        try{
            connections.add(userCommand.getGameID(), session);
            AuthData user = authDAO.getAuth(userCommand.getAuthToken());
            GameData game = gameDAO.getGame(userCommand.getGameID());
            String message;
            if(game.blackUsername().equals(user.username())){
                message = String.format("%s has joined game as black", user.username());
            }
            else if (game.whiteUsername().equals(user.username())){
                message = String.format("%s has joined game as white", user.username());
            }
            else{
                message = String.format("%s has joined game as an observer", user.username());
            }
            var notification = new NotificationMessage(message);
            connections.gameBroadcast(userCommand.getGameID(), session, notification);

            sendGame(game, session);
        }
        catch(DataAccessException ex){
            sendError("Error: Server error", session);
        }
    }

    public void makeMoveWS(String ctxMessage, Session session) throws IOException{
        try{
            MakeMoveCommand moveCommand = new Gson().fromJson(ctxMessage, MakeMoveCommand.class);
            GameData game = gameDAO.getGame(moveCommand.getGameID());

            game.game().makeMove(moveCommand.getMove());
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);

            var moveMessage = String.format("Move: %s", moveCommand.getMove().toString());
            NotificationMessage notification = new NotificationMessage(moveMessage);
            connections.gameBroadcast(moveCommand.getGameID(), session, notification);

            String mateMessage = mateCheck(game.game().getTeamTurn(), game.game());
            if(mateMessage != null){
                NotificationMessage mateNotification = new NotificationMessage(mateMessage);
                connections.gameBroadcast(moveCommand.getGameID(), null, mateNotification);
            }
        }
        catch(DataAccessException ex){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        }
        catch(InvalidMoveException ex){
            System.out.print("Error: invalid move");
        }
    }

    public void leaveWS(UserGameCommand userCommand, Session session) throws IOException{
        String message = "Leaving the game";
        NotificationMessage notification = new NotificationMessage(message);
        connections.gameBroadcast(userCommand.getGameID(), session, notification);
        connections.remove(userCommand.getGameID(), session);
    }

    public void resignWS(UserGameCommand userCommand, Session session) throws IOException{
        String message = "Resigned";
        NotificationMessage notification = new NotificationMessage(message);
        connections.gameBroadcast(userCommand.getGameID(), session, notification);
    }

    public String mateCheck(ChessGame.TeamColor playerColor, ChessGame game){
        String color;
        String message = null;
        if(playerColor == ChessGame.TeamColor.WHITE){
            color = "White";
        }
        else{
            color = "Black";
        }
        if(game.isInCheck(playerColor)){
            message = String.format("%s is in check!", color);
        }
        else if(game.isInCheckmate(playerColor)){
            message = String.format("%s is in checkmate!", color);
        }
        else if(game.isInStalemate(playerColor)){
            message = "Stalemate";
        }
        return message;
    }

    public void sendError(String error, Session session) throws IOException{
        ErrorMessage errorMessage = new ErrorMessage(error);
        String errorString = new Gson().toJson(errorMessage);
        session.getRemote().sendString(errorString);
    }

    public void sendGame(GameData game, Session session) throws IOException {
        LoadGameMessage loadGameMessage = new LoadGameMessage(game);
        String loadGameString = new Gson().toJson(loadGameMessage);
        session.getRemote().sendString(loadGameString);
    }
}

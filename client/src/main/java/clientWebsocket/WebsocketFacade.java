package clientWebsocket;

import chess.ChessMove;
import model.GameData;
import server.ResponseException;

import com.google.gson.Gson;


import jakarta.websocket.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



public class WebsocketFacade extends Endpoint {

    NotificationHandler notificationHandler;
    Session session;

    public WebsocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                        NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                        notificationHandler.notify(notification);
                    }
                    else if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
                        ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
                        notificationHandler.notifyError(error);
                    }
                    else if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                        LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                        notificationHandler.notifyGame(loadGameMessage);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connectGame(String authToken, int gameID) throws ResponseException{
        try{
            var userCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userCommand));
        }
        catch(IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException{
        try{
            var makeMoveCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE,
                    authToken,
                    gameID,
                    move);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveCommand));
        }
        catch(IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resignGame(String authToken, int gameID) throws ResponseException{
        try{
            var userCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userCommand));
        }
        catch(IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException{
        try{
            var userCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userCommand));
        }
        catch(IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }
}

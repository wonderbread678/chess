package websocket;

import com.google.gson.Gson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler{

    private final ConnectionManager connections = new ConnectionManager();

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
                case CONNECT -> connectWS(ctx.message(), ctx.session);
                case MAKE_MOVE -> makeMoveWS();
                case LEAVE -> leaveWS();
                case RESIGN -> resignWS();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx){
        System.out.print("Websocket closed");
    }

    public void connectWS(String ctxMessage, Session session){
        connections.add();
    }

    public void makeMoveWS(){

    }

    public void leaveWS(){

    }

    public void resignWS(){

    }

//    public void loadGameWS(String message, Session session){
//        GameData game = new Gson().fromJson(message, GameData.class);
//        connections.add(game.gameID(), session);
//    }
//
//    public void errorWS(String message, Session session){
//        String msg = String.format("Error: %s", new Gson().fromJson(message, ErrorMessage.class));
//        connections.gameBroadcast();
//
//    }
//
//    public void notificationWS(String message, Session session){
//
//    }
}

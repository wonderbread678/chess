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
            ServerMessage serverMessage = new Gson().fromJson(ctx.message(), ServerMessage.class);
            switch (serverMessage.getServerMessageType()) {
                case LOAD_GAME -> loadGameWS(ctx.message(), ctx.session);
                case ERROR -> errorWS(ctx.message(), ctx.session);
                case NOTIFICATION -> notificationWS(ctx.message(), ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx){

    }

    public void connectWS(){

    }

    public void makeMoveWS(){

    }

    public void leaveWS(){

    }

    public void resignWS(){

    }

    public void loadGameWS(String message, Session session){
        GameData game = new Gson().fromJson(message, GameData.class);
        connections.
    }

    public void errorWS(String message, Session session){

    }

    public void notificationWS(String message, Session session){

    }
}

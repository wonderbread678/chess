package server;

import com.google.gson.Gson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import org.eclipse.jetty.websocket.api.Session;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler{

    @Override
    public void handleConnect(WsConnectContext ctx){

    }

    @Override
    public void handleMessage(WsMessageContext ctx){

    }

    @Override
    public void handleClose(WsCloseContext ctx){

    }
}

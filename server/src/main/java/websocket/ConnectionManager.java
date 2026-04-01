package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import websocket.*;
import websocket.messages.NotificationMessage;

public class ConnectionManager {

    private final ConcurrentHashMap<Integer, List<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session){
        connections.compute(gameID, (k, list) -> {
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(session);
            return list;
        });
    }

    public void remove(int gameID, Session session){
        connections.computeIfPresent(gameID, (k, list) -> {
            if(list.contains(session)){
                list.remove(session);
            }
            return list;
        });
    }

    public void gameBroadcast(int gameID, Session excludeSession, NotificationMessage notification) throws IOException {
        String message = notification.toString();
        List<Session> gameSessions = connections.get(gameID);
        for(Session c : gameSessions){
            if(c.isOpen()){
                if(!c.equals(excludeSession)){
                    c.getRemote().sendString(message);
                }
            }
        }
    }
}

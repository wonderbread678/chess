package clientWebsocket;

import model.GameData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {
    void notify(NotificationMessage notification);

    void notifyGame(LoadGameMessage loadGameMessage);

    void notifyError(ErrorMessage errorMessage);
}

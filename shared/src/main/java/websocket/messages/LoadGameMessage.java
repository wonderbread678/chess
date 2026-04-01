package websocket.messages;

import model.GameData;

public class LoadGameMessage {

    private final GameData game;

    public LoadGameMessage(GameData game){
        this.game = game;
    }

    public void loadGame(){
        System.out.print("game will someday go here (need to implement)");
    }
}

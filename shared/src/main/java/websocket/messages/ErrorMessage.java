package websocket.messages;

public class ErrorMessage {

    private final String error;

    public ErrorMessage(String error){
        this.error = error;
    }

    public void printError(){
        System.out.printf("Error: %s", error);
    }
}

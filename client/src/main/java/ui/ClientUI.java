package ui;

import java.util.Arrays;
import java.util.Scanner;

import client.*;
import server.ResponseException;

public class ClientUI {

    private String username = null;
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNED_OUT;

    public ClientUI(String serverURL){
        server = new ServerFacade(serverURL);
    }

    public void run(){
        System.out.println("Welcome to Ethan's Chess Server. Sign in to start!");
        System.out.print("help");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();
            try{
                result = eval(line);
            } catch(Throwable ex){
                var msg = ex.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
//        System.out.print("\n" + EscapeSequences.RESET + ">>> " + EscapeSequences.GREEN);
    }

    private void exceptionHandler(ResponseException ex){
        String clientErrorMessage = switch(ex.getCode()){
            case 400 -> "Invalid input";
            case 401 -> "Unauthorized";
            case 403 -> "Already taken";
            default -> "Server Error";
        };
        System.out.println(clientErrorMessage);
    }

    public String eval(String input){
        try{
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(state == State.SIGNED_OUT){
                return switch(cmd){
                    case "quit" -> quit();
                    case "login" -> login(params);
                    case "register" -> register(params);
                    default -> help();
                };
            }
            else if(state == State.SIGNED_IN){
                return switch(cmd){
                    case "logout" -> logout();
                    case "create game" -> createGame(params);
                    case "list games" -> listGames();
                    case "join game" -> joinGame(params);
                    case "observe" -> observeGame();
                    default -> help();
                };
            }
            else if(state == State.GAME){
                return switch(cmd){
                    case "exit" -> exitGame();
                    default -> help();
                };
            }
        }
        catch (ResponseException ex){
            exceptionHandler(ex);
        }
    }

    private void isSignedIn() throws ResponseException {
        if (state == State.SIGNED_OUT) {
            throw new ResponseException(401, "You must sign in");
        }
    }

    private String exitGame(){

    }

    private String observeGame() throws ResponseException{

    }

    private String joinGame(String... params) throws ResponseException{

    }

    private String listGames() throws ResponseException{

    }

    private String createGame(String... params) throws ResponseException{

    }

    private String logout() throws ResponseException{

    }

    private String register(String... params) throws ResponseException{

    }

    private String login(String... params) throws ResponseException{

    }

    private String quit() throws ResponseException{
        return "NEED TO IMPLEMENT";
    }

    private String help() {
        if(state == State.SIGNED_OUT){
            return """
                    - login
                    - register
                    - quit
                    """;
        }
        else if(state == State.SIGNED_IN){
            return """
                    - logout
                    - create game
                    - join game
                    - list games
                    - observe
                    """;
        }
        else {
            return """
                   - exit
                   - help
                   """;
        }
    }
}

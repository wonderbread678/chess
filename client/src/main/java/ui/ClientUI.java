package ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import chess.ChessGame;
import client.*;
import com.google.gson.Gson;
import model.AuthData;
import model.ListGamesData;
import model.ListGamesResponse;
import server.ResponseException;

public class ClientUI {

    private String username = null;
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNED_OUT;
    private final HashMap<Integer, Integer> gameIDToListNum= new HashMap<>();

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
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + result);
            } catch(Throwable ex){
                var msg = ex.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> " + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
    }

    private String exceptionHandler(ResponseException ex){
        return switch(ex.getCode()){
            case 400 -> "Invalid input";
            case 401 -> "Unauthorized";
            case 403 -> "Already taken";
            default -> "Server Error";
        };
    }

    public String eval(String input){
        try{
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(state == State.SIGNED_OUT){
                return switch(cmd){
                    case "quit" -> "quit";
                    case "login" -> login(params);
                    case "register" -> register(params);
                    default -> help();
                };
            }
            else if(state == State.SIGNED_IN){
                return switch(cmd){
                    case "logout" -> logout();
                    case "create" -> createGame(params);
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
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
            return "Error: You are in invalid state";
        }
        catch (ResponseException ex){
            return exceptionHandler(ex);
        }
    }

    private void isSignedIn() throws ResponseException {
        if (state == State.SIGNED_OUT) {
            throw new ResponseException(401, "You must sign in");
        }
    }

    private String exitGame(){
        state = State.SIGNED_IN;
        return "Exited game";
    }

    private String observeGame(String... params) throws ResponseException{
        try{
            if(params.length == 1){
                state = State.GAME;
                Chessboard.main(null);
                return String.format("Observing game %s", params[0]);
            }
            throw new ResponseException(400, "Error: Bad input");
        }
        catch(ResponseException ex){
            return exceptionHandler(ex);
        }
    }

    private String joinGame(String... params) throws ResponseException{
        try{
            isSignedIn();
            if(params.length == 2){
                int gameNum = Integer.parseInt(params[1]);
                int gameID = gameIDToListNum.get(gameNum);

                ChessGame.TeamColor playerColor = null;
                if (params[0].equals("black")){
                    playerColor = ChessGame.TeamColor.BLACK;
                }
                else if(params[0].equals("white")){
                    playerColor = ChessGame.TeamColor.WHITE;
                }
                else{
                    throw new ResponseException(400, "Error: Invalid color");
                }

                server.joinGame(playerColor, gameID);
                state = State.GAME;
                Chessboard.main(null);
                return "Joining game";
            }
            throw new ResponseException(400, "Error: Bad input");
        }
        catch(ResponseException ex){
            return exceptionHandler(ex);
        }
    }

    private String listGames() throws ResponseException{
        try{
            isSignedIn();
            ListGamesResponse gamesList = server.listGames();
            Collection<ListGamesData> games = gamesList.games();
            return clientListBuilder(games);
        }
        catch(ResponseException ex){
            return exceptionHandler(ex);
        }
    }

    private String createGame(String... params) throws ResponseException{
        try{
            isSignedIn();
            if(params.length == 1){
                server.createGame(params[0]);
                return String.format("Game: %s created successfully", params[0]);
            }
            throw new ResponseException(400, "Error: Invalid input");
        }
        catch(ResponseException ex){
            return exceptionHandler(ex);
        }
    }

    private String logout() throws ResponseException{
        try{
            isSignedIn();
            server.logout();
            state = State.SIGNED_OUT;
            return "Logged out successfully";
        }
        catch(ResponseException ex){
            return exceptionHandler(ex);
        }
    }

    private String register(String... params) throws ResponseException{
        try{
            if(params.length == 3){
                AuthData newUserData = server.register(params[0], params[1], params[2]);
                authToken = newUserData.authToken();
                username = newUserData.username();
                state = State.SIGNED_IN;
                return String.format("Welcome to the server, %s\n", username);
            }
            throw new ResponseException(400, "Error: Invalid input");
        }
        catch(ResponseException ex){
            return exceptionHandler(ex);
        }
    }

    private String login(String... params) throws ResponseException{
        try{
            if(params.length == 2){

                AuthData userData = server.login(params[0], params[1]);
                authToken = userData.authToken();
                username = userData.username();
                state = State.SIGNED_IN;
                return String.format("Welcome back, %s\n", username);
            }
            throw new ResponseException(400, "Error: Invalid input");
        }
        catch(ResponseException ex){
            return exceptionHandler(ex);
        }
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
                    - create
                    - join
                    - list
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

    private String clientListBuilder(Collection<ListGamesData> games) {
        StringBuilder buildList = new StringBuilder();
        int i = 1;
        for(ListGamesData game : games){
            buildList.append(i)
                    .append(". ")
                    .append(game.gameName())
                    .append(EscapeSequences.SET_TEXT_BOLD + " | ")
                    .append("White username: ");
            if(game.whiteUsername() == null){
                buildList.append("None ");
            }
            else {
                buildList.append(game.whiteUsername()).append(" ");
            }

            buildList.append(EscapeSequences.SET_TEXT_BOLD + "| ")
                    .append("Black username: ");
            if(game.blackUsername() == null){
                buildList.append("None ");
            }
            else{
                buildList.append(game.blackUsername()).append(" ");
            }

            buildList.append("\n");

            gameIDToListNum.put(i, game.gameID());
            i += 1;
        }
        return buildList.toString();
    }
}

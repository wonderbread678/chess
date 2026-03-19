//package ui;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Scanner;
//
//import chess.ChessGame;
//import client.*;
//import com.google.gson.Gson;
//import model.AuthData;
//import model.ListGamesData;
//import model.ListGamesResponse;
//import server.ResponseException;
//import server.response.CreateGameResponse;
//
//import static java.lang.System.exit;
//
//public class ClientUI {
//
//    private String username = null;
//    private String authToken = null;
//    private final ServerFacade server;
//    private State state = State.SIGNED_OUT;
//    private final HashMap<Integer, Integer> gameIDToListNum= new HashMap<>();
//
//    public ClientUI(String serverURL){
//        server = new ServerFacade(serverURL);
//    }
//
//    public void run(){
//        System.out.println("Welcome to Ethan's Chess Server. Sign in to start!");
//        System.out.print("help");
//
//        Scanner scanner = new Scanner(System.in);
//        var result = "";
//        while(!result.equals("quit")){
//            printPrompt();
//            String line = scanner.nextLine();
//            try{
//                result = eval(line);
//            } catch(Throwable ex){
//                var msg = ex.toString();
//                System.out.print(msg);
//            }
//        }
//        System.out.println();
//    }
//
//    private void printPrompt() {
////        System.out.print("\n" + EscapeSequences.RESET + ">>> " + EscapeSequences.GREEN);
//    }
//
//    private void exceptionHandler(ResponseException ex){
//        String clientErrorMessage = switch(ex.getCode()){
//            case 400 -> "Invalid input";
//            case 401 -> "Unauthorized";
//            case 403 -> "Already taken";
//            default -> "Server Error";
//        };
//        System.out.println(clientErrorMessage);
//    }
//
//    public String eval(String input){
//        try{
//            String[] tokens = input.toLowerCase().split(" ");
//            String cmd = (tokens.length > 0) ? tokens[0] : "help";
//            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
//            if(state == State.SIGNED_OUT){
//                return switch(cmd){
//                    case "quit" -> quit();
//                    case "login" -> login(params);
//                    case "register" -> register(params);
//                    default -> help();
//                };
//            }
//            else if(state == State.SIGNED_IN){
//                return switch(cmd){
//                    case "logout" -> logout();
//                    case "create game" -> createGame(params);
//                    case "list games" -> listGames();
//                    case "join game" -> joinGame(params);
//                    case "observe" -> observeGame();
//                    default -> help();
//                };
//            }
//            else if(state == State.GAME){
//                return switch(cmd){
//                    case "exit" -> exitGame();
//                    default -> help();
//                };
//            }
//        }
//        catch (ResponseException ex){
//            exceptionHandler(ex);
//        }
//    }
//
//    private void isSignedIn() throws ResponseException {
//        if (state == State.SIGNED_OUT) {
//            throw new ResponseException(401, "You must sign in");
//        }
//    }
//
////    private String exitGame(){
////
////    }
//
////    private String observeGame() throws ResponseException{
////
////    }
//
//    private String joinGame(String... params) throws ResponseException{
//        isSignedIn();
//        int gameNum = Integer.parseInt(params[1]);
//        int gameID = gameIDToListNum.get(gameNum);
//
//        ChessGame.TeamColor playerColor = new Gson().fromJson(params[0], ChessGame.TeamColor.class);
//
//        server.joinGame(playerColor, gameID);
//        return "Joining game";
//    }
//
//    private String listGames() throws ResponseException{
//        isSignedIn();
//        ListGamesResponse gamesList = server.listGames();
//        Collection<ListGamesData> games = gamesList.games();
//        return clientListBuilder(games);
//    }
//
//    private String createGame(String... params) throws ResponseException{
//        isSignedIn();
//        if(params.length == 1){
//            CreateGameResponse createGameData = server.createGame(params[0]);
//            return String.format("Game: %s created successfully", params[0]);
//        }
//    }
//
//    private String logout() throws ResponseException{
//        isSignedIn();
//        server.logout();
//        state = State.SIGNED_OUT;
//        return "Logged out successfully";
//    }
//
//    private String register(String... params) throws ResponseException{
//        if(params.length == 3){
//            AuthData newUserData = server.register(params[0], params[1], params[2]);
//            authToken = newUserData.authToken();
//            username = newUserData.username();
//            state = State.SIGNED_IN;
//            return String.format("Welcome to the server, %s\n", username);
//        }
//    }
//
//    private String login(String... params) throws ResponseException{
//        if(params.length == 2){
//            AuthData userData = server.login(params[0], params[1]);
//            authToken = userData.authToken();
//            username = userData.username();
//            state = State.SIGNED_IN;
//            return String.format("Welcome back, %s\n", username);
//        }
//        throw new ResponseException(401, "Invalid username or password");
//    }
//
//    private String quit() throws ResponseException{
//        System.out.println("Quitting");
//        exit(0);
//    }
//
//    private String help() {
//        if(state == State.SIGNED_OUT){
//            return """
//                    - login
//                    - register
//                    - quit
//                    """;
//        }
//        else if(state == State.SIGNED_IN){
//            return """
//                    - logout
//                    - create game
//                    - join game
//                    - list games
//                    - observe
//                    """;
//        }
//        else {
//            return """
//                   - exit
//                   - help
//                   """;
//        }
//    }
//
//    private String clientListBuilder(Collection<ListGamesData> games) {
//        StringBuilder buildList = new StringBuilder();
//        int i = 1;
//        for(ListGamesData game : games){
//            buildList.append(i)
//                    .append(". ")
//                    .append(game.gameName())
//                    .append("White username: ");
//            if(game.whiteUsername() == null){
//                buildList.append("None ");
//            }
//            else {
//                buildList.append(game.whiteUsername()).append(" ");
//            }
//
//            buildList.append("Black username: ");
//            if(game.blackUsername() == null){
//                buildList.append("None ");
//            }
//            else{
//                buildList.append(game.blackUsername()).append(" ");
//            }
//
//            buildList.append("\n");
//
//            gameIDToListNum.put(i, game.gameID());
//            i += 1;
//        }
//        return buildList.toString();
//    }
//}

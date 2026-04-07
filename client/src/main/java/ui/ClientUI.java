package ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import client.*;
import clientWebsocket.NotificationHandler;
import clientWebsocket.WebsocketFacade;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.ListGamesData;
import model.ListGamesResponse;
import server.ResponseException;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public class ClientUI implements NotificationHandler {

    private String username = null;
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNED_OUT;
    private final HashMap<Integer, Integer> gameIDToListNum= new HashMap<>();
    private final HashMap<Integer, String> listNumToName= new HashMap<>();
    private final HashMap<String, Integer> authTokenToGameID = new HashMap<>();
    private final WebsocketFacade ws;

    public ClientUI(String serverURL) throws ResponseException {
        server = new ServerFacade(serverURL);
        ws = new WebsocketFacade(serverURL, this);
    }

    public void run(){
        System.out.println("Welcome to Ethan's Chess Server. Sign in to start!");
        System.out.print("- help");

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
        String strState = "SIGNED_OUT";
        if(state == State.SIGNED_IN){
            strState = "SIGNED_IN";
        }
        else if(state == State.GAME){
            strState = "IN_GAME";
        }
        System.out.printf("\n" + "[%s] >>> " + EscapeSequences.SET_TEXT_COLOR_MAGENTA, strState);
    }

    @Override
    public void notify(NotificationMessage notification) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + notification.getMessage());
        printPrompt();
    }

    @Override
    public void notifyGame(LoadGameMessage loadGameMessage){
        ChessBoard board = loadGameMessage.getGame().game().getBoard();
        String stringBoard = new Gson().toJson(board);
        Chessboard.main();

    }

    @Override
    public void notifyError(ErrorMessage errorMessage){
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + errorMessage.getErrorMessage());
        printPrompt();
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
                    case "observe" -> observeGame(params);
                    default -> help();
                };
            }
            else if(state == State.GAME){
                return switch(cmd){
                    case "leave" -> leave();
                    case "makeMove" -> makeMove(params);
                    case "confirmResign" -> resign();
                    case "resign" -> "Are you sure? Type \"confirmResign\" to resign";
                    case "redraw" -> redraw();
                    case "highlight" -> highlightLegalMoves(params);
                    default -> help();
                };
            }
            return "Error: You are in invalid state";
        }
        catch (ClientException ex){
            return exceptionHandler(ex);
        }
    }


    private String exceptionHandler(ClientException ex){
        return switch(ex.getCode()){
            case 400 -> "Invalid input";
            case 401 -> "Unauthorized";
            case 403 -> "Already taken";
            case 405 -> "Invalid game";
            case 406 -> "Invalid color";
            case 407 -> "Invalid observe input. Requires (<game number>)";
            case 408 -> "Invalid input for join. Requires (<player color> <game number>)";
            case 409 -> "Invalid input for create. Requires (<game name>)";
            case 410 -> "Invalid input for login. Requires (<username> <password>)";
            case 411 -> "Invalid input for register. Requires (<username> <password> <email>)";
            case 412 -> "Game does not exist";
            case 413 -> "Game number must be an integer";
            default -> "Server Error";
        };
    }

    private void isSignedIn() throws ClientException {
        if (state == State.SIGNED_OUT) {
            throw new ClientException(401, "You must sign in");
        }
    }

    private String highlightLegalMoves(String...params) throws ClientException{
        try{
            return "need to implement";
        }
        catch(ResponseException ex){
            throw new ClientException(500, "Error: Server error");
        }
    }

    private String redraw() throws ClientException{
        try{
            return "need to implement";
        }
        catch(ResponseException ex){
            throw new ClientException(500, "Error: Server error");
        }
    }

    private String resign() throws ClientException{
        try{
            ws.resignGame(authToken, authTokenToGameID.get(authToken));
            return "You have resigned";
        }
        catch(ResponseException ex){
            throw new ClientException(500, "Error: Server error");
        }
    }

    private String makeMove(String...params) throws ClientException{
        try{
            if(params.length == 2){
                String startPosition = params[0];
                String endPosition = params[1];
                ChessMove move = moveConverter(startPosition, endPosition);
                ws.makeMove();
            }
        }
        catch(ResponseException ex){
            throw new ClientException(500, "Error: Server error");
        }
    }

    private ChessMove moveConverter(String startPosition, String endPosition) {
        for (int i = 0; i < startPosition.length(); ++i){

        }
    }

    private String leave() throws ClientException{
        try{
            state = State.SIGNED_IN;
            ws.leaveGame(authToken, authTokenToGameID.get(authToken));
            authTokenToGameID.remove(authToken);
            return "Exited game";
        }
        catch(ResponseException ex){
            throw new ClientException(500, "Error: Server error");
        }
    }

    private String observeGame(String... params) throws ClientException{
        try{
            if(params.length == 1){
                try{
                    int gameNum = Integer.parseInt(params[0]);
                    String name = listNumToName.get(gameNum);
                    if(name == null){
                        throw new ClientException(405, "Error: Invalid game");
                    }
                    state = State.GAME;
                    String[] args = { "white" };
                    Chessboard.main(args);
                    return String.format("Observing game #%s: %s", params[0], listNumToName.get(Integer.parseInt(params[0])));
                }
                catch(NumberFormatException ex){
                    throw new ClientException(413, "Error: bad input (gave string, but should have been integer)");
                }
        }
            throw new ClientException(407, "Error: Bad input for observe");
        }
        catch(ClientException ex){
            return exceptionHandler(ex);
        }
    }

    private String joinGame(String... params) throws ClientException{
        try{
            isSignedIn();
            if(params.length == 2){
                try{
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
                        throw new ClientException(406, "Error: Invalid color");
                    }

                    server.joinGame(playerColor, gameID);

                    authTokenToGameID.put(authToken, gameID);

                    state = State.GAME;
                    String[] args = { params[0] };
                    Chessboard.main(args);
                    return "Joining game";
                }
                catch(NullPointerException ex){
                    throw new ClientException(412, "Error: bad input (invalid game)");
                }
                catch(NumberFormatException ex){
                    throw new ClientException(413, "Error: bad input (gave string, but should have been integer)");
                }
            }
            throw new ClientException(408, "Error: Bad input for join");
        }
        catch(ClientException ex){
            return exceptionHandler(ex);
        }
    }

    private String listGames() throws ClientException{
        try{
            isSignedIn();
            ListGamesResponse gamesList = server.listGames();
            Collection<ListGamesData> games = gamesList.games();
            return clientListBuilder(games);
        }
        catch(ClientException ex){
            return exceptionHandler(ex);
        }
    }

    private String createGame(String... params) throws ClientException{
        try{
            isSignedIn();
            if(params.length == 1){
                server.createGame(params[0]);
                return String.format("Game \"%s\" created successfully", params[0]);
            }
            throw new ClientException(409, "Error: Invalid input");
        }
        catch(ClientException ex){
            return exceptionHandler(ex);
        }
    }

    private String logout() throws ClientException{
        try{
            isSignedIn();
            server.logout();
            state = State.SIGNED_OUT;
            return "Logged out successfully";
        }
        catch(ClientException ex){
            return exceptionHandler(ex);
        }
    }

    private String register(String... params) throws ClientException{
        try{
            if(params.length == 3){
                AuthData newUserData = server.register(params[0], params[1], params[2]);
                authToken = newUserData.authToken();
                username = newUserData.username();
                state = State.SIGNED_IN;
                return String.format("Welcome to the server, %s!\n", username);
            }
            throw new ClientException(411, "Error: Invalid input");
        }
        catch(ClientException ex){
            return exceptionHandler(ex);
        }
    }

    private String login(String... params) throws ClientException{
        try{
            if(params.length == 2){

                AuthData userData = server.login(params[0], params[1]);
                authToken = userData.authToken();
                username = userData.username();
                state = State.SIGNED_IN;
                return String.format("Welcome back, %s!\n", username);
            }
            throw new ClientException(410, "Error: Invalid input");
        }
        catch(ClientException ex){
            return exceptionHandler(ex);
        }
    }

    private String help() {
        if(state == State.SIGNED_OUT){
            return """
                    - login (<username> <password>)
                    - register (<username> <password> <email>)
                    - quit
                    """;
        }
        else if(state == State.SIGNED_IN){
            return """
                    - logout
                    - create (<game name>)
                    - join (<player color> <game number>)
                    - list
                    - observe (<game number>)
                    """;
        }
        else {
            return """
                   - leave
                   - redraw
                   - makeMove (<start position (ColRow)> <end position (ColRow)>)
                   - resign
                   - highlight (<piece position (ColRow)>)
                   - help
                   """;
        }
    }

    private String clientListBuilder(Collection<ListGamesData> games) {
        if(games.isEmpty()){
            return "No current games";
        }
        StringBuilder buildList = new StringBuilder();
        int i = 1;
        for(ListGamesData game : games){
            buildList.append(i)
                    .append(". ")
                    .append(game.gameName())
                    .append(EscapeSequences.SET_TEXT_BOLD + " | ")
                    .append("White player: ");
            if(game.whiteUsername() == null){
                buildList.append("None ");
            }
            else {
                buildList.append(game.whiteUsername()).append(" ");
            }

            buildList.append(EscapeSequences.SET_TEXT_BOLD + "| ")
                    .append("Black player: ");
            if(game.blackUsername() == null){
                buildList.append("None ");
            }
            else{
                buildList.append(game.blackUsername()).append(" ");
            }

            buildList.append("\n");

            gameIDToListNum.put(i, game.gameID());
            listNumToName.put(i, game.gameName());
            i += 1;
        }
        return buildList.toString();
    }
}

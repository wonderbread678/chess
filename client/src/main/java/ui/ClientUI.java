package ui;

import java.util.*;

import chess.*;
import client.*;
import clientwebsocket.NotificationHandler;
import clientwebsocket.WebsocketFacade;
import model.AuthData;
import model.GameData;
import model.ListGamesData;
import model.ListGamesResponse;
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
    private final Chessboard boardDrawer;

    private ChessBoard gameBoard;
    private GameData gameData;
    private ChessGame.TeamColor currentColor;

    public ClientUI(String serverURL) throws ClientException {
        server = new ServerFacade(serverURL);
        ws = new WebsocketFacade(serverURL, this);
        boardDrawer = new Chessboard();
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
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        printPrompt();
    }

    @Override
    public void notifyGame(LoadGameMessage loadGameMessage){
        gameData = loadGameMessage.getGame();
        gameBoard = loadGameMessage.getGame().game().getBoard();
        System.out.println();
        boardDrawer.makeBoard(currentColor, gameBoard);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        printPrompt();
    }

    @Override
    public void notifyError(ErrorMessage errorMessage){
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + errorMessage.getErrorMessage());
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
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
                    case "move" -> makeMove(params);
                    case "confirm" -> resign();
                    case "resign" -> "Are you sure? Type \"confirm\" to resign\n";
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
            case 414 -> "Move contains invalid character";
            case 415 -> "No piece at that position";
            case 416 -> "Invalid promotion piece";
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
            if(params.length == 1){
                String positionString = params[0];
                ChessPosition position = positionConverter(positionString);
                if(gameBoard.getPiece(position) == null){
                    throw new ClientException(415, "Error: No piece aat that position");
                }
                ChessGame game = gameData.game();
                boardDrawer.highlightMoves(gameBoard, currentColor, position, game);
                return String.format("Displaying moves for %s", positionString);
            }
            return "Invalid input. Should be \"highlight <game square>\"";
        }
        catch(NullPointerException ex){
            throw new ClientException(415, "No piece at this position");
        }
        catch(ClientException ex){
            return exceptionHandler(ex);
        }
    }

    private String redraw() throws ClientException{
       boardDrawer.makeBoard(currentColor, gameBoard);
       return "Redrawing board";
    }

    private String resign() throws ClientException{
        try{
            ws.resignGame(authToken, authTokenToGameID.get(authToken));
            return "You have resigned";
        }
        catch(ClientException ex){
            throw new ClientException(500, "Error: Server error");
        }
    }

    private String makeMove(String...params) throws ClientException{
        try{
            if(params.length == 2){
                String startString = params[0];
                String endString = params[1];
                ChessPosition startPosition = positionConverter(startString);
                ChessPosition endPosition = positionConverter(endString);
                ChessPiece.PieceType promotionType = null;
                if(currentColor == ChessGame.TeamColor.WHITE){
                    if(gameBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN &&
                    endPosition.getRow() == 8){
                        promotionType = pawnPromotion();
                    }
                }
                else{
                    if(gameBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN &&
                            endPosition.getRow() == 1){
                        promotionType = pawnPromotion();
                    }
                }
                ChessMove move = new ChessMove(startPosition, endPosition, promotionType);
                ws.makeMove(authToken, authTokenToGameID.get(authToken), move);
                return String.format("Move: %s to %s", startString, endString);
            }
            return "Invalid input. Should be \"move <start position> <end position>\"";
        }
        catch(NullPointerException ex){
            throw new ClientException(415, "Error: No piece at this position");
        }
        catch(ClientException ex){
            return exceptionHandler(ex);
        }
    }

    private ChessPiece.PieceType pawnPromotion(){
        try{
            System.out.println("Your pawn can be promoted! Which piece type will you choose?");
            System.out.print("- queen\n- rook\n- knight\n- bishop\n");
            Scanner scanner = new Scanner(System.in);
            System.out.print("\n" + ">>> " + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            String line = scanner.nextLine();

            return switch(line.toLowerCase()){
                case "queen" -> ChessPiece.PieceType.QUEEN;
                case "rook" -> ChessPiece.PieceType.ROOK;
                case "knight" -> ChessPiece.PieceType.KNIGHT;
                case "bishop" -> ChessPiece.PieceType.BISHOP;
                default -> throw new ClientException(416, "Error: Invalid promotion piece");
            };
        }
        catch(ClientException ex){
            System.out.println("Error: Invalid promotion piece. Try again\n");
            pawnPromotion();
        }

        return null;
    }

    private ChessPosition positionConverter(String posString) throws ClientException{
        List<Integer> positionVals = new ArrayList<>();
        for (int i = 0; i < posString.length(); ++i){
            char character = posString.charAt(i);
            if(!Character.isDigit(character) && Character.isLetter(character)){
                switch(Character.toLowerCase(character)){
                    case 'a' -> positionVals.add(1);
                    case 'b' -> positionVals.add(2);
                    case 'c' -> positionVals.add(3);
                    case 'd' -> positionVals.add(4);
                    case 'e' -> positionVals.add(5);
                    case 'f' -> positionVals.add(6);
                    case 'g' -> positionVals.add(7);
                    case 'h' -> positionVals.add(8);
                }
            }
            else if(Character.isDigit(character)){
                int pos = Character.getNumericValue(character);
                if(pos < 9 && pos > 0){
                    positionVals.add(pos);
                }
            }
            else{
                throw new ClientException(414, "Error: Move contains invalid character");
            }
        }
        return new ChessPosition(positionVals.get(1), positionVals.get(0));
    }

    private String leave() throws ClientException{
        try{
            ws.leaveGame(authToken, authTokenToGameID.get(authToken));
            state = State.SIGNED_IN;
            authTokenToGameID.remove(authToken);
            return "Exited game";
        }
        catch(ClientException ex){
            return exceptionHandler(ex);
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
                    currentColor = ChessGame.TeamColor.WHITE;
                    authTokenToGameID.put(authToken, gameIDToListNum.get(gameNum));
                    ws.connectGame(authToken, gameIDToListNum.get(gameNum));
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
                    if (params[0].equals("black")){
                        currentColor = ChessGame.TeamColor.BLACK;
                    }
                    else if(params[0].equals("white")){
                        currentColor = ChessGame.TeamColor.WHITE;
                    }
                    else{
                        throw new ClientException(406, "Error: Invalid color");
                    }

                    server.joinGame(currentColor, gameID);

                    authTokenToGameID.put(authToken, gameID);

                    state = State.GAME;
                    ws.connectGame(authToken, gameID);
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

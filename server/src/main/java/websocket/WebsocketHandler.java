package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.ResponseException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler{

    private final ConnectionManager connections = new ConnectionManager();
    private final SQLGameDAO gameDAO;
    private final SQLUserDAO userDAO;
    private final SQLAuthDAO authDAO;

    public WebsocketHandler(SQLGameDAO gameDAO, SQLAuthDAO authDAO, SQLUserDAO userDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    @Override
    public void handleConnect(WsConnectContext ctx){
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx){
        try {
            UserGameCommand userCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (userCommand.getCommandType()) {
                case CONNECT -> connectWS(userCommand, ctx.session);
                case MAKE_MOVE -> makeMoveWS(ctx.message(), ctx.session);
                case LEAVE -> leaveWS(userCommand, ctx.session);
                case RESIGN -> resignWS(userCommand, ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx){
        System.out.print("Websocket closed");
    }

    public void connectWS(UserGameCommand userCommand, Session session) throws IOException{
        try{
            connections.add(userCommand.getGameID(), session);
            AuthData user = authDAO.getAuth(userCommand.getAuthToken());
            GameData game = gameDAO.getGame(userCommand.getGameID());
            String messageString;
            if(game.blackUsername().equals(user.username())){
                messageString = String.format("%s has joined game as black", user.username());
            }
            else if (game.whiteUsername().equals(user.username())){
                messageString = String.format("%s has joined game as white", user.username());
            }
            else{
                messageString = String.format("%s has joined game as an observer", user.username());
            }
            var message = new NotificationMessage(messageString);
            String notification = new Gson().toJson(message);
            connections.gameBroadcast(userCommand.getGameID(), session, notification);

            String loadGameMessage = new Gson().toJson(new LoadGameMessage(game));
            session.getRemote().sendString(loadGameMessage);
        }
        catch(DataAccessException ex){
            sendError("Error: Server error", session);
        }
        catch(NullPointerException ex){
            sendError("Error: Game does not exist", session);
        }
    }

    public void makeMoveWS(String ctxMessage, Session session) throws IOException{
        try{
            MakeMoveCommand moveCommand = new Gson().fromJson(ctxMessage, MakeMoveCommand.class);
            GameData game = gameDAO.getGame(moveCommand.getGameID());
            AuthData user = authDAO.getAuth(moveCommand.getAuthToken());
            if(user == null){
                throw new ResponseException(401, "Error: Unauthorized");
            }
            moveCheck(user, game);

            ChessGame.TeamColor playerColor = null;
            if(user.username().equals(game.whiteUsername())){
                playerColor = ChessGame.TeamColor.WHITE;
            }
            else if(user.username().equals(game.blackUsername())){
                playerColor = ChessGame.TeamColor.BLACK;
            }

            if(gameDAO.getGameState(game.gameID()).equals("FINISHED")){
                throw new InvalidMoveException();
            }
            game.game().makeMove(moveCommand.getMove());
            gameDAO.updateGameBoard(game.gameID(), game.game());

            String loadGameMessage = new Gson().toJson(new LoadGameMessage(game));
            connections.gameBroadcast(moveCommand.getGameID(), null, loadGameMessage);

            var moveMessage = String.format("%s has made the move: %s", user.username(), moveCommand.getMove().toString());
            NotificationMessage notification = new NotificationMessage(moveMessage);
            connections.gameBroadcast(moveCommand.getGameID(), session, new Gson().toJson(notification));

            String mateMessage = mateCheck(user.username(), game.game().getTeamTurn(), game.game());
            if(mateMessage != null){
                NotificationMessage mateNotification = new NotificationMessage(mateMessage);
                connections.gameBroadcast(moveCommand.getGameID(), null, new Gson().toJson(mateNotification));
            }
            if(playerColor != null){
                if(game.game().isInStalemate(playerColor) || game.game().isInCheckmate(playerColor)){
                    gameDAO.updateGameState(game.gameID(), "FINISHED");
                }
            }

        }
        catch(DataAccessException ex){
            sendError("Error: Server error", session);
        }
        catch(InvalidMoveException ex){
            sendError("Error: invalid move", session);
        }
        catch(ResponseException ex){
            sendError("Error: Unauthorized", session);
        }
    }

    private static void moveCheck(AuthData user, GameData game) throws InvalidMoveException {
        if(!user.username().equals(game.whiteUsername()) && !user.username().equals(game.blackUsername())){
            throw new InvalidMoveException();
        }
        if(user.username().equals(game.whiteUsername()) && game.game().getTeamTurn() != ChessGame.TeamColor.WHITE){
            throw new InvalidMoveException();
        }
        if(user.username().equals(game.blackUsername()) && game.game().getTeamTurn() != ChessGame.TeamColor.BLACK){
            throw new InvalidMoveException();
        }
    }

    public void leaveWS(UserGameCommand userCommand, Session session) throws IOException{
        try{
            GameData game = gameDAO.getGame(userCommand.getGameID());
            AuthData user = authDAO.getAuth(userCommand.getAuthToken());
            String message = String.format("%s has left the game", user.username());
            NotificationMessage notification = new NotificationMessage(message);
            connections.gameBroadcast(userCommand.getGameID(), session, new Gson().toJson(notification));
            connections.remove(userCommand.getGameID(), session);
            if(game.whiteUsername().equals(user.username())){
                gameDAO.updateGamePlayers(userCommand.getGameID(), null, game.blackUsername());
            }
            else if(game.blackUsername().equals(user.username())){
                gameDAO.updateGamePlayers(userCommand.getGameID(), game.whiteUsername(), null);
            }
        }
        catch(DataAccessException ex){
            sendError("Error: server error", session);
        }
    }

    public void resignWS(UserGameCommand userCommand, Session session) throws IOException{
        try{
            GameData game = gameDAO.getGame(userCommand.getGameID());
            AuthData user = authDAO.getAuth(userCommand.getAuthToken());
            if(user == null){
                throw new ResponseException(401, "Error: Unauthorized");
            }
            if(!game.whiteUsername().equals(user.username()) && !game.blackUsername().equals(user.username())){
                throw new ResponseException(485, "Error: Observers can't resign");
            }
            if(gameDAO.getGameState(userCommand.getGameID()).equals("FINISHED")){
                throw new ResponseException(484, "Error: Double resign");
            }
            String message = String.format("%s has resigned", user.username());
            NotificationMessage notification = new NotificationMessage(message);
            connections.gameBroadcast(userCommand.getGameID(), null, new Gson().toJson(notification));
            gameDAO.updateGameState(userCommand.getGameID(), "FINISHED");
        }
        catch(DataAccessException ex){
            sendError("Error: Server error", session);
        }
        catch(ResponseException ex){
            if(ex.getCode() == 401){
                sendError("Error: Unauthorized", session);
            }
            else if(ex.getCode() == 484){
                sendError("Error: Game already over", session);
            }
            else{
                sendError("Error: Observers can't resign", session);
            }
        }
    }

    public String mateCheck(String username, ChessGame.TeamColor playerColor, ChessGame game){
        String message = null;
        if(game.isInCheck(playerColor)){
            message = String.format("%s is in check!", username);
        }
        else if(game.isInCheckmate(playerColor)){
            message = String.format("%s is in checkmate!", username);
        }
        else if(game.isInStalemate(playerColor)){
            message = "Stalemate";
        }
        return message;
    }

    public void sendError(String error, Session session) throws IOException{
        ErrorMessage errorMessage = new ErrorMessage(error);
        String errorString = new Gson().toJson(errorMessage);
        session.getRemote().sendString(errorString);
    }

}

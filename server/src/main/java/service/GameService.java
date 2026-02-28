package service;
import chess.ChessGame;
import dataaccess.*;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryGameDAO;
import model.*;
import server.Response.CreateGameResponse;
import server.Response.ListGamesResponse;
import server.ResponseException;
import server.*;

import java.util.ArrayList;
import java.util.HashMap;

public class GameService {

    private final MemoryGameDAO gameDAO;
    private final MemoryAuthDAO authDAO;

    public GameService(MemoryGameDAO gameDAO, MemoryAuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        authService = new AuthService(authDAO);

    }
    private final AuthService authService;


    public String getListGames(String authToken) throws ResponseException {
            if(!authService.isAuth(authToken)){
                throw new ResponseException(401, "Error: Unauthorized");
            }
            ListGamesResponse reformatter = new ListGamesResponse();
            return reformatter.toJson(gameDAO);
    }

    public CreateGameResponse createGame(String authToken, String gameName) throws ResponseException{
        try{
            if(!authService.isAuth(authToken)){
                throw new ResponseException(401, "Error: Unauthorized");
            }
            if(gameName == null){
                throw new ResponseException(403, "Error: Bad request (no game name provided");
            }
            int id = makeGameID();
            GameData newGame = new GameData(id, null, null, gameName, new ChessGame());
            GameData createdGame = gameDAO.createGame(newGame);
            return new CreateGameResponse(createdGame.gameID());
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws ResponseException{
        try {
            if(!authService.isAuth(authToken)){
                throw new ResponseException(401, "Error: Unauthorized");
            }
            GameData game = gameDAO.getGame(gameID);
            if(game == null){
                throw new ResponseException(400, "Error: Bad ID");
            }
            AuthData auth = authDAO.getAuth(authToken);
            if(color != ChessGame.TeamColor.BLACK && color != ChessGame.TeamColor.WHITE){
                throw new ResponseException(400, "Error: Bad request (Invalid Color)");
            }
            if (isColorAvailable(color, game)) {
                if (color == ChessGame.TeamColor.WHITE) {
                    gameDAO.updateGamePlayers(game, auth.username(), game.blackUsername());
                }
                if (color == ChessGame.TeamColor.BLACK) {
                    gameDAO.updateGamePlayers(game, game.whiteUsername(), auth.username());
                }
            }
            else if(!isColorAvailable(color, game)){
                throw new ResponseException(403, "Error: Color already taken");
            }
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public int makeGameID() throws ResponseException{
        try{
            return gameDAO.listGames().size() + 1;
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public boolean isColorAvailable(ChessGame.TeamColor color, GameData game){
        if(color == ChessGame.TeamColor.WHITE){
            return game.whiteUsername() == null;
        }
        else if(color == ChessGame.TeamColor.BLACK){
            return game.blackUsername() == null;
        }
        return false;
    }

    public void deleteAllGames() throws ResponseException{
        try{
            gameDAO.deleteAllGames();
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }



}

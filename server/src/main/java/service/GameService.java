package service;
import chess.ChessGame;
import dataaccess.*;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryGameDAO;
import model.*;
import server.ResponseException;

import java.util.HashMap;

public class GameService {

    private final MemoryGameDAO gameDAO;
    private final MemoryAuthDAO authDAO;
    private final AuthService authService = new AuthService(new MemoryAuthDAO());

    public GameService(MemoryGameDAO gameDAO, MemoryAuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public HashMap<Integer, GameData> getListGames(String authToken) throws ResponseException {
        try{
            if(!authService.isAuth(authToken)){
                throw new ResponseException(401, "Error: Unauthorized");
            }
            return gameDAO.listGames();
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public GameData createGame(String authToken, String gameName) throws ResponseException{
        try{
            if(!authService.isAuth(authToken)){
                throw new ResponseException(401, "Error: Unauthorized");
            }
            int id = makeGameID();
            GameData newGame = new GameData(id, null, null, gameName, new ChessGame());
            return gameDAO.createGame(newGame);
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
            AuthData auth = authDAO.getAuth(authToken);
            if (isColorAvailable(color, game)) {
                if (color == ChessGame.TeamColor.WHITE) {
                    gameDAO.updateGamePlayers(game, auth.username(), null);
                } else if (color == ChessGame.TeamColor.BLACK) {
                    gameDAO.updateGamePlayers(game, null, auth.username());
                }
            }
            else{
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

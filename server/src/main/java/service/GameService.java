package service;
import chess.ChessGame;
import dataaccess.*;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryGameDAO;
import dataaccess.Memory.MemoryUserDAO;
import model.*;

import java.util.HashMap;

public class GameService {

    private final MemoryGameDAO gameDAO;
    private final MemoryUserDAO userDAO;
    private final MemoryAuthDAO authDAO;
    private final AuthService authService = new AuthService(new MemoryAuthDAO());

    public GameService(MemoryGameDAO gameDAO, MemoryUserDAO userDAO, MemoryAuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public HashMap<Integer, GameData> getListGames(String authToken) throws DataAccessException{
        return gameDAO.listGames();
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException{
        int id = makeGameID();
        GameData newGame = new GameData(id, null, null, gameName, new ChessGame());
        return gameDAO.createGame(newGame);
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws DataAccessException{
        GameData game = gameDAO.getGame(gameID);
        AuthData auth = authDAO.getAuth(authToken);
        if(isColorAvailable(color, game)){
            if(color == ChessGame.TeamColor.WHITE){

            }
            else if(color == ChessGame.TeamColor.BLACK){

            }
        }
    }

    public int makeGameID() throws DataAccessException{
        return gameDAO.listGames().size() + 1;
    }

    public boolean isColorAvailable(ChessGame.TeamColor color, GameData game) throws DataAccessException{
        if(color == ChessGame.TeamColor.WHITE){
            return game.whiteUsername() == null;
        }
        else if(color == ChessGame.TeamColor.BLACK){
            return game.blackUsername() == null;
        }
        return false;
    }



}

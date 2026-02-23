package dataaccess;

import model.*;
import java.util.HashMap;

public interface GameDAO {

    GameData createGame(GameData gameData) throws DataAccessException;

    HashMap<Integer, GameData> listGames() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGamePlayers(GameData game, String whiteUsername, String blackUsername) throws DataAccessException;

    void deleteAllGames() throws DataAccessException;
}

package dataaccess;

import model.GameData;

import java.util.HashMap;

public class SQLGameDAO implements GameDAO {
    public GameData createGame(GameData gameData) throws DataAccessException {
        return null;
    }

    public HashMap<Integer, GameData> listGames() throws DataAccessException {
        return null;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    public void updateGamePlayers(GameData game, String whiteUsername, String blackUsername) throws DataAccessException {

    }

    public void deleteAllGames() throws DataAccessException {

    }
}

package dataaccess.memory;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.*;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, GameData> allGames = new HashMap<>();

    public GameData createGame(GameData gameData) throws DataAccessException {
        allGames.put(gameData.gameID(), gameData);
        return gameData;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return allGames.get(gameID);
    }

    public HashMap<Integer, GameData> listGames() throws DataAccessException {
        return allGames;
    }

    public void updateGamePlayers(GameData game, String whiteUsername, String blackUsername) throws DataAccessException {
        GameData updatedGame = new GameData(game.gameID(), whiteUsername, blackUsername, game.gameName(), game.game());
        allGames.remove(game.gameID());
        allGames.put(game.gameID(), updatedGame);
    }

    public void deleteAllGames() throws DataAccessException {
        allGames.clear();
    }
}

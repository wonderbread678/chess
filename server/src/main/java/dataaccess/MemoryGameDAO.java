package dataaccess;
import model.*;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> allGames = new HashMap<>();

    public GameData createGame(GameData gameData) throws DataAccessException {
        gameData = new GameData(
                gameData.gameID(),
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                gameData.game());

        allGames.put(gameData.gameID(), gameData);
        return gameData;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return allGames.get(gameID);
    }

    public HashMap<Integer, GameData> listGames() throws DataAccessException {
        return allGames;
    }

    public void updateGame() throws DataAccessException {

    }

    public void deleteAllGames() throws DataAccessException {
        allGames.clear();
    }
}

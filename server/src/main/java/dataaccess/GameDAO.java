package dataaccess;

import model.*;

public interface GameDAO {

    GameData createGame() throws DataAccessException;

    GameData listGames() throws DataAccessException;

    GameData getGame() throws DataAccessException;

    void updateGame() throws DataAccessException;
}

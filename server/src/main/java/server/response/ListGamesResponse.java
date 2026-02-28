package server.response;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.memory.MemoryGameDAO;
import model.GameData;
import model.ListGamesData;
import server.ResponseException;

import java.util.ArrayList;
import java.util.HashMap;

public class ListGamesResponse {

    public HashMap<String, ArrayList<ListGamesData>> reformatGameList(MemoryGameDAO gameDAO) throws ResponseException {
        try{
            ArrayList<ListGamesData> newGamesList = new ArrayList<>();
            HashMap<Integer, GameData> gamesList = gameDAO.listGames();
            HashMap<String, ArrayList<ListGamesData>> reformatedGamesList = new HashMap<>();
            if(gamesList.isEmpty()){
                reformatedGamesList.put("games", newGamesList);
                return reformatedGamesList;
            }
            gamesList.forEach((key, game) -> {
                ListGamesData gameData = new ListGamesData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
                newGamesList.add(gameData);
            });
            reformatedGamesList.put("games", newGamesList);
            return reformatedGamesList;
        }
        catch(DataAccessException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public String toJson(MemoryGameDAO gameDAO) throws ResponseException{
        return new Gson().toJson(reformatGameList(gameDAO));
    }
}

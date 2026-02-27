package model;

import com.google.gson.Gson;

public record ListGamesData(int gameID,
                            String whiteUsername,
                            String blackUsername,
                            String gameName) {
    public String toString() {
        return new Gson().toJson(this);
    }
}

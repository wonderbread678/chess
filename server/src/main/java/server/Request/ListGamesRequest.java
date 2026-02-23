package server.Request;

import com.google.gson.Gson;

public record ListGamesRequest(String authToken) {
    public String toString() {
        return new Gson().toJson(this);
    }
}

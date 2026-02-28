package server.request;

import com.google.gson.Gson;

public record CreateGameRequest(String authToken, String gameName) {
    public String toString() {
        return new Gson().toJson(this);
    }
}

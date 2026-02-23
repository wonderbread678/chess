package server.Request;

import com.google.gson.Gson;

public record ClearRequest() {
    public String toString() {
        return new Gson().toJson(this);
    }
}

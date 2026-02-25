package server;

import com.google.gson.Gson;

import java.util.Map;

public class ResponseException extends Exception {

    private final int code;

    public ResponseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String toJson(){
        return new Gson().toJson(Map.of("message", getMessage()));
    }
}

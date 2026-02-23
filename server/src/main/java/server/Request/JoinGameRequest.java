package server.Request;

import chess.ChessGame;
import com.google.gson.Gson;

public record JoinGameRequest(String authToken, ChessGame.TeamColor color, int gameID) {
    public String toString() {
        return new Gson().toJson(this);
    }
}

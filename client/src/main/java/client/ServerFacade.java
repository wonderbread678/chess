package client;


import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import server.ResponseException;
import server.request.*;
import server.response.CreateGameResponse;

import java.net.*;
import java.net.http.*;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private String authToken = null;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public AuthData register(String username, String password, String email) throws ResponseException{
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        var response = new Client_Communicate().postMethod(serverUrl, "/user", new Gson().toJson(registerRequest), null);

        AuthData converted_response = handleResponse(response, AuthData.class);
        authToken = converted_response.authToken();

        return converted_response;
    }

    public AuthData login(String username, String password) throws ResponseException{
        LoginRequest loginRequest = new LoginRequest(username, password);
        var response = new Client_Communicate().postMethod(serverUrl, "/session", new Gson().toJson(loginRequest), null);

        AuthData converted_response = handleResponse(response, AuthData.class);
        authToken = converted_response.authToken();

        return converted_response;
    }

    public void logout() throws ResponseException{
        if(authToken == null){
            throw new ResponseException(401, "Error: Unauthorized");
        }
        new Client_Communicate().deleteMethod(serverUrl, "/session", authToken);
        authToken = null;
    }

    public ListGamesResponse listGames() throws ResponseException{
        var response = new Client_Communicate().getMethod(serverUrl, "/game", authToken);
        return handleResponse(response, ListGamesResponse.class);
    }

    public CreateGameResponse createGame(String gameName) throws ResponseException{
        if(authToken == null){
            throw new ResponseException(401, "Error: Unauthorized");
        }
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
        var response = new Client_Communicate().postMethod(serverUrl, "/game", new Gson().toJson(createGameRequest),authToken);
        return handleResponse(response, CreateGameResponse.class);
    }

    public void joinGame(ChessGame.TeamColor playerColor, int gameID) throws ResponseException{
        JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID);
        new Client_Communicate().putMethod(serverUrl, "/game", new Gson().toJson(joinGameRequest), authToken);
    }

    public void clear() throws ResponseException{
        new Client_Communicate().deleteMethod(serverUrl, "/db", null);
    }


    public String getAuthToken(){
        return authToken;
    }


    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new ResponseException(status, body);
            }

            throw new ResponseException(status, "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}

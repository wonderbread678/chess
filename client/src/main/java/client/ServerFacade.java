package client;


import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import server.ResponseException;
import server.request.*;
import server.response.CreateGameResponse;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private final HashMap<String, String> authTokens = new HashMap<>();

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public AuthData register(String username, String password, String email) throws ResponseException{
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        var response = new Client_Communicate().postMethod(serverUrl, "/user", new Gson().toJson(registerRequest), null);

        AuthData converted_response = handleResponse(response, AuthData.class);
        authTokens.put(username, converted_response.authToken());

        return converted_response;
    }

    public AuthData login(String username, String password) throws ResponseException{
        LoginRequest loginRequest = new LoginRequest(username, password);
        var response = new Client_Communicate().postMethod(serverUrl, "/session", new Gson().toJson(loginRequest), null);

        AuthData converted_response = handleResponse(response, AuthData.class);
        authTokens.put(username, converted_response.authToken());

        return converted_response;
    }

    public void logout(String username) throws ResponseException{
        LogoutRequest logoutRequest = new LogoutRequest(authTokens.get(username));
        new Client_Communicate().deleteMethod(serverUrl, "/session", authTokens.get(username));
    }

    public String listGames(String username) throws ResponseException{
        ListGamesRequest listGamesRequest = new ListGamesRequest(authTokens.get(username));
        var response = new Client_Communicate().getMethod(serverUrl, "/game", new Gson().toJson(listGamesRequest));
        return handleResponse(response, String.class);
    }

    public CreateGameResponse createGame(String gameName, String username) throws ResponseException{
        CreateGameRequest createGameRequest = new CreateGameRequest(authTokens.get(username), gameName);
        var response = new Client_Communicate().postMethod(serverUrl, "/game", new Gson().toJson(createGameRequest), authTokens.get(username));
        return handleResponse(response, CreateGameResponse.class);
    }

    public void joinGame(String username, ChessGame.TeamColor color, int gameNumber) throws ResponseException{
        JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameNumber);
        new Client_Communicate().putMethod(serverUrl, "/game", new Gson().toJson(joinGameRequest), authTokens.get(username));
    }

    public void clear() throws ResponseException{
        new Client_Communicate().deleteMethod(serverUrl, "/db", null);
    }

    private void exceptionHandler(ResponseException ex){

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

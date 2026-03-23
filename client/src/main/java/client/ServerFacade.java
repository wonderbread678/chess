package client;


import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import model.ListGamesResponse;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.CreateGameResponse;

import java.net.http.*;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private String authToken = null;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public AuthData register(String username, String password, String email) throws ClientException{
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        var response = new ClientCommunicate().postMethod(serverUrl, "/user", new Gson().toJson(registerRequest), null);

        AuthData convertedResponse = handleResponse(response, AuthData.class);
        authToken = convertedResponse.authToken();

        return convertedResponse;
    }

    public AuthData login(String username, String password) throws ClientException{
        LoginRequest loginRequest = new LoginRequest(username, password);
        var response = new ClientCommunicate().postMethod(serverUrl, "/session", new Gson().toJson(loginRequest), null);

        AuthData convertedResponse = handleResponse(response, AuthData.class);
        authToken = convertedResponse.authToken();

        return convertedResponse;
    }

    public void logout() throws ClientException{
        if(authToken == null){
            throw new ClientException(401, "Error: Unauthorized");
        }
        new ClientCommunicate().deleteMethod(serverUrl, "/session", authToken);
        authToken = null;
    }

    public ListGamesResponse listGames() throws ClientException{
        var response = new ClientCommunicate().getMethod(serverUrl, "/game", authToken);
        return handleResponse(response, ListGamesResponse.class);
    }

    public CreateGameResponse createGame(String gameName) throws ClientException{
        if(authToken == null){
            throw new ClientException(401, "Error: Unauthorized");
        }
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
        var response = new ClientCommunicate().postMethod(serverUrl, "/game", new Gson().toJson(createGameRequest),authToken);
        return handleResponse(response, CreateGameResponse.class);
    }

    public void joinGame(ChessGame.TeamColor playerColor, int gameID) throws ClientException{
        JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID);
        new ClientCommunicate().putMethod(serverUrl, "/game", new Gson().toJson(joinGameRequest), authToken);
    }

    public void clear() throws ClientException{
        new ClientCommunicate().deleteMethod(serverUrl, "/db", null);
    }


    public String getAuthToken(){
        return authToken;
    }


    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ClientException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new ClientException(status, body);
            }

            throw new ClientException(status, "other failure: " + status);
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

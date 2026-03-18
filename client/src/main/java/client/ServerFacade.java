package client;


import com.google.gson.Gson;
import model.*;
import server.ResponseException;
import server.request.LoginRequest;
import server.request.LogoutRequest;
import server.request.RegisterRequest;
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
        return handleResponse(response, AuthData.class);
    }

    public AuthData login(String username, String password) throws ResponseException{
        LoginRequest loginRequest = new LoginRequest(username, password);
        var response = new Client_Communicate().postMethod(serverUrl, "/session", new Gson().toJson(loginRequest), null);
        return handleResponse(response, AuthData.class);
    }

    public void logout() throws ResponseException{
        LogoutRequest logoutRequest = new LogoutRequest()
        handleResponse(response, null);
    }

    public String listGames() throws ResponseException{

        return handleResponse(response, String.class);
    }

    public CreateGameResponse createGame(String gameName) throws ResponseException{

        return handleResponse(response, CreateGameResponse.class);
    }

    public void joinGame() throws ResponseException{

        handleResponse(response, null);
    }

    public void clear() throws ResponseException{
        var request = buildRequest("DELETE", "/db", null);
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

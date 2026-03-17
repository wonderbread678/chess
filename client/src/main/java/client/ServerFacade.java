package client;


import com.google.gson.Gson;
import model.*;
import server.ResponseException;
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

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public AuthData register(String username, String password, String email) throws ResponseException{
        UserData newUser = new UserData(username, password, email);
        var request = buildRequest("POST", "/user", newUser);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData login(UserData user) throws ResponseException{
        var request = buildRequest("POST", "/session", user);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logout(String authToken) throws ResponseException{
        var request = buildRequest("DELETE", "/session", authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public String listGames() throws ResponseException{
        var request = buildRequest("GET", "/game", null);
        var response = sendRequest(request);
        return handleResponse(response, String.class);
    }

    public CreateGameResponse createGame(String gameName) throws ResponseException{
        var request = buildRequest("POST", "/game", gameName);
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResponse.class);
    }

    public void joinGame() throws ResponseException{
        var request = buildRequest("PUT", "/game", null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }



    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
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

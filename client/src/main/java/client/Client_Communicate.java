package client;

import com.google.gson.Gson;
import server.ResponseException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client_Communicate {
    private final HttpClient client = HttpClient.newHttpClient();

    public HttpResponse<String> getMethod(String serverUrl, String path, String authToken) throws ResponseException{
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + path))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .GET()
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch(Exception ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public HttpResponse<String> postMethod(String serverUrl, String path, String body, String authToken) throws ResponseException{
        try{
            HttpRequest.Builder request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + path))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json");
            if(authToken != null){
                request.header("authorization", authToken);
            }
            HttpRequest built_request = request.build();

            return client.send(built_request, HttpResponse.BodyHandlers.ofString());
        }
        catch(Exception ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }


    public void putMethod(String serverUrl, String path, String body, String authToken) throws ResponseException{
        try{
            Serializer converter = new Serializer();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + path))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .PUT(HttpRequest.BodyPublishers.ofString(converter.serialize(body)))
                    .header("Content-Type", "application/json")
                    .header("authorization", authToken)
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch(Exception ex){
            throw new ResponseException(500, ex.getMessage());
        }


    }

    public void deleteMethod(String serverUrl, String path, String authToken) throws ResponseException{
        try{
            HttpRequest.Builder request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + path))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .DELETE();
            if(authToken != null){
                request.header("authorization", authToken);
            }
            HttpRequest built_request = request.build();

            client.send(built_request, HttpResponse.BodyHandlers.ofString());
        }
        catch(Exception ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

//    private HttpRequest buildRequest(String method, String path, Object body) {
//        var request = HttpRequest.newBuilder()
//                .uri(URI.create(serverUrl + path))
//                .method(method, makeRequestBody(body));
//        if (body != null) {
//            request.setHeader("Content-Type", "application/json");
//        }
//        return request.build();
//    }
//
//    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
//        if (request != null) {
//            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
//        } else {
//            return HttpRequest.BodyPublishers.noBody();
//        }
//    }
//
//    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
//        try {
//            return client.send(request, HttpResponse.BodyHandlers.ofString());
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
//
//
//    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
//        var status = response.statusCode();
//        if (!isSuccessful(status)) {
//            var body = response.body();
//            if (body != null) {
//                throw new ResponseException(status, body);
//            }
//
//            throw new ResponseException(status, "other failure: " + status);
//        }
//
//        if (responseClass != null) {
//            return new Gson().fromJson(response.body(), responseClass);
//        }
//
//        return null;
//    }
//
//    private boolean isSuccessful(int status) {
//        return status / 100 == 2;
//    }
}

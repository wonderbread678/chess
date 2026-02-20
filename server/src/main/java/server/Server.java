package server;

import io.javalin.*;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.http.Context;
import model.*;
import service.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
            .post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .delete("/db", this::clearAll);
        // Register your endpoints and exception handlers here.

    }

    private void register(Context context) {
    }

    private void login(Context context) {
    }

    private void logout(Context context) {
    }

    private void listGames(Context context) {
    }

    private void createGame(Context context) {
    }

    private void joinGame(Context context) {
    }

    private void clearAll(Context context) {
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}

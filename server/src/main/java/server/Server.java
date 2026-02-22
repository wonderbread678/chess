package server;

import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryUserDAO;
import io.javalin.*;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.http.Context;
import model.*;
import service.*;

public class Server {

    private final Javalin javalin;
    private final UserService userService = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());


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

    private void register(Context ctx) throws DataAccessException{
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData registerResult = userService.createUser(user.username(), user.password(), user.email());
        ctx.result(new Gson().toJson(registerResult));
    }

    private void login(Context ctx) throws DataAccessException{
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData loginResult = userService.createAuth(user.username(), user.password());
        ctx.result(new Gson().toJson(loginResult));
    }

    private void logout(Context ctx) throws DataAccessException{
        String authToken = new Gson().fromJson(ctx.body(), String.class);
        userService.logout(authToken);
    }

    private void listGames(Context ctx) throws DataAccessException{

    }

    private void createGame(Context ctx) throws DataAccessException{

    }

    private void joinGame(Context ctx) throws DataAccessException{

    }

    private void clearAll(Context ctx) throws DataAccessException{

    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}

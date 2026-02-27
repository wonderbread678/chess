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
                .delete("/db", this::clearAll)
                .exception(ResponseException.class, this::exceptionHandler);

        // Register your endpoints and exception handlers here.

    }

    private void register(Context ctx) {
        try{
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        if(user.username() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        if(user.password() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        if(user.email() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        AuthData registerResult = userService.createUser(user.username(), user.password(), user.email());
        ctx.result(new Gson().toJson(registerResult));
        }
        catch (ResponseException ex){
            exceptionHandler(ex, ctx);
        }
    }

    private void login(Context ctx){
        try {
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);
            if(user.username() == null){
                throw new ResponseException(400, "Error: bad request");
            }
            if(user.password() == null){
                throw new ResponseException(400, "Error: bad request");
            }
            AuthData loginResult = userService.createAuth(user.username(), user.password());
            ctx.result(new Gson().toJson(loginResult));
        }
        catch(ResponseException ex){
            exceptionHandler(ex, ctx);
        }
    }

    private void logout(Context ctx) throws ResponseException{
        try {
            String authToken = new Gson().fromJson(ctx.header("authorization"), String.class);
            userService.logout(authToken);
        }
        catch(ResponseException ex){
            exceptionHandler(ex, ctx);
        }
    }

    private void listGames(Context ctx) throws ResponseException{
        try{

        }
        catch(ResponseException ex){
            exceptionHandler(ex, ctx);
        }
    }

    private void createGame(Context ctx) throws ResponseException{
        try{

        }
        catch(ResponseException ex){
            exceptionHandler(ex, ctx);
        }
    }

    private void joinGame(Context ctx) throws ResponseException{
        try{

        }
        catch(ResponseException ex){
            exceptionHandler(ex, ctx);
        }
    }

    private void clearAll(Context ctx) throws ResponseException{
        try{

        }
        catch(ResponseException ex){
            exceptionHandler(ex, ctx);
        }
    }

    private void exceptionHandler(ResponseException ex, Context ctx){
        ctx.status(ex.getCode());
        ctx.result(ex.toJson());
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}

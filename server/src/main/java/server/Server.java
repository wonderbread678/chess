package server;

import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryGameDAO;
import dataaccess.Memory.MemoryUserDAO;
import io.javalin.*;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.http.Context;
import model.*;
import server.Request.CreateGameRequest;
import server.Request.JoinGameRequest;
import service.*;

public class Server {

    private final Javalin javalin;
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private final MemoryUserDAO userDAO = new MemoryUserDAO();
    private final UserService userService = new UserService(authDAO, userDAO);
    private final GameService gameService = new GameService(gameDAO, authDAO);
    private final AuthService authService = new AuthService(authDAO);


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

    private void register(Context ctx) throws ResponseException{
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

    private void login(Context ctx) throws ResponseException{
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

    private void logout(Context ctx) throws ResponseException{
            String authToken = ctx.header("authorization");
            userService.logout(authToken);
    }

    private void listGames(Context ctx) throws ResponseException{
            String authToken = ctx.header("authorization");
            ctx.result(gameService.getListGames(authToken));
    }

    private void createGame(Context ctx) throws ResponseException{
            String authToken = ctx.header("authorization");
            CreateGameRequest createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
            if(createGameRequest.gameName() == null){
                throw new ResponseException(400, "Error: bad request (no gameName)");
            }
            ctx.result(new Gson().toJson(gameService.createGame(authToken, createGameRequest.gameName())));
    }

    private void joinGame(Context ctx) throws ResponseException{
            String authToken = ctx.header("authorization");
            server.Request.JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
            gameService.joinGame(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());
            ctx.result("{}");
    }

    private void clearAll(Context ctx) throws ResponseException{
            userService.deleteAllUsers();
            gameService.deleteAllGames();
            authService.deleteAllAuth();
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

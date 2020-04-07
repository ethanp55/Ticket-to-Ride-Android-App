package ateam.tickettoride.common.requests;

/**
 * Class representing a request to join a game.
 */

public class JoinGameRequest extends Request {
    //The ID of the game.
    private String gameID;
    //The name of the game.
    private String gameName;
    //The user's authorization token.
    private String authToken;

    public JoinGameRequest(String id, String name, String token){
        gameID = id;
        gameName = name;
        authToken = token;
    }

    public String getGameID(){
        return gameID;
    }

    public String getGameName(){
        return gameName;
    }

    public String getAuthToken(){
        return authToken;
    }
}

package ateam.tickettoride.common.requests;

/**
 * Class representing a request to start a game.
 */

public class StartGameRequest extends Request {
    //The ID of the game to be started.
    private String gameID;
    //The user's authorization token.
    private String authToken;

    public StartGameRequest(String id, String token){
        gameID = id;
        authToken = token;
    }

    public String getGameID(){
        return gameID;
    }

    public String getAuthToken(){
        return authToken;
    }
}

package ateam.tickettoride.common.requests;

/**
 * Class for requesting the creation of a game.
 */

public class CreateGameRequest extends Request {
    //The name of the game.
    private String gameName;
    //The maximum number of players allowed.
    private int maxPlayers;
    //The user's authorization token.
    private String authToken;

    public CreateGameRequest(String name, int numPlayers, String token){
        gameName = name;
        maxPlayers = numPlayers;
        authToken = token;
    }

    public String getName(){
        return gameName;
    }

    public int getMaxPlayers(){
        return maxPlayers;
    }

    public String getAuthToken(){
        return authToken;
    }
}

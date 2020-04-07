package ateam.tickettoride.common.responses;

import java.util.ArrayList;

import ateam.tickettoride.common.model.GameInfo;


/**
 * Class representing a response to a successful register request.
 */

public class RegisterResponse extends Response {
    //The user's new authorization token.
    private String authToken;
    //The user's username.
    private String username;
    //The list of games for the game browser.
    private ArrayList<GameInfo> unstartedGames;

    public RegisterResponse(String token, String name, ArrayList<GameInfo> games){
        authToken = token;
        username = name;
        unstartedGames = games;
    }

    public String getAuthToken(){
        return authToken;
    }

    public ArrayList<GameInfo> getUnstartedGames() {
        return unstartedGames;
    }

    public String getUsername() {
        return username;
    }
}

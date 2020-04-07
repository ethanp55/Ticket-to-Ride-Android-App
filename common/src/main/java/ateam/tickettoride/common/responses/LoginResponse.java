package ateam.tickettoride.common.responses;

import java.util.ArrayList;


import ateam.tickettoride.common.model.GameInfo;
import ateam.tickettoride.common.responses.Response;

/**
 * Class representing a response to a successful login request.
 */

public class LoginResponse extends Response {
    //The user's new authorization token.
    private String authToken;
    //The user's username.
    private String username;
    //The list of games in the game browser.
    private ArrayList<GameInfo> unstartedGames;

    public LoginResponse(String token, String name, ArrayList<GameInfo> games){
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

package ateam.tickettoride.common.requests;

/**
 * Class representing a request for updates to the game browser.
 */

public class UpdateGameBrowserRequest extends Request {
    //The number of the next command to start processing.
    private int nextCommandNumber;
    //The user's authorization token.
    private String authToken;

    public UpdateGameBrowserRequest(String token, int nextCmd){
        nextCommandNumber = nextCmd;
        authToken = token;
    }

    public String getAuthToken(){
        return authToken;
    }

    public int getNextCommandNumber(){
        return nextCommandNumber;
    }
}

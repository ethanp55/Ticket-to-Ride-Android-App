package ateam.tickettoride.common.requests;


/**
 * Class representing a request to update the game.
 */
public class UpdateGameRequest extends Request{
    //The number of the next command to start processing.
    private int nextCommandNumber;
    //The user's authorization token.
    private String authToken;
    //The ID of the game the user is in.
    private String gameID;

    public UpdateGameRequest(int nextCommandNumber, String authToken, String gameID) {
        this.nextCommandNumber = nextCommandNumber;
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public int getNextCommandNumber() {
        return nextCommandNumber;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getGameID() {
        return gameID;
    }
}

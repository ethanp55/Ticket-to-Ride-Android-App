package ateam.tickettoride.common.requests;

public class ChatRequest extends Request {
    //Authorization token of the player sending the chat message
    private String authToken;
    //ID of the game that the chat message belongs to
    private String gameID;
    //The message that is being sent
    private String message;

    public ChatRequest(String authToken, String gameID, String message) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

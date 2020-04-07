package ateam.tickettoride.common.requests;

public class DrawDestinationCardRequest extends Request {
    private String authToken;
    private String gameID;

    public DrawDestinationCardRequest(String authToken, String gameID) {
        this.authToken = authToken;
        this.gameID = gameID;
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
}

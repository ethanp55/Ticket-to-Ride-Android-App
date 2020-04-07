package ateam.tickettoride.common.requests;

public class LongestPathRequest extends Request {
    private String authToken;
    private String gameID;
    private int longestPath;

    public LongestPathRequest(String authToken, String gameID, int longestPath) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.longestPath = longestPath;
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

    public int getLongestPath() {
        return longestPath;
    }

    public void setLongestPath(int longestPath) {
        this.longestPath = longestPath;
    }
}

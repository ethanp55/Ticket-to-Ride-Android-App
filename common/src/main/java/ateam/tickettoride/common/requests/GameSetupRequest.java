package ateam.tickettoride.common.requests;

public class GameSetupRequest extends Request {
    //Authorization token of the player who is requesting game setup
    private String authToken;
    //ID of the game that the player belongs to
    private String gameID;

    public GameSetupRequest(String authToken, String gameID) {
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

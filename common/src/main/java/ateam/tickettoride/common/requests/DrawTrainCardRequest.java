package ateam.tickettoride.common.requests;

public class DrawTrainCardRequest extends Request {
    private String authToken;
    private String gameID;
    private boolean changeTurn;

    public DrawTrainCardRequest(String authToken, String gameID, boolean changeTurn) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.changeTurn = changeTurn;
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

    public boolean isChangeTurn() {
        return changeTurn;
    }

    public void setChangeTurn(boolean changeTurn) {
        this.changeTurn = changeTurn;
    }
}

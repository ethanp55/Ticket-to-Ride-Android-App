package ateam.tickettoride.common.requests;

public class DrawFaceUpCardRequest extends Request {
    private String authToken;
    private String gameID;
    private int cardIndex;
    private boolean changeTurn;

    public DrawFaceUpCardRequest(String authToken, String gameID, int cardIndex, boolean changeTurn) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.cardIndex = cardIndex;
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

    public int getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public boolean isChangeTurn() {
        return changeTurn;
    }

    public void setChangeTurn(boolean changeTurn) {
        this.changeTurn = changeTurn;
    }
}

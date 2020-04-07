package ateam.tickettoride.common.requests;

import ateam.tickettoride.common.card.DestinationCard;

public class CompletedDestinationCardRequest extends Request {
    private String authToken;
    private String gameID;
    private DestinationCard destinationCard;

    public CompletedDestinationCardRequest(String authToken, String gameID, DestinationCard destinationCard) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.destinationCard = destinationCard;
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

    public DestinationCard getDestinationCard() {
        return destinationCard;
    }

    public void setDestinationCard(DestinationCard destinationCard) {
        this.destinationCard = destinationCard;
    }
}

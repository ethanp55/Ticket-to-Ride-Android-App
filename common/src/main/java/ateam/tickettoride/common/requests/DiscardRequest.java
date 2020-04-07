package ateam.tickettoride.common.requests;

import java.util.ArrayList;

import ateam.tickettoride.common.card.DestinationCard;

public class DiscardRequest extends Request {
    private String authToken;
    private String gameID;
    private ArrayList<DestinationCard> cardsToDiscard;

    public DiscardRequest(String authToken, String gameID, ArrayList<DestinationCard> cardsToDiscard) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.cardsToDiscard = cardsToDiscard;
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

    public ArrayList<DestinationCard> getCardsToDiscard() {
        return cardsToDiscard;
    }

    public void setCardsToDiscard(ArrayList<DestinationCard> cardsToDiscard) {
        this.cardsToDiscard = cardsToDiscard;
    }
}

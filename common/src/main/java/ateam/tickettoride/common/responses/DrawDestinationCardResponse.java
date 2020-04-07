package ateam.tickettoride.common.responses;

import ateam.tickettoride.common.card.DestinationCard;

public class DrawDestinationCardResponse extends Response {
    private DestinationCard[] destinationCards;

    public DrawDestinationCardResponse(DestinationCard[] destinationCards) {
        this.destinationCards = destinationCards;
    }

    public DestinationCard[] getDestinationCards() {
        return destinationCards;
    }

    public void setDestinationCards(DestinationCard[] destinationCards) {
        this.destinationCards = destinationCards;
    }
}

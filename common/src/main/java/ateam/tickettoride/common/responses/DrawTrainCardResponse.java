package ateam.tickettoride.common.responses;

import ateam.tickettoride.common.card.TrainCard;

public class DrawTrainCardResponse extends Response {
    private TrainCard trainCard;

    public DrawTrainCardResponse(TrainCard trainCard) {
        this.trainCard = trainCard;
    }

    public TrainCard getTrainCard() {
        return trainCard;
    }

    public void setTrainCard(TrainCard trainCard) {
        this.trainCard = trainCard;
    }
}

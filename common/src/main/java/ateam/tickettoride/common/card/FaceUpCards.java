package ateam.tickettoride.common.card;

import java.io.Serializable;

public class FaceUpCards implements Serializable {
    //The five face up train cards
    private TrainCard[] trainCards;

    public FaceUpCards(TrainCard[] trainCards) {
        this.trainCards = trainCards;
    }

    public TrainCard[] getTrainCards() {
        return trainCards;
    }

    public void setTrainCards(TrainCard[] trainCards) {
        this.trainCards = trainCards;
    }

    public int getNonNullCardCount(){
        int count = 0;
        for(int i = 0; i < trainCards.length; i++){
            if(trainCards[i] != null){
                count++;
            }
        }
        return count;
    }
}

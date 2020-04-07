package ateam.tickettoride.common.card;


import java.awt.Color;
import java.io.Serializable;
import java.util.Observer;

import ateam.tickettoride.common.TrainColor;

public class TrainCard implements Serializable {
    //Color of the train card
    private int cardColor;

    public TrainCard(int cardColor) {
        this.cardColor = cardColor;
    }

    public int getCardColor() {
        return cardColor;
    }

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }

    public String getTrainCardColor(){
        return TrainColor.getStringColor(cardColor);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof TrainCard)){
            return false;
        }

        TrainCard oCard = (TrainCard)o;

        if(this.cardColor == oCard.getCardColor()){
            return true;
        }
        else {
            return false;
        }
    }
}

package ateam.tickettoride.common.card;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

public class TrainCardDeck implements Serializable {
    //The deck of cards that is held
    private ArrayDeque<TrainCard> cards;

    public TrainCardDeck(ArrayDeque<TrainCard> cards) {
        this.cards = cards;
    }

    public ArrayDeque<TrainCard> getCards() {
        return cards;
    }

    public void setCards(ArrayDeque<TrainCard> cards) {
        this.cards = cards;
    }

    public void shuffle() {
        //Make a new list of cards
        ArrayList<TrainCard> cardList = new ArrayList<>();

        //Add the cards from the queue into the list
        for (TrainCard card : cards) {
            cardList.add(card);
        }

        //Shuffle the list and clear the old queue
        Collections.shuffle(cardList);

        cards.clear();

        //Add the shuffled cards back into the queue
        for (TrainCard card : cardList) {
            cards.add(card);
        }
    }

    public TrainCard drawCard() {
        return cards.poll();
    }
}

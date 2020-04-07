package ateam.tickettoride.common.card;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

public class DestinationCardDeck implements Serializable {
    //The deck of cards that is held
    private ArrayDeque<DestinationCard> cards;

    public DestinationCardDeck(ArrayDeque<DestinationCard> cards) {
        this.cards = cards;
    }

    public ArrayDeque<DestinationCard> getCards() {
        return cards;
    }

    public void setCards(ArrayDeque<DestinationCard> cards) {
        this.cards = cards;
    }

    public void shuffle() {
        //Make a new list of cards
        ArrayList<DestinationCard> cardList = new ArrayList<>();

        //Add the cards from the queue into the list
        for (DestinationCard card : cards) {
            cardList.add(card);
        }

        //Shuffle the list and clear the old queue
        Collections.shuffle(cardList);

        cards.clear();

        //Add the shuffled cards back into the queue
        for (DestinationCard card : cardList) {
            cards.add(card);
        }
    }

    public DestinationCard drawCard() {
        return cards.poll();
    }
}

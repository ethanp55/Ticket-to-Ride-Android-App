package ateam.tickettoride.Presenter.MapPresenterStates;

import java.util.ArrayList;
import java.util.List;

import ateam.tickettoride.Presenter.MapPresenter;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;

public interface IMapPresenterState {

    /**
     * Attempts to draw destination cards from the server.
     */
    public void drawDestCards();

    /**
     * Attempts to discard the given destination cards to the server.
     * @param cards The list of DestinationCards to be discarded.
     */
    public void discardDestCards(ArrayList<DestinationCard> cards);

    /**
     * Attempts to claim the given route for the player.
     * @param route The route to be claimed.
     * @param color The color of the cards to use
     */
    public void claimRoute(Route route, ArrayList<TrainCard> trainCardsToUse);

    /**
     * Attempts to draw a Face Up TrainCard for the player.
     * @param cardIndex  The index of the card to draw
     */
    public void drawFaceUpTrainCard(int cardIndex);

    /**
     * Attempts to draw a train card for the player from the deck.
     */
    public void drawDeckTrainCard();

    /**
     * Notifies the state that the drawer was closed.
     */
    public void signalClosedDrawer();

    /**
     * Notifies the state that the drawer was opened.
     */
    public void signalOpenDrawer(String whichDrawer);
}

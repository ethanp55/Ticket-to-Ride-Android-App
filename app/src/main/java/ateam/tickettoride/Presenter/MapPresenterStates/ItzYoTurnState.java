package ateam.tickettoride.Presenter.MapPresenterStates;

import java.util.ArrayList;

import ateam.tickettoride.Presenter.MapPresenter;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;

/**
 * State for the Map Presenter for when it is the given player's turn.
 */
public class ItzYoTurnState implements IMapPresenterState {
    private MapPresenter presenter;

    public ItzYoTurnState(MapPresenter presenter){
        this.presenter = presenter;
    }
    /**
     * Attempts to draw destination cards from the server.
     */
    @Override
    public void drawDestCards() {
        //does nothing
    }

    /**
     * Attempts to discard the given destination cards to the server.
     *
     * @param cards The list of DestinationCards to be discarded.
     */
    @Override
    public void discardDestCards(ArrayList<DestinationCard> cards) {
        //does nothing
    }

    /**
     * Attempts to claim the given route for the player.
     *
     * @param route The route to be claimed.
     * @param color The color of the cards to use
     */
    @Override
    public void claimRoute(Route route, ArrayList<TrainCard> trainCardsToUse){
        //does nothing
    }

    /**
     * Attempts to draw a Face Up TrainCard for the player.
     *
     * @param cardIndex The index of the card to draw
     */
    @Override
    public void drawFaceUpTrainCard(int cardIndex) {
        //does nothing
    }

    /**
     * Attempts to draw a train card for the player from the deck.
     */
    @Override
    public void drawDeckTrainCard() {
        //does nothing
    }

    /**
     * Notifies the state that the drawer was closed.
     */
    @Override
    public void signalClosedDrawer() {
        //does nothing
    }

    /**
     * Notifies the state that the drawer was opened.
     */
    @Override
    public void signalOpenDrawer(String whichDrawer) {
        if (whichDrawer.equals(MapPresenter.DESTINATION)){
//            presenter.showMessage("It's now Destination.");
            presenter.updateState(new DrawDestCardsState(presenter));
        }
        else if(whichDrawer.equals(MapPresenter.ROUTE)){
//            presenter.showMessage("It's now ClaimRoute.");
            presenter.updateState(new ClaimRouteState(presenter));
        }
        else if(whichDrawer.equals(MapPresenter.TRAIN)){
//            presenter.showMessage("It's now TrainCards.");
            presenter.updateState(new DrawTrainCardsState(presenter));
        }
    }
}

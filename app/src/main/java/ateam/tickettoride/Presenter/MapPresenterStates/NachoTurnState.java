package ateam.tickettoride.Presenter.MapPresenterStates;

import java.util.ArrayList;
import java.util.List;

import ateam.tickettoride.Presenter.MapPresenter;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;

/**
 * The state for the Map Presenter for when it is not the given player's turn.
 * None of the methods do anything.
 */
public class NachoTurnState implements IMapPresenterState {
    private static final String INSTRUCTION = "You cannot do that when it is not your turn.";

    private MapPresenter presenter;
    public NachoTurnState(MapPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void drawDestCards() {
        presenter.showMessage(INSTRUCTION);
        //does nothing
    }

    @Override
    public void discardDestCards(ArrayList<DestinationCard> cards) {
        presenter.showMessage(INSTRUCTION);
        //does nothing
    }

    /**
     * Attempts to claim the given route for the player.
     *
     * @param route The route to be claimed.
     * @param trainCardsToUse The train cards to use
     */
    @Override
    public void claimRoute(Route route, ArrayList<TrainCard> trainCardsToUse) {
        presenter.showMessage(INSTRUCTION);
        //does nothing
    }

    @Override
    public void drawFaceUpTrainCard(int cardIndex) {
        presenter.showMessage(INSTRUCTION);
        //does nothing
    }

    @Override
    public void drawDeckTrainCard() {
        presenter.showMessage(INSTRUCTION);
        //does nothing
    }

    @Override
    public void signalClosedDrawer() {
        //does nothing
    }

    @Override
    public void signalOpenDrawer(String whichDrawer) {
        //does nothing
    }
}

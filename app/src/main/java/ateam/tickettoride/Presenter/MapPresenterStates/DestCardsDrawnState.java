package ateam.tickettoride.Presenter.MapPresenterStates;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.MapPresenter;
import ateam.tickettoride.ServerProxy;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.requests.DiscardRequest;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;

public class DestCardsDrawnState implements IMapPresenterState {
    private static final String INSTRUCTION_MESSAGE = "You may not do anything but discard up to 2 destination cards.";
    private MapPresenter presenter;
    private boolean requestSent;

    public DestCardsDrawnState(MapPresenter presenter){
        this.presenter = presenter;
        requestSent = false;
    }
    /**
     * Attempts to draw destination cards from the server.
     */
    @Override
    public void drawDestCards() {
        presenter.showMessage(INSTRUCTION_MESSAGE);
    }

    /**
     * Attempts to discard the given destination cards to the server.
     *
     * @param cards The list of DestinationCards to be discarded.
     */
    @Override
    public void discardDestCards(ArrayList<DestinationCard> cards) {
        if (!requestSent) {
            DiscardRequest request = new DiscardRequest(ModelFacade.getInstance().getAuthToken(), ModelFacade.getInstance().getGameId(),
                    cards);
            new DiscardDestCardsTask().execute(request);
            requestSent = true;
        }
    }

    /**
     * Attempts to claim the given route for the player.
     *
     * @param route The route to be claimed.
     * @param color The color of the cards to use
     */
    @Override
    public void claimRoute(Route route, ArrayList<TrainCard> trainCardsToUse) {
        presenter.showMessage(INSTRUCTION_MESSAGE);
    }

    /**
     * Attempts to draw a Face Up TrainCard for the player.
     *
     * @param cardIndex The index of the card to draw
     */
    @Override
    public void drawFaceUpTrainCard(int cardIndex) {
        presenter.showMessage(INSTRUCTION_MESSAGE);
    }

    /**
     * Attempts to draw a train card for the player from the deck.
     */
    @Override
    public void drawDeckTrainCard() {
        presenter.showMessage(INSTRUCTION_MESSAGE);
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
     *
     * @param whichDrawer
     */
    @Override
    public void signalOpenDrawer(String whichDrawer) {
        //does nothing
    }

    private void handleResponse(Response response){
        if(response instanceof ErrorResponse){
            requestSent = false;
            ErrorResponse error = (ErrorResponse)response;
            presenter.showMessage(error.getMessage());
        }
        else{
            //clear displayed destination cards
            DestinationCard[] nullCards = new DestinationCard[3];
            nullCards[0] = null;
            nullCards[1] = null;
            nullCards[2] = null;
            presenter.updateDrawnDestCards(nullCards);

            presenter.updateState(new NachoTurnState(presenter));
        }
    }

    private class DiscardDestCardsTask extends AsyncTask<DiscardRequest, Void, Response>{
        @Override
        protected void onPostExecute(Response response) {
            handleResponse(response);
        }

        @Override
        protected Response doInBackground(DiscardRequest... discardRequests) {
            return ServerProxy.getInstance().discard(discardRequests[0]);
        }
    }
}

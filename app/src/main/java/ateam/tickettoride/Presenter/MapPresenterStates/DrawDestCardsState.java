package ateam.tickettoride.Presenter.MapPresenterStates;

import android.os.AsyncTask;
import android.view.Display;

import java.util.ArrayList;
import java.util.List;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.MapPresenter;
import ateam.tickettoride.ServerProxy;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.requests.DrawDestinationCardRequest;
import ateam.tickettoride.common.responses.DrawDestinationCardResponse;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;

public class DrawDestCardsState implements IMapPresenterState {
    private MapPresenter presenter;
    private boolean requestSent;

    public DrawDestCardsState(MapPresenter presenter){
        this.presenter = presenter;
        requestSent = false;
    }
    /**
     * Attempts to draw destination cards from the server.
     */
    @Override
    public void drawDestCards() {
        if(!requestSent){
            DrawDestinationCardRequest request = new DrawDestinationCardRequest(ModelFacade.getInstance().getAuthToken(),
                    ModelFacade.getInstance().getGameId());
            new DrawDestCardsTask().execute(request);
            requestSent = true;
        }
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
    public void claimRoute(Route route, ArrayList<TrainCard> trainCardsToUse) {
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
        if(!requestSent) {
            presenter.updateState(new ItzYoTurnState(presenter));
        }
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
        if (response instanceof ErrorResponse){
            requestSent = false;

            ErrorResponse error = (ErrorResponse)response;
            presenter.showMessage(error.getMessage());
        }
        else{
            DrawDestinationCardResponse goodResponse = (DrawDestinationCardResponse)response;
            presenter.updateDrawnDestCards(goodResponse.getDestinationCards());
            presenter.updateState(new DestCardsDrawnState(presenter));
        }
    }

    private class DrawDestCardsTask extends AsyncTask<DrawDestinationCardRequest, Void, Response>{
        @Override
        protected void onPostExecute(Response response) {
            handleResponse(response);
        }

        @Override
        protected Response doInBackground(DrawDestinationCardRequest... drawDestinationCardRequests) {
            return ServerProxy.getInstance().drawDestinationCard(drawDestinationCardRequests[0]);
        }
    }
}

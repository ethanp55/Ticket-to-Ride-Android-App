package ateam.tickettoride.Presenter.MapPresenterStates;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.MapPresenter;
import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.ServerProxy;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Player;
import ateam.tickettoride.common.requests.ClaimRouteRequest;
import ateam.tickettoride.common.requests.CompletedDestinationCardRequest;
import ateam.tickettoride.common.responses.EmptyResponse;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;

public class ClaimRouteState implements IMapPresenterState {
    private static final String TAG = "ClaimRouteState";
    private MapPresenter presenter;
    private Route route;
    private boolean requestSent;

    public ClaimRouteState(MapPresenter presenter){
        this.presenter = presenter;
        requestSent = false;
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
     */
    @Override
    public void claimRoute(Route route, ArrayList<TrainCard> trainCardsToUse) {
        //don't sent multiple requests
        if(!requestSent) {
            TrainCard[] cardArr = new TrainCard[trainCardsToUse.size()];
            cardArr = trainCardsToUse.toArray(cardArr);

            ClaimRouteRequest request = new ClaimRouteRequest(ModelFacade.getInstance().getAuthToken(), ModelFacade.getInstance().getGameId(),
                    route, cardArr);
            new ClaimRouteTask().execute(request);
            this.route = route;
            requestSent = true;
        }
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
     * State switches back to ItzYoTurnState
     */
    @Override
    public void signalClosedDrawer() {
        if (!requestSent) {
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
            Log.i(TAG, "Claim Route Success from Server");

            presenter.updateState(new NachoTurnState(presenter));
        }
    }



    private class ClaimRouteTask extends AsyncTask<ClaimRouteRequest, Void, Response>{
        @Override
        protected Response doInBackground(ClaimRouteRequest... claimRouteRequests) {
            return ServerProxy.getInstance().claimRoute(claimRouteRequests[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            handleResponse(response);
        }
    }
}

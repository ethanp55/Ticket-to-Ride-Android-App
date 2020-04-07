package ateam.tickettoride.Presenter.MapPresenterStates;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;

import java.util.ArrayList;
import java.util.List;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.MapPresenter;
import ateam.tickettoride.ServerProxy;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.requests.DrawFaceUpCardRequest;
import ateam.tickettoride.common.requests.DrawTrainCardRequest;
import ateam.tickettoride.common.responses.DrawTrainCardResponse;
import ateam.tickettoride.common.responses.EmptyResponse;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;

public class DrawTrainCardsState implements IMapPresenterState {
    private static final String TAG = "DrawTrainCardState";

    private MapPresenter presenter;
    private boolean requestSent;
    private boolean drewLocomotive;

    public DrawTrainCardsState(MapPresenter presenter){
        this.presenter = presenter;
        requestSent = false;
        drewLocomotive = false;
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
     * @param trainCardsToUse The color of the cards to use
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
        if (!requestSent) {
//            presenter.showMessage("Message sent.");
            FaceUpCards cards = ModelFacade.getInstance().getRunningGame().getFaceUpCards();
            TrainCard card = cards.getTrainCards()[cardIndex];

            if(card != null) {
                if (card.getCardColor() == 0xff808080) {
                    drewLocomotive = true;
                }

                // Check if there is only one faceup card that is not null
                Log.i(TAG, "" + cards.getNonNullCardCount());

                if(cards.getNonNullCardCount() == 1){
                    // They may not have drawn a locomotive but it will just increment their turn
                    drewLocomotive = true;
                }

                DrawFaceUpCardRequest request = new DrawFaceUpCardRequest(ModelFacade.getInstance().getAuthToken(),
                        ModelFacade.getInstance().getGameId(), cardIndex, drewLocomotive);

                new DrawFaceUpCardTask().execute(request);
                requestSent = true;
            }
            else{
                presenter.showMessage("No card in that position");
            }


        }
    }

    /**
     * Attempts to draw a train card for the player from the deck.
     */
    @Override
    public void drawDeckTrainCard() {

        if (!requestSent){
            int size = ModelFacade.getInstance().getTrainCardDeckSize();
            FaceUpCards cards = ModelFacade.getInstance().getRunningGame().getFaceUpCards();

            if(size == 1 && (cards.getNonNullCardCount() == 0)){
                drewLocomotive = true;
            }
//            presenter.showMessage("Message being sent.");
            DrawTrainCardRequest request = new DrawTrainCardRequest(ModelFacade.getInstance().getAuthToken(),
                    ModelFacade.getInstance().getGameId(), false);

            new DrawTrainCardFromDeckTask().execute(request);
            requestSent = true;
        }
    }

    /**
     * Notifies the state that the drawer was closed.
     */
    @Override
    public void signalClosedDrawer() {
        if (!requestSent){
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
        if(response instanceof ErrorResponse){
            requestSent = false;
            drewLocomotive = false;

            ErrorResponse error = (ErrorResponse)response;
            presenter.showMessage(error.getMessage());
        }
        else{
            if(drewLocomotive){
//                presenter.showMessage("Back from drawing locomotive.");
                presenter.updateState(new NachoTurnState(presenter));
            }
            if(response instanceof DrawTrainCardResponse){
                DrawTrainCardResponse drawTrainCardResponse = (DrawTrainCardResponse) response;

//                presenter.showMessage("Back from drawing regular.");
                if(drawTrainCardResponse.getTrainCard() != null){
                    String message = "You drew a " + ((DrawTrainCardResponse) response).getTrainCard().getTrainCardColor() + " card.";
                    presenter.showMessage(message);
                    presenter.updateState(new DrewNonFaceUpLocomotiveState(presenter));
                }
                else {
                    requestSent = false;
                    presenter.showMessage("There are no more cards in the deck.");
                }
            }
            if(response instanceof EmptyResponse && !drewLocomotive){
                presenter.showMessage("You drew your first faceup card!");
                presenter.updateState(new DrewNonFaceUpLocomotiveState(presenter));
            }
        }
    }

    private class DrawTrainCardFromDeckTask extends AsyncTask<DrawTrainCardRequest, Void, Response>{
        @Override
        protected void onPostExecute(Response response) {
            handleResponse(response);
        }

        @Override
        protected Response doInBackground(DrawTrainCardRequest... drawTrainCardRequests) {
            return ServerProxy.getInstance().drawTrainCard(drawTrainCardRequests[0]);
        }
    }

    private class DrawFaceUpCardTask extends AsyncTask<DrawFaceUpCardRequest, Void, Response>{
        @Override
        protected void onPostExecute(Response response) {
            handleResponse(response);
        }

        @Override
        protected Response doInBackground(DrawFaceUpCardRequest... drawFaceUpCardRequests) {
            return ServerProxy.getInstance().drawFaceUpCard(drawFaceUpCardRequests[0]);
        }
    }
}

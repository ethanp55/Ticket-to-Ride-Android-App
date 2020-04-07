package ateam.tickettoride.Presenter.MapPresenterStates;

import android.os.AsyncTask;

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
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;

public class DrewNonFaceUpLocomotiveState implements IMapPresenterState {
    private MapPresenter presenter;
    private boolean requestSent;

    private static final String INSTRUCTION_MESSAGE = "You may only draw another train card.";

    public DrewNonFaceUpLocomotiveState(MapPresenter presenter){
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
        presenter.showMessage(INSTRUCTION_MESSAGE);
    }

    /**
     * Attempts to claim the given route for the player.
     *
     * @param route The route to be claimed.
     * @param trainCardsToUse The color of the cards to use
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
        if (!requestSent) {
            FaceUpCards cards = ModelFacade.getInstance().getRunningGame().getFaceUpCards();
            TrainCard card = cards.getTrainCards()[cardIndex];

            if(card != null){
                if (card.getCardColor() != 0xff808080) {

                    DrawFaceUpCardRequest request = new DrawFaceUpCardRequest(ModelFacade.getInstance().getAuthToken(),
                            ModelFacade.getInstance().getGameId(), cardIndex, true);

                    new DrawFaceUpCardTask().execute(request);
                    requestSent = true;
//                presenter.showMessage("Request sent for non-locomotive after first draw.");
                }
                else{
                    presenter.showMessage("Sorry, you can't draw a locomotive.");
                }
            }
            else{
                presenter.showMessage("No card in that position");
            }
            //make sure not drawing a locomotive from face up cards
        }
    }

    /**
     * Attempts to draw a train card for the player from the deck.
     */
    @Override
    public void drawDeckTrainCard() {
        if (!requestSent){
//            presenter.showMessage("Request sent.");
            DrawTrainCardRequest request = new DrawTrainCardRequest(ModelFacade.getInstance().getAuthToken(),
                    ModelFacade.getInstance().getGameId(), true);

            new DrawTrainCardFromDeckTask().execute(request);
            requestSent = true;
        }
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

    public void handleResponse(Response response){
        if(response instanceof ErrorResponse){
            requestSent = false;
            ErrorResponse error = (ErrorResponse)response;
            presenter.showMessage(error.getMessage());
        }
        else{
            if(response instanceof DrawTrainCardResponse){
                DrawTrainCardResponse drawTrainCardResponse = (DrawTrainCardResponse) response;
                if(drawTrainCardResponse.getTrainCard() != null){
                    String message = "You drew a " + drawTrainCardResponse.getTrainCard().getTrainCardColor() + " card.";
                    presenter.showMessage(message);
                    presenter.updateState(new NachoTurnState(presenter));
                }
                else{
                    requestSent = false;
                    presenter.showMessage("There are no more cards in the deck.");
                }
            }
//            presenter.showMessage("NachoTurn from NonLocomotive.");
        }
    }

    private class DrawTrainCardFromDeckTask extends AsyncTask<DrawTrainCardRequest, Void, Response> {
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

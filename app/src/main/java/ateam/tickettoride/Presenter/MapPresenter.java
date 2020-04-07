package ateam.tickettoride.Presenter;

import android.app.Activity;
import android.media.midi.MidiOutputPort;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.MapPresenterStates.ClaimRouteState;
import ateam.tickettoride.Presenter.MapPresenterStates.IMapPresenterState;
import ateam.tickettoride.Presenter.MapPresenterStates.ItzYoTurnState;
import ateam.tickettoride.Presenter.MapPresenterStates.NachoTurnState;
import ateam.tickettoride.ServerProxy;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.Player;
import ateam.tickettoride.common.requests.CompletedDestinationCardRequest;
import ateam.tickettoride.common.requests.GameSetupRequest;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.GameSetupResponse;
import ateam.tickettoride.common.responses.Response;

public class MapPresenter implements Observer{
    public static final String DESTINATION = "destination";
    public static final String ROUTE = "route";
    public static final String TRAIN = "train";

    private static final String TAG = "MapPresenter";
    private static MapPresenter instance = null;
    private Activity activity;
    private IMapPresenterState myState;

    public static MapPresenter getInstance(){
        if(instance == null){
            instance = new MapPresenter();
        }
        return instance;
    }

    private MapPresenter(){
        activity = null;
        ModelFacade.getInstance().addRunningGameObserver(this);

        myState = new NachoTurnState(this);
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public void updatePlayerDestinationCards(String name, DestinationCard[] destinationCards){
        ModelFacade.getInstance().updatePlayerDestinationCards(name, destinationCards);
    }

    public void updateTurn(Integer currentPlayerIndex){
        ModelFacade.getInstance().updateTurn(currentPlayerIndex);

        //stuff to see if the state should be changed to ItzYoTurnState
        Game game = ModelFacade.getInstance().getRunningGame();
        Player[] players = game.getPlayers();

        if (currentPlayerIndex >= 0 && currentPlayerIndex < players.length){
            Player currPlayer = players[currentPlayerIndex];
            String myUsername = ModelFacade.getInstance().getUsername();
            if(currPlayer.getUsername().equals(myUsername)){
//                showMessage("ItzYoTurn.");
                updateState(new ItzYoTurnState(this));
            }
        }
    }

    public void updatePlayerRoutes(String name, Route route) {
        ModelFacade.getInstance().updatePlayerRoutes(name, route);

        if(name.equals(ModelFacade.getInstance().getUsername())){
            checkIfDestCardsAreComplete(route);
        }
    }

    /**
     * Checks if the newly claimed route completes any of the player's destination cards
     */
    private void checkIfDestCardsAreComplete(Route route){
        // Find the player who just claimed the route
        String username = ModelFacade.getInstance().getUsername();
        Player player = ModelFacade.getInstance().getPlayer(username);

        // Update their city graph with the new route
        player.updateCityGraph(route);

        // Check if any of their destination cards are completed. If they are they will be returned in an ArrayList
        ArrayList<DestinationCard> cardsComplete = player.seeIfDestinationCardsAreFinished();
        Log.i(TAG, "Complete dest card size: " + cardsComplete.size());

        // For each completed card, let the server know!
        for(int i = 0; i < cardsComplete.size(); i++){
            CompletedDestinationCardRequest request = new CompletedDestinationCardRequest(ModelFacade.getInstance().getAuthToken(),
                    ModelFacade.getInstance().getRunningGame().getGameID(), cardsComplete.get(i));
            Log.i(TAG, cardsComplete.get(i).toString());
            new CompleteDestCardTask().execute(request);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("updateTurn") && activity != null) {
            Game game = ModelFacade.getInstance().getRunningGame();

            ViewFacade.getInstance().updatePlayerTurn(this.activity, game.getCurrentPlayer());
        }
        if (arg.equals("updatePlayerRoutes") && activity != null) {
            Game game = ModelFacade.getInstance().getRunningGame();

            ViewFacade.getInstance().updatePlayersForRoutes(this.activity, game.getPlayers());
        }
    }

    public String getUsername(){
        return ModelFacade.getInstance().getUsername();
    }

    public void updateState(IMapPresenterState newState){
        myState = newState;
    }

    public Game getGame(){
        return ModelFacade.getInstance().getRunningGame();
    }

    public void drawDestCards(){
        myState.drawDestCards();
    }

    public void discardDestCards(ArrayList<DestinationCard> cards){
        myState.discardDestCards(cards);
    }

    public void claimRoute(Route route, ArrayList<TrainCard> trainCardsToUse){
        myState.claimRoute(route, trainCardsToUse);
    }

    public void drawFaceUpTrainCard(int cardIndex){
        myState.drawFaceUpTrainCard(cardIndex);
    }

    public void drawDeckTrainCard(){
        myState.drawDeckTrainCard();
    }

    public void signalClosedDrawer(){
        myState.signalClosedDrawer();
    }

    public void signalOpenDrawer(String whichDrawer){
        myState.signalOpenDrawer(whichDrawer);
    }

    public void showMessage(final String message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateDrawnDestCards(DestinationCard[] cards){
        ViewFacade.getInstance().updateDrawnDestCards(this.activity, cards);
    }

    public void updateLastTurn() {
        ViewFacade.getInstance().updateLastTurn(this.activity);
    }

    public void updateGameOver() {
        ViewFacade.getInstance().updateGameOver(this.activity);
    }

    private class GetGameSetupTask extends AsyncTask<GameSetupRequest, Void, Response> {
        @Override
        protected Response doInBackground(GameSetupRequest... gameSetupRequests){
            return ServerProxy.getInstance().gameSetup(gameSetupRequests[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            if(!(response instanceof ErrorResponse)){
                GameSetupResponse gameSetupResponse = (GameSetupResponse) response;

            }
            else{
                ErrorResponse errorResponse = (ErrorResponse) response;
                ViewFacade.getInstance().showErrorMessageMain(activity, errorResponse.getMessage());
            }
        }
    }

    private class CompleteDestCardTask extends AsyncTask<CompletedDestinationCardRequest, Void, Response>{
        @Override
        protected Response doInBackground(CompletedDestinationCardRequest... completedDestinationCardRequests) {
            return ServerProxy.getInstance().completedDestinationCard(completedDestinationCardRequests[0]);
        }

        @Override
        protected void onPostExecute(Response response) {

        }
    }

}

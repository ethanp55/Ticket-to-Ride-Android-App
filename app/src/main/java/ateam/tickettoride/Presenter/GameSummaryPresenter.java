package ateam.tickettoride.Presenter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Model.UserInfoContainer;
import ateam.tickettoride.ServerProxy;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.Player;
import ateam.tickettoride.common.requests.ChatRequest;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;

public class GameSummaryPresenter implements Observer {
    private static final String TAG = "GameSummaryPresenter";
    private static GameSummaryPresenter instance = null;
    private Activity activity;
    private Fragment parentFragment;

    public static GameSummaryPresenter getInstance(){
        if(instance == null){
            instance = new GameSummaryPresenter();
        }
        return instance;
    }

    private GameSummaryPresenter(){
        ModelFacade.getInstance().addRunningGameObserver(this);
        activity = null;
    }

    public void setActivity(Activity activity){
        System.out.println("Setting activity");
        this.activity = activity;
    }

    public void setParentFragment(Fragment fragment) {
        this.parentFragment = fragment;
    }

    public Player updateHand() {
        Game game = ModelFacade.getInstance().getRunningGame();
        UserInfoContainer thisUser = UserInfoContainer.getInstance();

        return game.findPlayer(thisUser.getUsername());
    }

    public Player[] updateSummary() {
        Game game = ModelFacade.getInstance().getRunningGame();

        return game.getPlayers();
    }

    public void sendChat(String message) {
        ModelFacade modelFacade = ModelFacade.getInstance();
        ChatRequest request = new ChatRequest(modelFacade.getAuthToken(), modelFacade.getGameId(), message);
        new sendChatTask().execute(request);
    }

    public void updateChat(String message) {
        Log.i(TAG, "updateChat");
        ModelFacade modelFacade = ModelFacade.getInstance();
        modelFacade.updateChat(message);
    }

    public void updateGameHistory(String message) {
        ModelFacade modelFacade = ModelFacade.getInstance();
        modelFacade.updateGameHistory(message);
    }


    public void updatePlayerPoints(String name, Integer points) {
        ModelFacade.getInstance().updatePlayerPoints(name, points);
    }

    public void updatePlayerTrainPieces(String name, Integer numTrainPieces) {
        ModelFacade.getInstance().updatePlayerTrainPieces(name, numTrainPieces);
    }


    public void updateClaimedGameRoutes(Route claimedRoute) {
        ModelFacade.getInstance().updateClaimedGameRoutes(claimedRoute);
    }

    public void updateNumTrainCardsInDeck(Integer numTrainCards) {
        ModelFacade.getInstance().updateNumTrainCardsInDeck(numTrainCards);
    }

    public void updateNumDestinationCardsInDeck(Integer numDestinationCards) {
        ModelFacade.getInstance().updateNumDestinationCardsInDeck(numDestinationCards);
    }

    @Override
    public void update(Observable o, Object arg) {
        //Log.i(TAG, "updateChat Observable1");
        System.out.println(arg.toString());

        if(arg.equals("updateChat") && activity != null) {
            Game game = ModelFacade.getInstance().getRunningGame();

            Log.i(TAG, "UpdatingChat in update function");

            ViewFacade.getInstance().updateChat(activity, game.getChatHistory());
        }

        if(arg.equals("updateGameHistory") && activity != null) {
            Game game = ModelFacade.getInstance().getRunningGame();

            ViewFacade.getInstance().updateGameHistory(activity, game.getGameHistory());
        }

        // Updating the player objects on the GameSummaryFragment whenever any player information changes in the model
        if((arg.equals("updatePlayerRoutes") ||
                arg.equals("updatePlayerTrainPieces") ||
                arg.equals("updatePlayerPoints") ||
                arg.equals("updatePlayerTrainCards") ||
                arg.equals("updatePlayerDestCards")) &&
                activity != null){

            Game game = ModelFacade.getInstance().getRunningGame();

            Log.i(TAG, "Updating player information");

            ViewFacade.getInstance().updatePlayers(activity, game.getPlayers());
        }
    }

    Player[] getPlayers(){
        return ModelFacade.getInstance().getRunningGame().getPlayers();
    }

    private class sendChatTask extends AsyncTask<ChatRequest,Void,Response> {
        @Override
        protected Response doInBackground(ChatRequest... chatRequests) {
            return ServerProxy.getInstance().chat(chatRequests[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            if(response instanceof ErrorResponse) {
                ErrorResponse errorResponse = (ErrorResponse) response;
                ViewFacade.getInstance().showErrorMessageGame(activity, errorResponse.getMessage());
            }
        }
    }
}

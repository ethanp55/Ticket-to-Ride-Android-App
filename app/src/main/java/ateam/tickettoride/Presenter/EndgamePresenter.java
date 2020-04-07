package ateam.tickettoride.Presenter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

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
import ateam.tickettoride.common.requests.LongestPathRequest;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;

//TODO Get chat working on Endgame, might need a new command.
public class EndgamePresenter implements Observer {
    private static final String TAG = "EndGamePresenter";
    private static EndgamePresenter instance = null;
    private Activity activity;
    private Fragment parentFragment;

    public static EndgamePresenter getInstance() {
        if(instance == null) {
            instance = new EndgamePresenter();
        }
        return instance;
    }

    private EndgamePresenter() {
        ModelFacade.getInstance().addRunningGameObserver(this);
        activity = null;
    }

    @Override
    public void update(Observable o, Object arg) {
        Game game = ModelFacade.getInstance().getRunningGame();
        if(arg.equals("updateGameHistory") && activity != null) {
            ViewFacade.getInstance().updateGameHistoryEnd(activity, game.getGameHistory());
        }
    }

    public void setActivity(Activity activity){
        System.out.println("Setting activity");
        this.activity = activity;
    }

    public void setParentFragment(Fragment fragment) {
        this.parentFragment = fragment;
    }

    public void sendLongest(int myLongest) {
        ModelFacade modelFacade = ModelFacade.getInstance();
        LongestPathRequest request = new LongestPathRequest(modelFacade.getAuthToken(), modelFacade.getGameId(), myLongest);
        new sendLongestTask().execute(request);
    }

    public void updateLongest(Integer[] longestPaths) {
        ViewFacade.getInstance().updateEndgameScore(activity, longestPaths);
    }

    public void exitGame(Activity activity) {
        ViewFacade.getInstance().exitGame(activity);
    }

    public void updateNumTrainCardsInDeck(Integer numTrainCards) {
        ModelFacade.getInstance().updateNumTrainCardsInDeck(numTrainCards);
    }

    public void updateNumDestinationCardsInDeck(Integer numDestinationCards) {
        ModelFacade.getInstance().updateNumDestinationCardsInDeck(numDestinationCards);
    }

    Player[] getPlayers(){
        return ModelFacade.getInstance().getRunningGame().getPlayers();
    }

    private class sendLongestTask extends AsyncTask<LongestPathRequest,Void,Response> {

        @Override
        protected Response doInBackground(LongestPathRequest... longestPathRequests) {
            return ServerProxy.getInstance().setLongestPath(longestPathRequests[0]);
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

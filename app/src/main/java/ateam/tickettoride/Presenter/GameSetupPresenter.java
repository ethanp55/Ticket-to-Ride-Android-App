package ateam.tickettoride.Presenter;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.ServerProxy;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.model.Player;
import ateam.tickettoride.common.requests.InitialDiscardRequest;
import ateam.tickettoride.common.responses.EmptyResponse;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;

public class GameSetupPresenter {
    private static final String TAG = "GameSetupPresenter";
    private static GameSetupPresenter instance = null;
    private Activity activity;
    private boolean requestSent;

    public static GameSetupPresenter getInstance(){
        if(instance == null){
            instance = new GameSetupPresenter();
        }
        return instance;
    }

    private GameSetupPresenter(){
        activity = null;
        requestSent = false;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public ArrayList<DestinationCard> getDestinationCards(){
        String username = ModelFacade.getInstance().getUsername();

        Player userPlayer = ModelFacade.getInstance().getPlayer(username);

        return userPlayer.getDestinationCards();
    }

    public void discardDestCards(DestinationCard card){
        Log.i(TAG, "discardingDestinationCards...");

        String authToken = ModelFacade.getInstance().getAuthToken();
        String gameId = ModelFacade.getInstance().getGameId();

        if(!requestSent) {
            new InitialDiscardTask().execute(new InitialDiscardRequest(authToken, gameId, card));
            requestSent = true;
        }
    }


    private class InitialDiscardTask extends AsyncTask<InitialDiscardRequest,Void,Response> {
        @Override
        protected Response doInBackground(InitialDiscardRequest... discardRequests){
            return ServerProxy.getInstance().initialDiscard(discardRequests[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            if(!(response instanceof ErrorResponse)){
                EmptyResponse initialDiscardResponse = (EmptyResponse) response;

                ViewFacade.getInstance().changeToMapFragment(activity);
            }
            else{
                requestSent = false;
                ErrorResponse errorResponse = (ErrorResponse) response;
                ViewFacade.getInstance().showErrorMessageGame(activity, errorResponse.getMessage());
            }
        }
    }
}

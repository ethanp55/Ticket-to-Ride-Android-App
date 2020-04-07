package ateam.tickettoride.Presenter;

import android.app.Activity;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.Display;

import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.Model.JoinedGameContainer;
import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Model.RunningGameContainer;
import ateam.tickettoride.Poller;
import ateam.tickettoride.ServerProxy;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.requests.GameSetupRequest;
import ateam.tickettoride.common.requests.StartGameRequest;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.GameSetupResponse;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.common.responses.StartGameResponse;

public class GameLobbyPresenter implements Observer {
    private static final int MIN_PLAYER_NUMBER = 2;
    private static final String NOT_ENOUGH_PLAYERS_MESSAGE = "There are not enough players to start the game";

    private static GameLobbyPresenter instance = null;
    private Activity activity;
    private boolean requestSent;

    public static GameLobbyPresenter getInstance(){
        if(instance == null){
            instance = new GameLobbyPresenter();
        }
        return instance;
    }

    private GameLobbyPresenter(){
        activity = null;
        ModelFacade.getInstance().addGameLobbyObserver(this);
        ModelFacade.getInstance().addRunningGameObserver(this);
        ModelFacade.getInstance().setPollerMethodToCall(Poller.gameUpdateString);
        requestSent = false;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    /**
     * If the user is the host, the game will be started if there are enough people
     * @param gameId
     */
    public void startGame(String gameId){
        if(ModelFacade.getInstance().getJoinedGame().getCurrNumPlayers() < MIN_PLAYER_NUMBER){
            ViewFacade.getInstance().showErrorMessageMain(activity, NOT_ENOUGH_PLAYERS_MESSAGE);
        }
        else {
            if(!requestSent) {
                new StartGameTask().execute(new StartGameRequest(gameId, ModelFacade.getInstance().getAuthToken()));
                requestSent = true;
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof RunningGameContainer && arg.equals("setup")){
            String authToken = ModelFacade.getInstance().getAuthToken();
            String gameId = ModelFacade.getInstance().getJoinedGame().getGameId();

            new GetGameSetupTask().execute(new GameSetupRequest(authToken, gameId));
        }
        if(o instanceof JoinedGameContainer){
            ViewFacade.getInstance().updateGameLobby(activity);
        }
    }

    private void changePollingMethod(){

    }

    private void stopObserving(){
        ModelFacade.getInstance().deleteRunningGameObserver(this);
    }

    private class StartGameTask extends AsyncTask<StartGameRequest,Void,Response> {
        @Override
        protected Response doInBackground(StartGameRequest... startGameRequests){
            return ServerProxy.getInstance().startGame(startGameRequests[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            if(!(response instanceof ErrorResponse)){
                StartGameResponse startGameResponse = (StartGameResponse) response;
                //ModelFacade.getInstance().setJoinedGame(startGameResponse.getGameInfo());
                ModelFacade.getInstance().startGame();

                //Add new observer!

                changePollingMethod();
            }
            else{
                requestSent = false;
                ErrorResponse errorResponse = (ErrorResponse) response;
                ViewFacade.getInstance().showErrorMessageMain(activity, errorResponse.getMessage());
            }
        }
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

                ModelFacade.getInstance().setGame(gameSetupResponse.getGame());

                changePollingMethod();
                stopObserving();

                ViewFacade.getInstance().moveToGameActivity(activity);
            }
            else{
                ErrorResponse errorResponse = (ErrorResponse) response;
                ViewFacade.getInstance().showErrorMessageMain(activity, errorResponse.getMessage());
            }
        }
    }
}

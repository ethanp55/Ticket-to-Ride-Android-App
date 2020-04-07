package ateam.tickettoride.Presenter;


import android.app.Activity;
import android.os.AsyncTask;

import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.Model.GameBrowserContainer;
import ateam.tickettoride.Model.JoinedGameContainer;
import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Model.RunningGameContainer;
import ateam.tickettoride.Poller;
import ateam.tickettoride.ServerProxy;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.model.User;
import ateam.tickettoride.common.requests.CreateGameRequest;
import ateam.tickettoride.common.requests.JoinGameRequest;
import ateam.tickettoride.common.requests.LoginRequest;
import ateam.tickettoride.common.requests.RegisterRequest;
import ateam.tickettoride.common.responses.CreateGameResponse;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.JoinGameResponse;
import ateam.tickettoride.common.responses.LoginResponse;
import ateam.tickettoride.common.responses.RegisterResponse;
import ateam.tickettoride.common.responses.Response;

/**
 * The presenter for the GameBrowserFragment, serves an intermediary between Server and View layers
 */
public class GameBrowserPresenter implements Observer {

    private static GameBrowserPresenter instance = null;

    private boolean requestSent;

    public static GameBrowserPresenter getInstance(){
        if(instance == null){
            instance = new GameBrowserPresenter();
        }
        return instance;
    }

    private Activity activity;

    public GameBrowserPresenter(){
        activity = null;
        requestSent = false;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    /**
     * Creates a new CreateGame Task
     * @param gameName name of game to be created
     * @param maxPlayers number of players the creator would like to have in his/her game
     */
    public void createGame(String gameName, int maxPlayers){
        if(!requestSent) {
            new CreateGameTask().execute(new CreateGameRequest(gameName, maxPlayers, ModelFacade.getInstance().getAuthToken()));
            requestSent = true;
        }
    }

    /**
     * Creates a new JoinGameTask
     * @param gameId the gameId of the game the user is wanting to join
     * @param gameName the name of the game the user is wanting to join
     */
    public void joinGame(String gameId, String gameName){
        if(!requestSent) {
            new JoinGameTask().execute(new JoinGameRequest(gameId, gameName, ModelFacade.getInstance().getAuthToken()));
            requestSent = true;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof GameBrowserContainer){
            ViewFacade.getInstance().updateGameBrowser(activity);
        }
    }

    /**
     * Sets the model polling method so the poller knows which commands to retrieve from the server
     */
    private void changePollingMethod(){
        ModelFacade.getInstance().setPollerMethodToCall(Poller.gameUpdateString);
    }

    private class CreateGameTask extends AsyncTask<CreateGameRequest,Void,Response> {
        @Override
        protected Response doInBackground(CreateGameRequest... createGameRequests){
            return ServerProxy.getInstance().createGame(createGameRequests[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            if(!(response instanceof ErrorResponse)){
                CreateGameResponse createGameResponse = (CreateGameResponse) response;
                ModelFacade.getInstance().setJoinedGame(createGameResponse.getGameInfo());

                ViewFacade.getInstance().changeToLobbyFragment(activity);
            }
            else{
                requestSent = false;
                ErrorResponse errorResponse = (ErrorResponse) response;
                ViewFacade.getInstance().showErrorMessageMain(activity, errorResponse.getMessage());
            }
        }
    }

    private class JoinGameTask extends AsyncTask<JoinGameRequest,Void,Response> {
        @Override
        protected Response doInBackground(JoinGameRequest... joinGameRequests){
            return ServerProxy.getInstance().joinGame(joinGameRequests[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            if(!(response instanceof ErrorResponse)){
                JoinGameResponse joinGameResponse = (JoinGameResponse) response;

                ModelFacade.getInstance().setJoinedGame(joinGameResponse.getGameInfo());

                ViewFacade.getInstance().changeToLobbyFragment(activity);
            }
            else{
                requestSent = false;
                ErrorResponse errorResponse = (ErrorResponse) response;
                ViewFacade.getInstance().showErrorMessageMain(activity, errorResponse.getMessage());
            }
        }
    }


}

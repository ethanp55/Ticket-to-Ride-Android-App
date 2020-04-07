package ateam.tickettoride.Presenter;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.Display;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.ClientCommunicator;
import ateam.tickettoride.ClientFacade;
import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Poller;
import ateam.tickettoride.ServerProxy;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.Jsonifier;
import ateam.tickettoride.common.model.GameInfo;
import ateam.tickettoride.common.model.User;
import ateam.tickettoride.common.requests.LoginRequest;
import ateam.tickettoride.common.requests.RegisterRequest;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.LoginResponse;
import ateam.tickettoride.common.responses.RegisterResponse;
import ateam.tickettoride.common.responses.Response;

public class LoginRegisterPresenter implements Observer {
    private static LoginRegisterPresenter instance = null;
    private Activity activity;
    private boolean requestSent;

    public static LoginRegisterPresenter getInstance(){
        if(instance == null){
            instance = new LoginRegisterPresenter();
        }
        return instance;
    }

    public LoginRegisterPresenter(){
        activity = null;
        requestSent = false;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public void login(String IPAddress, String userName, String password){
        if(!requestSent) {
            ClientCommunicator.getInstance().setIPAddress(IPAddress);
            new LoginTask().execute(new LoginRequest(userName, password));
            requestSent = true;
        }
    }

    public void register(String IPAddress, String userName, String password) {
        if(!requestSent) {
            ClientCommunicator.getInstance().setIPAddress(IPAddress);
            new RegisterTask().execute(new RegisterRequest(userName, password));
            requestSent = true;
        }
    }

    public void startPoller(){

        Poller.getInstance().start();
        ModelFacade.getInstance().setPollerMethodToCall(Poller.browserUpdateString);
    }


    @Override
    public void update(Observable o, Object arg) {

    }

    private class RegisterTask extends AsyncTask<RegisterRequest,Void,Response> {
        @Override
        protected Response doInBackground(RegisterRequest... registerRequest){
            return ServerProxy.getInstance().register(registerRequest[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            if(!(response instanceof ErrorResponse)){
                RegisterResponse registerResponse = (RegisterResponse) response;

                ModelFacade.getInstance().setUsername(registerResponse.getUsername());
                ModelFacade.getInstance().setAuthToken(registerResponse.getAuthToken());

                ModelFacade.getInstance().setGames(registerResponse.getUnstartedGames());

                startPoller();
                ModelFacade.getInstance().addGameBrowserObserver(GameBrowserPresenter.getInstance());

                ViewFacade.getInstance().changeToBrowserFragment(activity);
            }
            else{
                requestSent = false;
                ErrorResponse errorResponse = (ErrorResponse) response;
                ViewFacade.getInstance().showErrorMessageMain(activity, errorResponse.getMessage());
            }
        }
    }

    private class LoginTask extends AsyncTask<LoginRequest,Void,Response> {
        @Override
        protected Response doInBackground(LoginRequest... loginRequests){
            return ServerProxy.getInstance().login(loginRequests[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            if(!(response instanceof ErrorResponse)){
                LoginResponse loginResponse = (LoginResponse) response;

                ModelFacade.getInstance().setUsername(loginResponse.getUsername());
                ModelFacade.getInstance().setAuthToken(loginResponse.getAuthToken());

                ModelFacade.getInstance().setGames(loginResponse.getUnstartedGames());
                startPoller();
                ModelFacade.getInstance().addGameBrowserObserver(GameBrowserPresenter.getInstance());

                ViewFacade.getInstance().changeToBrowserFragment(activity);
            }
            else{
                requestSent = false;
                ErrorResponse errorResponse = (ErrorResponse) response;
                ViewFacade.getInstance().showErrorMessageMain(activity, errorResponse.getMessage());
            }
        }
    }
}

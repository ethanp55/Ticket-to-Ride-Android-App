package ateam.tickettoride;

import android.util.Log;

import java.util.List;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.common.Command;
import ateam.tickettoride.common.requests.UpdateGameBrowserRequest;
import ateam.tickettoride.common.requests.UpdateGameRequest;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.common.responses.UpdateGameBrowserResponse;
import ateam.tickettoride.common.responses.UpdateGameResponse;

/**
 * Class for regularly polling the server for updates.
 */
public class Poller implements Runnable{
    private static Poller singlePoller = null;

    //Strings for determining which update to ask for.
    public static final String browserUpdateString = "sendBrowserRequest";
    public static final String gameUpdateString = "sendGameUpdateRequest";

    private ModelFacade modelFacade;
    private Thread pollerThread;
    private boolean shouldRun;

    private int runInterval;

    public static Poller getInstance(){
        if(singlePoller == null){
            singlePoller = new Poller();
        }
        return singlePoller;
    }

    private Poller(){
        shouldRun = false;
        runInterval = 1000;
        pollerThread = new Thread(this);
        modelFacade = ModelFacade.getInstance();
    }

    /**
     * Starts the Poller running.
     */
    public void start(){
        pollerThread.start();
    }

    /**
     * At a regular interval, which can be set, sends requests to the server for updates,
     * the type being determined by a string in the model.
     */
    public void run(){
        shouldRun = true;

        while(shouldRun){
            //which method the poller should be calling
            String methodToCall = ModelFacade.getInstance().getPollerMethodToCall();

            if(methodToCall.equals("sendBrowserRequest")){
                Response response = sendBrowserRequest();
                //if the request was processed
                if(response instanceof UpdateGameBrowserResponse){
                    UpdateGameBrowserResponse goodResponse = (UpdateGameBrowserResponse) response;

                    //execute the commands received
                    List<Command> commands = goodResponse.getCommand();
                    for(int i = 0; i<commands.size(); i++){
                        commands.get(i).execute();
                    }
                    //update the model with the next command number to ask for
                    ModelFacade.getInstance().setGameBrowserCmdNumber(goodResponse.getNextCommandNumber());
                }
                //if something went wrong
                else{
                    processError((ErrorResponse) response);
                }
            }
            else if(methodToCall.equals("sendGameUpdateRequest")){
                Response response = sendGameUpdateRequest();
                //if the request was processed
                if(response instanceof UpdateGameResponse){
                    UpdateGameResponse goodResponse = (UpdateGameResponse) response;

                    //execute the commands received
                    List<Command> commands = goodResponse.getCommand();
                    for(int i = 0; i<commands.size(); i++){
                        commands.get(i).execute();
                    }
                    //update the model with the next command number to ask for
                    ModelFacade.getInstance().setGameUpdateCmdNumber(goodResponse.getNextCommandNumber());
//                    Log.i("Poller", "Up to command " + goodResponse.getNextCommandNumber());
                }
                else{
                    processError((ErrorResponse) response);
                }
            }

            try{
                //keep the regular intervals, instead of just spamming
                Thread.sleep(runInterval);
            }catch(InterruptedException e){
                System.out.println("Thread sleep interrupted in Poller.");
            }
        }
    }

    private Response sendBrowserRequest(){
        int nextCmd = ModelFacade.getInstance().getGameBrowserCmdNumber();
        UpdateGameBrowserRequest request = new UpdateGameBrowserRequest(modelFacade.getAuthToken(), nextCmd);
        return ServerProxy.getInstance().updateGameBrowser(request);
    }

    private Response sendGameUpdateRequest(){
        int nextCmd = ModelFacade.getInstance().getGameUpdateCmdNumber();
        UpdateGameRequest request = new UpdateGameRequest(nextCmd,
                modelFacade.getAuthToken(), ModelFacade.getInstance().getJoinedGame().getGameId());
        return ServerProxy.getInstance().updateGame(request);
    }

    /**
     * Does something with the ErrorResponses received from the ServerProxy.
     * @param response  The ErrorResponse to process.
     */
    private void processError(ErrorResponse response){
        System.out.println("Poller error message: " + response.getMessage());
    }

    /**
     * Stops the Poller from polling.
     */
    public void stop(){
        shouldRun = false;
        pollerThread.interrupt();
    }

    /**
     * Sets the interval, in milliseconds, between polls.
     * @param runInterval   The time, in milliseconds, to wait between polls.
     */
    public void setRunInterval(int runInterval) {
        this.runInterval = runInterval;
    }
}

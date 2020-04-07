package ateam.tickettoride;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ateam.tickettoride.Logging.GameLogging;
import ateam.tickettoride.Model.GameBrowserContainer;
import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.GameSummaryPresenter;
import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.common.IClient;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.GameInfo;

/**
 * Class for commands from the server to call in order to update the client.
 */
public class ClientFacade implements IClient{
    private static final String TAG = "ClientFacade";

    private static ClientFacade singleClientFacade;

    private ModelFacade modelFacade;

    /**
     * Returns the singleton instance.
     * @return  The singleton instance of ClientFacade.
     */
    public static ClientFacade getInstance(){
        if(singleClientFacade == null){
            singleClientFacade = new ClientFacade();
        }
        return singleClientFacade;
    }

    private ClientFacade(){
        modelFacade = ModelFacade.getInstance();
    }

    /**
     * Updates the game browser in the model.
     * @param gameArray An array of GameInfo to use to update the browser in the model.
     */
    @Override
    public void updateGameBrowser(GameInfo[] gameArray) {
        ModelFacade.getInstance().setGames(Arrays.asList(gameArray));
    }

    /**
     * Updates the game lobby the user is in.
     * @param playerNames   List of the names of the players in the lobby.
     * @param isStarted Whether or not the game has been started.
     */
    @Override
    public void updateLobby(String[] playerNames, Boolean isStarted) {
        modelFacade.getJoinedGame().setPlayerNames(Arrays.asList(playerNames));
        if(isStarted){
            //Call getGameSetup in GameSetupPresenter
            modelFacade.startGame();
        }
    }

    @Override
    public void updateChatHistory(String message) {
        Log.i(TAG, "updateChatHistory");
        PresenterFacade.getInstance().updateChat(message);
    }

    @Override
    public void updateGameHistory(String newEvent) {
        PresenterFacade.getInstance().updateGameHistory(newEvent);
    }

    @Override
    public void updatePlayerDestinationCards(String name, DestinationCard[] destinationCards) {
        Log.i(TAG, "Updating " + name + "'s destination cards");
        PresenterFacade.getInstance().updatePlayerDestinationCards(name, destinationCards);
    }

    @Override
    public void updatePlayerTrainCards(String name, TrainCard[] trainCards) {
        Log.i(TAG, "updating " + name + " train cards");
        PresenterFacade.getInstance().updatePlayerTrainCards(name, trainCards);
    }

    @Override
    public void updateTurn(Integer currentPlayerIndex) {
        Log.i(TAG, "updating turn to " + currentPlayerIndex);
        PresenterFacade.getInstance().updateTurn(currentPlayerIndex);
        Log.i(TAG, GameLogging.getCurrentGameState());
    }

    @Override
    public void updateFaceUpCards(FaceUpCards faceUpCards) {
        Log.i(TAG, "updating face up cards");
        PresenterFacade.getInstance().updateFaceUpCards(faceUpCards);
    }

    @Override
    public void updatePlayerPoints(String name, Integer points) {
        Log.i(TAG, "updating player points");
        PresenterFacade.getInstance().updatePlayerPoints(name, points);
    }

    @Override
    public void updatePlayerTrainPieces(String name, Integer numTrainPieces) {
        Log.i(TAG, "updating player train pieces");
        PresenterFacade.getInstance().updatePlayerTrainPieces(name, numTrainPieces);
    }

    @Override
    public void updatePlayerRoutes(String name, Route route) {
        Log.i(TAG, "updating player routes");
        PresenterFacade.getInstance().updatePlayerRoutes(name, route);
    }

    @Override
    public void updateClaimedGameRoutes(Route claimedRoute) {
        Log.i(TAG, "updating claimed game routes");
        PresenterFacade.getInstance().updateClaimedGameRoutes(claimedRoute);
    }

    @Override
    public void updateNumTrainCardsInDeck(Integer numTrainCards) {
        Log.i(TAG, "updating num train cards in deck");
        PresenterFacade.getInstance().updateNumTrainCardsInDeck(numTrainCards);
    }

    @Override
    public void updateNumDestinationCardsInDeck(Integer numDestinationCards) {
        Log.i(TAG, "updating num dest cards in deck");
        PresenterFacade.getInstance().updateNumDestinationCardsInDeck(numDestinationCards);
    }

    @Override
    public void lastRoundStarted() {
        Log.i(TAG, "last round started");
        PresenterFacade.getInstance().updateLastTurn();
    }

    @Override
    public void gameOver() {
        Log.i(TAG, "game over");
        PresenterFacade.getInstance().updateGameOver();
    }

    @Override
    public void allPlayersLongestPathCalculated(Integer[] longestPaths) {
        Log.i(TAG, "longest path calculated");
        PresenterFacade.getInstance().updatePlayerScore(longestPaths);
    }
}

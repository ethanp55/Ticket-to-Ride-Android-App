package ateam.tickettoride.View;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ateam.tickettoride.View.Activity.GameActivity;
import ateam.tickettoride.View.Activity.MainActivity;
import ateam.tickettoride.View.Fragment.DrawTrainCardFragment;
import ateam.tickettoride.View.Fragment.MapFragment;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCardDeck;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Player;

/**
 * A facade that anything that wants to interact with the views must call to change anything
 */
public class  ViewFacade {
    private static ViewFacade instance = null;

    public static ViewFacade getInstance(){
        if(instance == null){
            instance = new ViewFacade();
        }
        return instance;
    }

    private ViewFacade(){

    }

    public void changeToBrowserFragment(Activity activity){
        ((MainActivity)activity).goToBrowserFragment();
    }

    public void changeToLobbyFragment(Activity activity){
        ((MainActivity)activity).goToLobbyFragment();
    }

    public void updateGameBrowser(Activity activity){
        ((MainActivity)activity).updateGameBrowser();
    }

    public void updateGameLobby(Activity activity){
        ((MainActivity)activity).updateGameLobby();
    }

    public void moveToGameActivity(Activity activity){
        ((MainActivity)activity).moveToGameActivity();
    }

    public void exitGame(Activity activity) {
        ((GameActivity)activity).moveToMainActivity();
    }

    public void showErrorMessageMain(Activity activity, String errorMessage) {
        ((MainActivity)activity).showErrorMessage(errorMessage);
    }

    public void showErrorMessageGame(Activity activity, String errorMessage) {
        ((GameActivity)activity).showErrorMessage(errorMessage);
    }

    public void changeToMapFragment(Activity activity){
        ((GameActivity)activity).goToMapFragment();
    }

    public void updatePlayerTurn(Activity activity, int playerTurn){
        ((GameActivity)activity).updatePlayerTurn(playerTurn);
    }
    public void updateFaceUpCards(Activity activity, FaceUpCards faceUpCards){
        ((GameActivity)activity).updateFaceUpCards(faceUpCards);
    }

    public void updateNumTrainCardsInDeck(Activity activity, int deckSize){
        ((GameActivity)activity).updateNumTrainCardsInDeck(deckSize);
    }

    public void updatePlayers(Activity activity, Player[] players){
        ((GameActivity)activity).updatePlayers(players);
    }

    public void updateUnclaimedRoutes(Activity activity, ArrayList<Route> unclaimedRoutes){
        ((GameActivity)activity).updateUnclaimedRoutes(unclaimedRoutes);
    }

    public void updateChat(Activity activity, ArrayList<String> chatHistory) {
        ((GameActivity)activity).updateChat(chatHistory);
    }

    public void updateGameHistory(Activity activity, ArrayList<String> gameHistory) {
        ((GameActivity)activity).updateGameHistory(gameHistory);
    }

    public void updateGameHistoryEnd(Activity activity, ArrayList<String> gameHistory) {
        ((GameActivity)activity).updateGameHistoryEnd(gameHistory);
    }

    public void updateNumDestinationCardsInDeck(Activity activity, int deckSize){
        ((GameActivity)activity).updateNumDestinationCardsInDeck(deckSize);
    }

    public void updatePlayersForRoutes(Activity activity, Player[] players){
        ((GameActivity)activity).updatePlayersForRoutes(players);
    }

    public void updateDrawnDestCards(Activity activity, DestinationCard[] cards){
        ((GameActivity)activity).updateDrawnDestCards(cards);
    }

    public void updateLastTurn(Activity activity) {
        ((GameActivity)activity).updateLastTurn();
    }

    public void updateGameOver(Activity activity) {
        ((GameActivity)activity).goToEndgameFragment();
    }

    public void updateEndgameScore(Activity activity, Integer[] longestPaths) {
        ((GameActivity)activity).updatePlayersLongest(longestPaths);
    }
}

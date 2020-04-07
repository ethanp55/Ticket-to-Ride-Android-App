package ateam.tickettoride.Presenter;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;

import java.util.ArrayList;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.View.Fragment.DrawTrainCardFragment;
import ateam.tickettoride.View.Fragment.GameSummaryFragment;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Player;
import ateam.tickettoride.common.model.Game;

public class PresenterFacade {
    private static final String TAG = "PresenterFacade";
    private static PresenterFacade instance = null;

    public static PresenterFacade getInstance(){
        if(instance == null){
            instance = new PresenterFacade();
        }
        return instance;
    }

    public void discardInitialDestCard(DestinationCard cardToDiscard){
        GameSetupPresenter.getInstance().discardDestCards(cardToDiscard);
    }

    public void claimRoute(Route routeToClaim, Integer color){
        ClaimRoutePresenter.getInstance().claimRoute(routeToClaim, color);
    }

    public void claimRouteWithSelectedColor(Integer color){

    }

    public void drawFaceUpTrainCard(TrainCard card){

    }

    public void drawDeckTrainCard(){

    }

    public void drawDestCards(){

    }

    public void discardDestCards(ArrayList<DestinationCard> cards){

    }

    public void playTrainCards(ArrayList<TrainCard> cards){

    }

    public void setClaimRoutePresenterActivity(Activity activity){
        ClaimRoutePresenter.getInstance().setActivity(activity);
    }

    public void setDrawDestCardPresenterActivity(Activity activity){
        DrawDestCardPresenter.getInstance().setActivity(activity);
    }

    public void setDrawTrainCardPresenterActivity(Activity activity){
        DrawTrainCardPresenter.getInstance().setActivity(activity);
    }

    public void setGameSetupPresenterActivity(Activity activity){
        GameSetupPresenter.getInstance().setActivity(activity);
    }

    public void setMapPresenterActivity(Activity activity){
        MapPresenter.getInstance().setActivity(activity);
    }

    public void setGameSummaryPresenterActivity(Fragment parentFragment){
        Activity activity = parentFragment.getActivity();
        GameSummaryPresenter.getInstance().setParentFragment(parentFragment);
        GameSummaryPresenter.getInstance().setActivity(activity);
    }

    public void setEndgamePresenterActivity(Activity activity) {
        EndgamePresenter.getInstance().setActivity(activity);
    }

    public ArrayList<DestinationCard> getDestCardsSetup(){
        return GameSetupPresenter.getInstance().getDestinationCards();
    }

    public void updatePlayerDestinationCards(String name, DestinationCard[] destinationCards){
        MapPresenter.getInstance().updatePlayerDestinationCards(name, destinationCards);
    }

    public void updateTurn(Integer currentPlayerIndex){
        MapPresenter.getInstance().updateTurn(currentPlayerIndex);
    }

    public FaceUpCards getFaceUpCards(){
        return DrawTrainCardPresenter.getInstance().getFaceUpCards();
    }

    public void updateFaceUpCards(FaceUpCards faceUpCards){
        DrawTrainCardPresenter.getInstance().updateFaceUpCards(faceUpCards);
    }

    public Player updateHand() {
        return GameSummaryPresenter.getInstance().updateHand();
    }

    public Player[] updateSummary() {
        return GameSummaryPresenter.getInstance().updateSummary();
    }

    public void sendChat(String message) {
        GameSummaryPresenter.getInstance().sendChat(message);
    }

    public void updateChat(String message) {
        Log.i(TAG, "updateChat");
        GameSummaryPresenter.getInstance().updateChat(message);
    }

    public void updateGameHistory(String message) {
        Log.i(TAG, "updateGameHistory");
        GameSummaryPresenter.getInstance().updateGameHistory(message);
    }

    public void updateChatEnd(String message) {
        GameSummaryPresenter.getInstance().updateChat(message);
    }

    public void updateGameHistoryEnd(String message) {
        GameSummaryPresenter.getInstance().updateGameHistory(message);
    }

    public void updateLastTurn() {
        MapPresenter.getInstance().updateLastTurn();
    }

    public void updateGameOver() {
        MapPresenter.getInstance().updateGameOver();
    }

    public int getSizeOfTrainCardDeck(){
        return DrawTrainCardPresenter.getInstance().getSizeOfTrainCardDeck();
    }

    public int getSizeOfDestCardDeck(){
        return DrawDestCardPresenter.getInstance().getDestCardDeckSize();
    }

    public ArrayList<Route> getUnclaimedRoutes(){
        return ClaimRoutePresenter.getInstance().getUnclaimedRoutes();
    }

    public String getUsername(){
        return MapPresenter.getInstance().getUsername();
    }

    public Game getGame(){
        return MapPresenter.getInstance().getGame();
    }

    public void updatePlayerTrainCards(String name, TrainCard[] trainCards){
        DrawTrainCardPresenter.getInstance().updatePlayerTrainCards(name, trainCards);
    }

    public void updatePlayerPoints(String name, Integer points){
        GameSummaryPresenter.getInstance().updatePlayerPoints(name, points);
    }

    public void updatePlayerTrainPieces(String name, Integer numTrainPieces) {
        GameSummaryPresenter.getInstance().updatePlayerTrainPieces(name, numTrainPieces);
    }

    public void updatePlayerRoutes(String name, Route route) {
        MapPresenter.getInstance().updatePlayerRoutes(name, route);
    }

    public void updateClaimedGameRoutes(Route claimedRoute) {
        GameSummaryPresenter.getInstance().updateClaimedGameRoutes(claimedRoute);
    }

    public void updateNumTrainCardsInDeck(Integer numTrainCards) {
        GameSummaryPresenter.getInstance().updateNumTrainCardsInDeck(numTrainCards);
    }

    public void updateNumDestinationCardsInDeck(Integer numDestinationCards) {
        GameSummaryPresenter.getInstance().updateNumDestinationCardsInDeck(numDestinationCards);
    }

    public Player[] getPlayers(){
        return GameSummaryPresenter.getInstance().getPlayers();
    }

    public void setInMapFragment(Boolean inThere){
        ModelFacade.getInstance().setInMapFragment(inThere);
    }

    public boolean getInMapFragment(){
        return ModelFacade.getInstance().getInMapFragment();
    }

    public void updatePlayerScore(Integer[] longestPaths) {
        EndgamePresenter.getInstance().updateLongest(longestPaths);
    }

}

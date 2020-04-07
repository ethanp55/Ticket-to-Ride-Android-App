package ateam.tickettoride.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import ateam.tickettoride.Presenter.GameBrowserPresenter;
import ateam.tickettoride.Presenter.GameLobbyPresenter;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.GameInfo;
import ateam.tickettoride.common.model.Player;
import ateam.tickettoride.common.model.User;

/**
 * A facade that anything who interacts with the model will call to access model data
 */
public class ModelFacade {
    private static final String TAG = "ModelFacade";

    private static ModelFacade instance;

    /**
     * The method the poller should poll for requests
     */
    private String pollerMethodToCall;

    /**
     * The command number the game browser is at for polling purposes
     */
    private int gameBrowserCmdNumber = 0;

    /**
     * The command number the joined game is at for polling purposes
     */
    private int gameUpdateCmdNumber = 0;

    private boolean inMapFragment;

    public static ModelFacade getInstance(){
        if(instance == null){
            instance = new ModelFacade();
        }
        return instance;
    }

    private ModelFacade(){

    }

    ////////////////////////////////////////////////
    // Used for testing
    public Route getUnclaimedRoute(){
        Random ran = new Random();

        // Print next int value
        // Returns number between 0-10
        int index = ran.nextInt(RunningGameContainer.getInstance().getGame().getUnclaimedRoutes().size());

        return RunningGameContainer.getInstance().getGame().getUnclaimedRoutes().get(index);
    }

    public String getPlayer1Username(){
        return RunningGameContainer.getInstance().getGame().getPlayers()[0].getUsername();
    }

    public String getPlayer2Username(){
        return RunningGameContainer.getInstance().getGame().getPlayers()[1].getUsername();
    }

    public int getGameBrowserCmdNumber(){
        return gameBrowserCmdNumber;
    }

    public int getGameUpdateCmdNumber(){
        return gameUpdateCmdNumber;
    }

    public void setGameBrowserCmdNumber(int newNum){
        gameBrowserCmdNumber = newNum;
    }

    public void setGameUpdateCmdNumber(int newNum){
        gameUpdateCmdNumber = newNum;
    }

    public String getPollerMethodToCall(){
        return pollerMethodToCall;
    }

    public void setPollerMethodToCall(String pollerMethodToCall){
        this.pollerMethodToCall = pollerMethodToCall;
    }

    public void addGameBrowserObserver(Observer observer){
        GameBrowserContainer.getInstance().addObserver(observer);
    }

    public void addGameLobbyObserver(Observer observer){
        JoinedGameContainer.getInstance().addObserver(observer);
    }

    public void addRunningGameObserver(Observer observer){
        RunningGameContainer.getInstance().addObserver(observer);
    }

    public void startGame(){
        RunningGameContainer.getInstance().startGame();
    }

    public void setGame(Game game){
        RunningGameContainer.getInstance().setGame(game);
    }

    public JoinedGameContainer getJoinedGame() {
        return JoinedGameContainer.getInstance();
    }

    public void setJoinedGame(GameInfo joinedGame) {
        JoinedGameContainer.getInstance().setJoinedGameInfo(joinedGame);
    }

    public String getUsername(){
        return UserInfoContainer.getInstance().getUsername();
    }

    public String getAuthToken(){
        return UserInfoContainer.getInstance().getAuthToken();
    }

    public List<GameInfo> getGames() {
        return GameBrowserContainer.getInstance().getGames();
    }

    public void setGames(List<GameInfo> games) {
        GameBrowserContainer.getInstance().setGames(games);
    }

    public void setUsername(String username){
        UserInfoContainer.getInstance().setUsername(username);
    }

    public void setAuthToken(String authToken){
        UserInfoContainer.getInstance().setAuthToken(authToken);
    }

    public Player getPlayer(String username){
        Game game = RunningGameContainer.getInstance().getGame();

        return game.findPlayer(username);
    }

    public String getGameId(){
        Game game = RunningGameContainer.getInstance().getGame();

        return game.getGameID();
    }

    public Game getRunningGame(){
        return RunningGameContainer.getInstance().getGame();
    }

    public int getTrainCardDeckSize(){
        return RunningGameContainer.getInstance().getTrainCardDeckSize();
    }

    public int getDestCardDeckSize(){
        return RunningGameContainer.getInstance().getDestCardDeckSize();
    }

    public void updatePlayerDestinationCards(String name, DestinationCard[] destinationCards){
        RunningGameContainer.getInstance().updatePlayerDestinationCards(name, destinationCards);
    }

    public void updatePlayerTrainCards(String name, TrainCard[] trainCards){
        RunningGameContainer.getInstance().updatePlayerTrainCards(name, trainCards);
    }

    public void updatePlayerPoints(String name, Integer points) {
        RunningGameContainer.getInstance().updatePlayerPoints(name, points);
    }

    public void updatePlayerTrainPieces(String name, Integer numTrainPieces) {
        RunningGameContainer.getInstance().updatePlayerTrainPieces(name, numTrainPieces);
    }

    public void updatePlayerRoutes(String name, Route route) {
        RunningGameContainer.getInstance().updatePlayerRoutes(name, route);
    }

    public void updateClaimedGameRoutes(Route claimedRoute) {
        RunningGameContainer.getInstance().updateClaimedGameRoutes(claimedRoute);
    }

    public void updateNumTrainCardsInDeck(Integer numTrainCards) {
        RunningGameContainer.getInstance().updateNumTrainCardsInDeck(numTrainCards);
    }

    public void updateNumDestinationCardsInDeck(Integer numDestinationCards) {
        RunningGameContainer.getInstance().updateNumDestinationCardsInDeck(numDestinationCards);
    }

    public void updateTurn(Integer currentPlayerIndex){
        RunningGameContainer.getInstance().updateTurn(currentPlayerIndex);
    }

    public void deleteRunningGameObserver(Observer o){
        RunningGameContainer.getInstance().deleteObserver(o);
    }

    public void updateFaceUpCards(FaceUpCards faceUpCards){
        RunningGameContainer.getInstance().updateFaceUpCards(faceUpCards);
    }

    public ArrayList<Route> getUnclaimedRoutes(){
        Game game = RunningGameContainer.getInstance().getGame();
        return game.getUnclaimedRoutes();
    }

    public void updateChat(String message) {
        Log.i(TAG, "updateChat");
        RunningGameContainer.getInstance().updateChat(message);
    }

    public void updateGameHistory(String message) {
        RunningGameContainer.getInstance().updateGameHistory(message);
    }

    public void setInMapFragment(Boolean inThere){
        inMapFragment = inThere;
    }

    public boolean getInMapFragment(){
        return inMapFragment;
    }
}

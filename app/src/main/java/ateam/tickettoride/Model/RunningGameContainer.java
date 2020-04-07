package ateam.tickettoride.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.GameInfo;
import ateam.tickettoride.common.model.Player;

/**
 * A singleton class on the client that holds the information pertaining to the currently running game.
 */
public class RunningGameContainer extends Observable {
    private static String TAG = "RunningGameContainer";
    /**
     * The singleton instance of RunningGameContainer.
     */
    private static RunningGameContainer instance;

    /**
     * The game being contained.
     */
    private Game game = null;
    /**
     * The number of cards remaining in the server-side train card deck.
     */
    private int trainCardDeckSize;
    /**
     * The number of cards remaining in the server-side destination card deck.
     */
    private int destCardDeckSize;

    /**
     * Static method for obtaining the current instance of the RunningGameContainer.
     * @return  The current instance of RunningGameContainer.
     * @pre     None.
     * @post    The singleton instance of RunningGameContainer is not null.
     */
    public static RunningGameContainer getInstance(){
        if (instance == null){
            instance = new RunningGameContainer();
        }
        return instance;
    }

    private RunningGameContainer(){}

    /**
     * Sets the active game and sets the appropriate variables.
     * @pre     game is a valid game representation.
     * @post    This class's deck size variables are set correctly.
     */
    public void setGame(Game game){
        this.game = game;
        trainCardDeckSize = game.getTrainCardDeck().getCards().size();
        destCardDeckSize = game.getDestinationCardDeck().getCards().size();
    }

    /**
     * Notifies observers that the game is started.
     * @pre     None (Nothing will go wrong if there are no observers).
     * @post    Any observers are notified of the game starting.
     */
    public void startGame(){
        setChanged();
        notifyObservers("setup");
    }

    public Game getGame(){
        return game;
    }

    public int getDestCardDeckSize() {
        return destCardDeckSize;
    }

    int getTrainCardDeckSize() {
        return trainCardDeckSize;
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        super.deleteObserver(o);
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }

    @Override
    public void notifyObservers(Object arg) {
        super.notifyObservers(arg);
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }

    @Override
    protected synchronized void clearChanged() {
        super.clearChanged();
    }

    @Override
    public synchronized boolean hasChanged() {
        return super.hasChanged();
    }

    @Override
    public synchronized int countObservers() {
        return super.countObservers();
    }

    /**
     * Updates the destination cards that a player has to the given array of destination cards.
     * Also notifies observers.
     * @param name  The username of the player to be found.
     * @param destinationCards  The array of the destination cards to be found.
     * @pre     game is not null (the Game has been set).
     *          name is the username of a player in the game.
     * @post    The Player with the given username now has the given destination cards.
     *          Observers have been notified that player destination cards have been changed.
     */
    void updatePlayerDestinationCards(String name, DestinationCard[] destinationCards){
        ArrayList<DestinationCard> cards = new ArrayList<>(Arrays.asList(destinationCards));

        if(game.findPlayer(name) != null){
            game.findPlayer(name).setDestinationCards(cards);
        }
        else{
           Log.e(TAG, "ERROR in updatePlayerDestinationCards");
        }
        setChanged();
        notifyObservers("updatePlayerDestCards");
    }

    /**
     * Updates the turn to the given player index and notifies observers.
     * @param currentPlayerIndex    The index of the player whose turn it is.
     * @pre     The game has been set.
     * @post    The game's player turn index is now currentPlayerIndex.
     *          Observers are notified of a turn update.
     */
    void updateTurn(Integer currentPlayerIndex){
        game.setCurrentPlayer(currentPlayerIndex);
        setChanged();
        notifyObservers("updateTurn");
    }

    /**
     * Updates the game's chat history with the given message
     * and notifies observers.
     * @param message   The message to update the chat history with.
     * @pre     The game has been set.
     *          message is not null.
     * @post    The message is sent to the game's chat history.
     *          Observers are notified of a chat update.
     */
    void updateChat(String message) {
        Log.i(TAG, "updateChat");
        game.addChat(message);
        setChanged();
        notifyObservers("updateChat");
    }

    void updateGameHistory(String message) {
        game.addGameHistory(message);
        setChanged();
        notifyObservers("updateGameHistory");
    }

    /**
     * Updates the game's face-up cards with the given set of face-up cards.
     * Notifies observers of an update to the face-up cards.
     * @param faceUpCards   The FaceUpCards to be the game's new face-up cards.
     * @pre     The game has been set (to a non-null game).
     *          FaceUpCards is not null.
     * @post    The game's FaceUpCards is set to the given FaceUpCards.
     *          Observers are notified of an update to FaceUpCards.
     */
    void updateFaceUpCards(FaceUpCards faceUpCards){
        game.setFaceUpCards(faceUpCards);
        setChanged();
        notifyObservers("updateFaceUpCards");
    }

    /**
     * Updates the train cards for a given player.
     * Notifies observers.
     * @param name  The username of the player whose cards will be updated.
     * @param trainCards    The set of TrainCards to be the player's new cards.
     * @pre     The game has been set to a non-null game.
     *          name is the username of a player in the game.
     * @post    The player's hand of TrainCards is now the given set.
     *          Observers are notified of an update to a player's hand.
     */
    void updatePlayerTrainCards(String name, TrainCard[] trainCards){

        ArrayList<TrainCard> cards = new ArrayList<>(Arrays.asList(trainCards));

        if(game.findPlayer(name) != null){
            game.findPlayer(name).setTrainCards(cards);
        }
        else{
            Log.e(TAG, "ERROR in updatePlayerTrainCards");
        }
        setChanged();
        notifyObservers("updatePlayerTrainCards");
    }

    /**
     * Update the number of points for a player in the game and notifies observers.
     * @param name the username of the player to add/remove points
     * @param points the number of points the player now has
     * @pre     The game has been set to a non-null game.
     *          name is the username of a player in the game.
     * @post    The player with the given username now has the given number of points.
     *          Observers are notified of an update to player points.
     */
    void updatePlayerPoints(String name, int points){
        game.updatePlayerPoints(name, points);
        setChanged();
        notifyObservers("updatePlayerPoints");
    }

    /**
     * Updates the number of train pieces for a player in a game and updates observers.
     * @param name the username of the player to add/remove train pieces
     * @param numTrainPieces the number of train pieces the player will now have
     * @pre     The game has been set to a non-null game.
     *          name is the username of a player in the game.
     * @post    The given player has the given number of Train pieces.
     *          Observers are notified of an update to the number of train pieces a player has.
     */
    void updatePlayerTrainPieces(String name, int numTrainPieces){
        game.updatePlayerTrainPieces(name, numTrainPieces);
        setChanged();
        notifyObservers("updatePlayerTrainPieces");
    }

    /**
     * Updates the list of claimed routes of a player and updates observers.
     * @param name the username of the player
     * @param route the route to add to the player's list
     * @pre     The game has been set to a non-null game.
     *          name is the username of a player in the game.
     *          route is a non-null, valid route in the game.
     *          route has not been claimed by another player.
     * @post    The given player now owns the given route.
     *          Observers are notified of an update to route ownership.
     */
    void updatePlayerRoutes(String name, Route route){
        game.updatePlayerRoutes(name, route);
        setChanged();
        notifyObservers("updatePlayerRoutes");
    }

    /**
     * Updates the list of claimed routes in the game and notifies observers.
     * @param claimedRoute the route to add to the list
     * @pre     The game has been set to a non-null game.
     *          Route is a non-null, valid, unclaimed route in the game.
     * @post    The route is marked as claimed in the game.
     *          Observers are updated of a route being claimed.
     */
    void updateClaimedGameRoutes(Route claimedRoute){
        game.updateClaimedGameRoutes(claimedRoute);
        setChanged();
        notifyObservers("updateClaimedGameRoutes");
    }

    /**
     * Updates the number of cards in the TrainCard deck in the game and notifies observers of the change
     * @param numTrainCards the new number of train cards in the game deck
     * @pre     The game has been set to a non-null game.
     * @post    The number of cards in the train card deck is set to the given value.
     *          Observers are notified of a change in the size of the train card deck.
     */
    void updateNumTrainCardsInDeck(int numTrainCards){
        trainCardDeckSize = numTrainCards;
        setChanged();
        notifyObservers("updateNumTrainCardsInDeck");
    }

    /**
     * Updates the number of cards in the DestinationCard deck in the game and notifies observers of the change
     * @param numDestinationCards the new number of destination cards in the game deck
     * @pre     The game has been set to a non-null game.
     * @post    The number of cards in the destination card deck is set to the given value.
     *          Observers are notified of a change in the size of the destination card deck.
     */
    void updateNumDestinationCardsInDeck(int numDestinationCards){
        destCardDeckSize = numDestinationCards;
        setChanged();
        notifyObservers("updateNumDestinationCardsInDeck");
    }
}

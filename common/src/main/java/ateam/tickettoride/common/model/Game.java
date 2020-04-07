package ateam.tickettoride.common.model;


import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

import ateam.tickettoride.common.card.CardSetup;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.DestinationCardDeck;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.card.TrainCardDeck;
import ateam.tickettoride.common.map.Board;
import ateam.tickettoride.common.map.Route;

/**
 * Class representing what is in a game.
 */
public class Game implements Serializable {
    //The number of players currently in the game
    private int numPlayers;
    //The unique ID for the game.
    private String gameID;
    //The human-readable name for the game.
    private String gameName;
    //The players in the game.
    private Player[] players;
    //Whether or not the game has been started yet.
    private boolean started;
    //Whether or not the players in the game will start their last turns
    private boolean onLastRound;
    //Flag to indicate if the game is over or not
    private boolean finished;
    //The maximum number of players allowed in this game.
    private int maxPlayers;
    //Chat history
    private ArrayList<String> chatHistory;
    //The decks of train cards and destination cards
    private TrainCardDeck trainCardDeck;
    private DestinationCardDeck destinationCardDeck;
    //The five face up train cards
    private FaceUpCards faceUpCards;
    //The deck of discarded train cards
    private TrainCardDeck discardTrainCards;
    //The board/map
    private Board gameBoard;
    //The index of the current player (the player who is on their turn)
    private int currentPlayer;
    //List of claimed routes (routes that are unavailable for players to claim)
    private ArrayList<Route> claimedRoutes;
    //Game history
    private ArrayList<String> gameHistory;


    public Game(int maxNumPlayers, String name, String id, Player host){
        //assuming 1 < maxNumPlayers <= 5
        maxPlayers = maxNumPlayers;
        gameName = name;
        gameID = id;
        players = new Player[maxPlayers];
        players[0] = host;
        numPlayers = 1;
        started = false;
        onLastRound = false;
        finished = false;
        chatHistory = new ArrayList<>();
        //Set up the decks of cards
        CardSetup cardSetup = new CardSetup();
        cardSetup.setUpTrainCards(this);
        cardSetup.setUpDestinationCards(this);
        //Set up the board -> the constructor in the Board class sets everything up
        gameBoard = new Board();
        //Initialize the current player/turn index to -1 (when everyone is ready to play, it will be set to 0)
        currentPlayer = -1;
        claimedRoutes = new ArrayList<>();
        gameHistory = new ArrayList<>();
    }

    public boolean allPlayersReadyToPlay() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && !players[i].isReady()) {
                return false;
            }
        }

        return true;
    }

    //Adds another message to the chat history
    public void addChat(String message) {
        chatHistory.add(message);
    }

    public void addGameHistory(String message) {
        gameHistory.add(message);
    }

    //Adds discarded destination cards to the bottom of the destination card deck
    public void addDestinationDiscard(DestinationCard destinationCard) {
        destinationCardDeck.getCards().add(destinationCard);
    }

    //Changes player turns
    public void changeTurns() {
        //Increment the current player index
        currentPlayer++;

        //Make sure the index doesn't go out of bounds
        //If so, go back to the first player in the list (index 0)
        if (currentPlayer >= players.length) {
            currentPlayer = 0;
        }
    }

    /**
     * Attempts to add a player into the game. If there is space, it adds the player to the game and returns true.
     * If there is not space, returns false.
     * @param newPlayer The Player to add to the game.
     * @return  True if the player was added successfully, false otherwise.
     */
    public boolean addPlayer(Player newPlayer){
        for(int i = 0; i<players.length; i++){
            if(players[i] == null){
                //space for player, add them
                players[i] = newPlayer;
                numPlayers++;
                return true;
            }
        }
        //no space for the player
        return false;
    }

    /**
     * Attempts to remove the player from the game based on his username.
     * If the player is found in the game, he is removed and the method returns true.
     * @param username  The username of the player to remove.
     * @return  True if the player is found and removed. False otherwise.
     */
    public boolean removePlayer(String username){
        for(int i = 0; i<players.length; i++){
            if(players[i].getUsername().compareTo(username) == 0){
                //found the player, remove them
                players[i] = null;
                numPlayers--;
                return true;
            }
        }
        //could not find the player
        return false;
    }

    /**
     * Starts the game.
     */
    public void startGame(){
        started = true;

        //reduce players array to only actual players, not null
        Player[] array = new Player[numPlayers];
        int arrayPos = 0;
        for(int i = 0; i< players.length; i++){
            if(players[i] != null){
                array[arrayPos] = players[i];
                arrayPos++;
            }
        }
        players = array;

        for (int i = 0; i < players.length; i++) {
            giveCards(players[i]);
            assignColor(players[i], i);
        }
    }

    private void giveCards(Player player) {
        //Give the player 4 train cards
        for (int i = 0; i < 4; i++) {
            player.getTrainCards().add(trainCardDeck.drawCard());
        }

        //Give the player 3 destination cards
        for (int i = 0; i < 3; i++) {
            player.getDestinationCards().add(destinationCardDeck.drawCard());
        }
    }

    private void assignColor(Player player, int i) {
        if (i == 0) {
            player.setPlayerColor(Color.BLUE.getRGB());
        }

        else if (i == 1) {
            player.setPlayerColor(Color.RED.getRGB());
        }

        else if (i == 2) {
            player.setPlayerColor(Color.GREEN.getRGB());
        }

        else if (i == 3) {
            player.setPlayerColor(Color.YELLOW.getRGB());
        }

        else {
            player.setPlayerColor(Color.BLACK.getRGB());
        }
    }

    public int getNumPlayers(){
        return numPlayers;
    }

    public int getMaxPlayers(){
        return maxPlayers;
    }

    public boolean isStarted(){
        return started;
    }

    public String getGameID(){
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public ArrayList<String> getChatHistory() {
        return chatHistory;
    }

    public TrainCardDeck getTrainCardDeck() {
        return trainCardDeck;
    }

    public void setTrainCardDeck(TrainCardDeck trainCardDeck) {
        this.trainCardDeck = trainCardDeck;
    }

    public DestinationCardDeck getDestinationCardDeck() {
        return destinationCardDeck;
    }

    public FaceUpCards getFaceUpCards() {
        return faceUpCards;
    }

    public void setFaceUpCards(FaceUpCards faceUpCards) {
        this.faceUpCards = faceUpCards;
    }

    public TrainCardDeck getDiscardTrainCards() {
        return discardTrainCards;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isOnLastRound() {
        return onLastRound;
    }

    public void setOnLastRound(boolean onLastRound) {
        this.onLastRound = onLastRound;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setDiscardTrainCards(TrainCardDeck discardTrainCards) {
        this.discardTrainCards = discardTrainCards;
    }

    public void setDestinationCardDeck(DestinationCardDeck destinationCardDeck) {
        this.destinationCardDeck = destinationCardDeck;
    }

    public ArrayList<String> getGameHistory() {
        return gameHistory;
    }

    /**
     * Retrieves a GameInfo summary of the game for display in the Game Browser.
     * @return  A GameInfo object representing this game.
     */
    public GameInfo getGameInfo(){
        GameInfo info = new GameInfo(gameID, gameName, maxPlayers);
        for(int i = 0; i<players.length; i++){
            if(players[i] != null){
                info.addPlayerName(players[i].getUsername());
            }
        }

        return info;
    }

    public Player findPlayer(String username) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].getUsername().equals(username)) {
                return players[i];
            }
        }

        return null;
    }

    public void updatePlayerPoints(String name, int points) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUsername().equals(name)) {
                players[i].setPoints(points);
            }
        }
    }

    public void updatePlayerTrainPieces(String name, int numTrainPieces) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUsername().equals(name)) {
                players[i].setNumRemainingTrainPieces(numTrainPieces);
            }
        }
    }

    public void updatePlayerRoutes(String name, Route route) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getUsername().equals(name)) {
                players[i].getClaimedRoutes().add(route);
            }
        }
    }

    //Helper function for updating the five face up cards
    public void updateFaceUpCards(int index) {
        //If a player drew a face up card (index isn't -1), remove the card that was drawn from the face up pile
        if (index != -1) {
            faceUpCards.getTrainCards()[index] = null;
        }

        //Try to fill up the face up card pile
        fillUpFaceUpCards();

        //If there are now 3 locomotive cards in the face up pile, swap all of them out for 5 new cards
        if (threeLocomotivesInFaceUpCards()) {
            swapOutEveryFaceUpCard();
        }
    }

    //Helper function for trying to get 5 cards in the face up pile
    private void fillUpFaceUpCards() {
        //Shuffle the discard pile and put it in the train card deck
        addDiscardsToTrainDeck();

        //Try to fill up any missing face up cards
        for (int i = 0; i < faceUpCards.getTrainCards().length; i++) {
            if (faceUpCards.getTrainCards()[i] == null) {
                faceUpCards.getTrainCards()[i] = trainCardDeck.drawCard();
            }
        }
    }

    //Helper function for checking if there are 3 or more locomotives in the face up card pile
    private boolean threeLocomotivesInFaceUpCards() {
        int numLocomotives = 0;

        for (int i = 0; i < faceUpCards.getTrainCards().length; i++) {
            if (faceUpCards.getTrainCards()[i] != null && faceUpCards.getTrainCards()[i].getCardColor() == 0xff808080) {
                numLocomotives++;
            }
        }

        if (numLocomotives >= 3) {
            return true;
        }

        return false;
    }

    //Helper function for getting 5 new face up cards
    private void swapOutEveryFaceUpCard() {
        //Number of attempts the game makes to swap out the face up cards without giving 3 or more locomotives
        //This should rarely be a problem, but if there are no more train cards in the deck or discard pile there
        // could potentially be an infinite loop.  This numAttempts variable helps to prevent infinite looping.
        int numAttempts = 0;

        //Make sure there aren't 3 locomotives in the face up pile
        while (threeLocomotivesInFaceUpCards()) {
            numAttempts++;

            //Add the 5 face up cards to the discard pile
            for (int i = 0; i < faceUpCards.getTrainCards().length; i++) {
                if (faceUpCards.getTrainCards()[i] != null) {
                    discardTrainCards.getCards().add(faceUpCards.getTrainCards()[i]);
                }
            }

            //Shuffle the discard pile and put it in the train card deck
            addDiscardsToTrainDeck();

            //Get 5 train cards from the train card deck
            TrainCard[] trainCards = new TrainCard[5];

            for (int i = 0; i < trainCards.length; i++) {
                trainCards[i] = trainCardDeck.drawCard();
            }

            //Add the 5 new cards to the face up pile
            faceUpCards.setTrainCards(trainCards);

            //If the game has made a decent amount of attempts, clear the face up cards and break out of the while loop
            if (numAttempts > 15) {
                for (int i = 0; i < faceUpCards.getTrainCards().length; i++) {
                    if (faceUpCards.getTrainCards()[i] != null) {
                        discardTrainCards.getCards().add(faceUpCards.getTrainCards()[i]);
                        faceUpCards.getTrainCards()[i] = null;
                    }
                }

                addDiscardsToTrainDeck();

                break;
            }
        }
    }

    //Helper function for shuffling the train discards and putting them into the train card deck
    public void addDiscardsToTrainDeck() {
        //Shuffle the discard pile and put it in the train card deck
        discardTrainCards.shuffle();

        for (TrainCard card : discardTrainCards.getCards()) {
            trainCardDeck.getCards().add(card);
        }

        discardTrainCards.getCards().clear();

    }

    //Helper function for determining if there are less than 5 face-up cards
    public boolean lessThanFiveCardsInFaceUp() {
        for (int i = 0; i < faceUpCards.getTrainCards().length; i++) {
            if (faceUpCards.getTrainCards()[i] == null) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<Route> getUnclaimedRoutes(){
        ArrayList<Route> unclaimedRoute = new ArrayList<>(gameBoard.getRoutes());

        unclaimedRoute.removeAll(claimedRoutes);

        return unclaimedRoute;
    }

    //Helper function for removing available routes if a route between the same 2 cities is claimed and there are 2 or
    // 3 players in the game
    public Route removeRouteBetweenSameTwoCities(Route claimedRoute) {
        Route twinRoute = null;

        for (Route route : gameBoard.getRoutes()) {
            if (route.getCity1().equals(claimedRoute.getCity1()) && route.getCity2().equals(claimedRoute.getCity2())) {
                claimedRoutes.add(route);

                if (!route.equals(claimedRoute)) {
                    twinRoute = route;
                }
            }
        }

        return twinRoute;
    }

    public void updateClaimedGameRoutes(Route route){
        claimedRoutes.add(route);
    }

    public boolean allPlayersFinished() {
        for (int i = 0; i < players.length; i++) {
            if (!players[i].isFinishedLastTurn()) {
                return false;
            }
        }

        return true;
    }

    //Helper function for adding train cards to the discard pile
    //Called when a player claims a route
    public void addDiscards(TrainCard[] trainCards) {
        for (int i = 0; i < trainCards.length; i++) {
            discardTrainCards.getCards().add(trainCards[i]);
        }
    }

    //Helper function for checking if every player's longest path has been calculated (at the end of the game)
    public boolean allPlayersLongestPathCalculated() {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getLongestPath() == -1) {
                return false;
            }
        }

        return true;
    }

    public boolean playerInGame(String playerName) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].getUsername().equals(playerName)) {
                return true;
            }
        }

        return false;
    }
}
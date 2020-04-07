package ateam.tickettoride.common.model;

import java.io.Serializable;
import java.util.ArrayList;

import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.graph.MapGraph;
import ateam.tickettoride.common.graph.CityNode;
import ateam.tickettoride.common.graph.RouteEdge;
import ateam.tickettoride.common.map.City;
import ateam.tickettoride.common.map.Route;

/**
 * Class representing a player.
 * A player is different from a user.  A user simply contains a username, password, and authorization token.
 * A player is stored in a game and contains important information relating to the game.
 */
public class Player implements Serializable {
    /**
     * The player's username
     */
    private String username;
    /**
     * The ID of the game the player is in
     */
    private String gameID;
    /**
     * The lists of train cards, destination cards, and claimed routes a player has
     */
    private ArrayList<TrainCard> trainCards;
    private ArrayList<DestinationCard> destinationCards;
    private ArrayList<DestinationCard> destinationsCompleted;
    private ArrayList<DestinationCard> destinationsFailed;
    private ArrayList<Route> claimedRoutes;
    /**
     * The number of remaining train pieces a player has left
     */
    private int numRemainingTrainPieces;
    /**
     * The number of points a player has scored
     */
    private int points;
    private int totalPoints;
    private int destinationPointsGained;
    private int destinationPointsLost;
    /**
     * The int value of the color of the player
     */
    private int playerColor;
    /**
     * Flag used to indicate if a player is ready to play.  A game will start once all of its players are ready.
     */
    private boolean isReady;
    /**
     * Flag used to indicate if a player has finished their last turn.  Once every player in a game has finished, the game will end.
     */
    private boolean finishedLastTurn;
    /**
     * Graph of cities and routes based on a player's claimed routes.  Used to calculate completion of destination cards and longest route.
     */
    private MapGraph mapGraph;
    /**
     * The length of the players longest continuous path -> initialized to -1 and calculated at the end of the game
     */
    private int longestPath;

    /**
     * Constructor for a player object
     * @param name The username of the player
     * @param game The ID of the game that the player is in
     * @pre A valid username and game ID are needed to create a player object.  A user with the corresponding username must exist
     *  and a game with the corresponding game ID must exist
     * @post A player object with its corresponding fields will be created
     */
    public Player(String name, String game){
        username = name;
        gameID = game;
        trainCards = new ArrayList<>();
        destinationCards = new ArrayList<>();
        claimedRoutes = new ArrayList<>();
        numRemainingTrainPieces = 45;
        points = 0;
        totalPoints = 0;
        destinationPointsGained = 0;
        destinationPointsLost = 0;
        isReady = false;
        finishedLastTurn = false;
        mapGraph = new MapGraph(new ArrayList<CityNode>(), new ArrayList<RouteEdge>());
        longestPath = -1;
    }

    /**
     * Method for adding a train card to a player's list of train cards
     * @param trainCard The train card that is being added to the player's list of train cards
     * @pre A valid train card must be passed in
     * @post The train card will be added to the player's list of train cards
     */
    public void addTrainCard(TrainCard trainCard) {
        trainCards.add(trainCard);
    }

    /**
     * Method for removing a train card from a player's list of train cards
     * @param trainCard The train card that is being removed from the player's list of train cards
     * @pre A valid train card must be passed in
     * @post The train card will be removed from the player's list of train cards
     */
    public void removeTrainCard(TrainCard trainCard) {
        trainCards.remove(trainCard);
    }

    /**
     * Method for removing a destination card from a player's list of destination cards
     * @param destinationCard The destination card that is being removed from the player's list of destination cards
     * @pre A valid destination card must be passed in
     * @post The destination card will be removed from the player's list of destination cards
     */
    public void removeDestCard(DestinationCard destinationCard){
        destinationCards.remove(destinationCard);
    }

    /**
     * Method for adding a destination card to a player's list of destination cards
     * @param destinationCard The destination card that is being added to the player's list of destination cards
     * @pre A valid destination card must be passed in
     * @post The destination card will be removed from the player's list of destination cards
     */
    public void addDestinationCard(DestinationCard destinationCard) {
        destinationCards.add(destinationCard);
    }

    /**
     * Method for adding a claimed route to a player's list of claimed routes
     * When the player claims the route the train cards they use to do so will be consumed
     * @param route The route being claimed by the player
     * @param cardsUsedToClaimRoute The train cards they are using to claim the route
     * @pre A valid route must be passed in.  Also, a valid array of train cards must be passed in (the array cannot be empty and
     *  the cards must be applicable to the route)
     * @post The route will be added to the player's list of claimed routes and the train cards they used to claim the route will
     *  be removed from their list of train cards
     */
    public void addClaimedRoute(Route route, TrainCard[] cardsUsedToClaimRoute) {
        //Remove the train cards used to claim the route from the player's hand
        for (int i = 0; i < cardsUsedToClaimRoute.length; i++) {
            trainCards.remove(cardsUsedToClaimRoute[i]);
        }

        //Add the claimed route to the player's list of routes
        claimedRoutes.add(route);
    }

    /**
     * Method for decreasing the player's number of train pieces (when they claim a route)
     * @param numPieces The number of train pieces to be removed
     * @pre A valid, non-negative integer is passed in
     * @post The player's number of train pieces will be decremented by the number of train pieces that is passed in
     */
    public void decrementNumRemainingTrainPieces(int numPieces) {
        numRemainingTrainPieces = numRemainingTrainPieces - numPieces;
    }

    /**
     * Method for modifying a player's points
     * @param numPoints The number of points to be added or decremented from the player's points (the number passed in
     *  can be positive or negative: positive when the player gains points and negative when the player loses points
     *  for any incomplete destination cards at the end of the game)
     * @pre The number of points must be a valid integer
     * @post The player's points will be modified by the number of points passed in (if the number is positive then the player's
     *  points will increase by that amount and vice versa when the number is negative)
     */
    public void modifyPoints(int numPoints) {
        points = points + numPoints;
    }

    /**
     * Method for calculating the longest continuous path a player has
     * @return The length of the player's longest continuous path
     */
    public int calculateLongestPath() {
        int current;
        int longest = 0;
        ArrayList<CityNode> tempGraph = mapGraph.getCityNodes();

        for (CityNode node : tempGraph) {
            current = mapGraph.calculateLongestPath(node);

            if (current > longest) {
                longest = current;
            }
        }

        return longest;
    }

    /**
     * Method for updating a player's city graph for when they claim a route
     * @param claimedRoute The route the player claims on their turn (if they decide to claim a route)
     * @pre The route must be a valid route on the board and non null
     * @post The player's city graph will be updated based on the route they claimed (a route goes between 2 cities, so those cities and their
     *  adjacent routes will be added to the player's city graph)
     */
    public void updateCityGraph(Route claimedRoute) {
        //Find node1 in the graph
        CityNode node1 = mapGraph.findNode(claimedRoute.getCity1());

        //If node1 doesn't already exist in the graph, make a new node
        if (node1 == null) {
            node1 = new CityNode(new ArrayList<Route>(), claimedRoute.getCity1(), false);
            mapGraph.addCityNode(node1);
        }

        //Find node2 in the graph
        CityNode node2 = mapGraph.findNode(claimedRoute.getCity2());

        //If node2 doesn't already exist in the graph, make a new node
        if (node2 == null) {
            node2 = new CityNode(new ArrayList<Route>(), claimedRoute.getCity2(), false);
            mapGraph.addCityNode(node2);
        }

        //Update the adjacent routes to the nodes
        addRoutesToCityNode(node1);
        addRoutesToCityNode(node2);

        //Make a new route edge and add it to the map graph
        RouteEdge routeEdge = new RouteEdge(claimedRoute, false);
        mapGraph.addRouteEdge(routeEdge);
    }

    /**
     * Method for seeing if a player's destination cards are completed
     * @return A list of destination cards that previously weren't completed but now are
     */
    public ArrayList<DestinationCard> seeIfDestinationCardsAreFinished() {
        ArrayList<DestinationCard> completedCards = new ArrayList<>();

        //Check any destination card that isn't marked as completed
        for (DestinationCard card : destinationCards) {
            if (!card.isCompleted()) {
                mapGraph.checkIfDestinationIsCompleted(card);

                //If the card is now completed, add it to the list of newly completed cards
                if (card.isCompleted()) {
                    completedCards.add(card);
                }
            }
        }

        //Return the list of newly completed cards
        //If none of the cards were completed, the list will be empty
        return completedCards;
    }

    /**
     * Helper method for adding routes to a city node (based on the player's claimed routes)
     * @param cityNode The city node that we are adding routes to
     */
    private void addRoutesToCityNode(CityNode cityNode) {
        //Clear the list of connected routes (to avoid duplication)
        cityNode.getConnectedRoutes().clear();

        for (Route route : claimedRoutes) {
            if (route.getCity1().equals(cityNode.getCityName()) || route.getCity2().equals(cityNode.getCityName())) {
                cityNode.getConnectedRoutes().add(route);
            }
        }
    }

    private void calculateDestinationPoints() {
        destinationsCompleted = new ArrayList<>();
        destinationsFailed = new ArrayList<>();
        for(int i = 0; i < destinationCards.size(); i++) {
            DestinationCard tempCard = destinationCards.get(i);
            if(tempCard.isCompleted()) {
                destinationPointsGained += tempCard.getPointValue();
                destinationsCompleted.add(tempCard);
            }
            else {
                destinationPointsLost += tempCard.getPointValue();
                destinationsFailed.add(tempCard);
            }
        }
    }

    public void calculatePoints() {
        calculateDestinationPoints();
        totalPoints = (destinationPointsGained - destinationPointsLost) + points;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    public String getUsername(){
        return username;
    }

    public String getGameID(){
        return gameID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public ArrayList<TrainCard> getTrainCards() {
        return trainCards;
    }

    public void setTrainCards(ArrayList<TrainCard> trainCards) {
        this.trainCards = trainCards;
    }

    public ArrayList<DestinationCard> getDestinationCards() {
        return destinationCards;
    }

    public void setDestinationCards(ArrayList<DestinationCard> destinationCards) {
        this.destinationCards = destinationCards;
    }

    public ArrayList<Route> getClaimedRoutes() {
        return claimedRoutes;
    }

    public void setClaimedRoutes(ArrayList<Route> claimedRoutes) {
        this.claimedRoutes = claimedRoutes;
    }

    public int getNumRemainingTrainPieces() {
        return numRemainingTrainPieces;
    }

    public void setNumRemainingTrainPieces(int numRemainingTrainPieces) {
        this.numRemainingTrainPieces = numRemainingTrainPieces;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getDestinationPointsGained() {
        return destinationPointsGained;
    }

    public void setDestinationPointsGained(int destinationPointsGained) {
        this.destinationPointsGained = destinationPointsGained;
    }

    public int getDestinationPointsLost() {
        return destinationPointsLost;
    }

    public void setDestinationPointsLost(int destinationPointsLost) {
        this.destinationPointsLost = destinationPointsLost;
    }

    public MapGraph getMapGraph() {
        return mapGraph;
    }

    public void setMapGraph(MapGraph mapGraph) {
        this.mapGraph = mapGraph;
    }

    public boolean isFinishedLastTurn() {
        return finishedLastTurn;
    }

    public void setFinishedLastTurn(boolean finishedLastTurn) {
        this.finishedLastTurn = finishedLastTurn;
    }

    public ArrayList<DestinationCard> getDestinationsCompleted() {
        return destinationsCompleted;
    }

    public ArrayList<DestinationCard> getDestinationsFailed() {
        return destinationsFailed;
    }

    public int getLongestPath() {
        return longestPath;
    }

    public void setLongestPath(int longestPath) {
        this.longestPath = longestPath;
    }
}
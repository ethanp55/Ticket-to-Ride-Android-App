package ateam.tickettoride.server.ClientProxy;

import java.io.Serializable;
import java.util.ArrayList;

import ateam.tickettoride.common.Command;
import ateam.tickettoride.common.IClient;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.GameInfo;


/**
 * The GameProxy is the ClientProxy for users in a lobby.
 * Holds a list of commands
 * There are multiple instances of GameProxy, one for each game. Each player in a game shares a single GameProxy.
 * The GameProxy is separate from the GameBrowserProxy in order to prevent unnecessary polling
 * @invariant gameCommands.size() >= 0
 * @invariant GameProxy exists (The game has not ended)
 */
public class GameProxy implements IClient, Serializable {
    private ArrayList<Command> gameCommands;

    /**
     * Constructor
     * @pre None
     * @post Creates new List of commands.
     */
    public GameProxy() {
        gameCommands = new ArrayList<>();
    }

    @Override
    public void updateGameBrowser(GameInfo[] gameList) {
    }

    /**
     * @param playerNames Array of Strings that holds the name of players.
     * @param isStarted Boolean that stores whether the game is started or not.
     * @pre GameLobby has been created.
     * @pre playerNames != NULL
     * @post Adds updateLobby Command to list of commands
     */
    @Override
    public void updateLobby(String[] playerNames, Boolean isStarted) {
        String[] strings = new String[2];
        strings[0] = playerNames.getClass().getName();
        strings[1] = "java.lang.Boolean";
        Object[] objs = new Object[2];
        objs[0] = playerNames;
        objs[1] = isStarted;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updateLobby", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param message String containing the sent chat message.
     * @pre message != NULL
     * @post Adds updateChatHistory Command to list of commands.
     */
    @Override
    public void updateChatHistory(String message) {
        String[] strings = new String[1];
        strings[0] = message.getClass().getName();
        Object[] objs = new Object[1];
        objs[0] = message;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updateChatHistory", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param name String representing username of corresponding player.
     * @param destinationCards Array of Destination Cards to send back.
     * @pre name != NULL
     * @post Adds updatePlayerDestinationCards Command to list of commands.
     */
    @Override
    public void updatePlayerDestinationCards(String name, DestinationCard[] destinationCards) {
        String[] strings = new String[2];
        strings[0] = name.getClass().getName();
        strings[1] = destinationCards.getClass().getName();
        Object[] objs = new Object[2];
        objs[0] = name;
        objs[1] = destinationCards;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updatePlayerDestinationCards", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param name String representing username of corresponding player.
     * @param trainCards Array of Train Cards to send back.
     * @pre name != NULL
     * @pre trainCards != NULL
     * @post Adds updatePlayerTrainCards Command to list of commands.
     */
    @Override
    public void updatePlayerTrainCards(String name, TrainCard[] trainCards) {
        String[] strings = new String[2];
        strings[0] = name.getClass().getName();
        strings[1] = trainCards.getClass().getName();
        Object[] objs = new Object[2];
        objs[0] = name;
        objs[1] = trainCards;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updatePlayerTrainCards", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param currentPlayerIndex Integer of position in Player Array representing turn.
     * @pre 0 <= currentPlayerIndex < maxNumPlayers
     * @post Adds updateTurn Command to list of commands.
     */
    @Override
    public void updateTurn(Integer currentPlayerIndex) {
        String[] strings = new String[1];
        strings[0] = "java.lang.Integer";
        Object[] objs = new Object[1];
        objs[0] = currentPlayerIndex;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updateTurn", strings, objs);

        gameCommands.add(command);

    }

    /**
     * @param faceUpCards
     * @pre Face-Up Cards < 5
     * @post Adds updateFaceUpCards Command to list of commands.
     */
    @Override
    public void updateFaceUpCards(FaceUpCards faceUpCards) {
        String[] strings = new String[1];
        strings[0] = faceUpCards.getClass().getName();
        Object[] objs = new Object[1];
        objs[0] = faceUpCards;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updateFaceUpCards", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param name String representing username corresponding to player.
     * @param points Integer for number of points.
     * @pre name != NULL
     * @pre points != NULL
     * @post Adds updatePlayerPoints Command to list of commands.
     */
    @Override
    public void updatePlayerPoints(String name, Integer points) {
        String[] strings = new String[2];
        strings[0] = name.getClass().getName();
        strings[1] = "java.lang.Integer";
        Object[] objs = new Object[2];
        objs[0] = name;
        objs[1] = points;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updatePlayerPoints", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param name String representing username corresponding to player.
     * @param numTrainPieces Integer for number of train pieces.
     * @pre name != NULL
     * @pre numTrainPieces != NULL
     * @post Adds updatePlayerTrainPieces Command to list of commands.
     */
    @Override
    public void updatePlayerTrainPieces(String name, Integer numTrainPieces) {
        String[] strings = new String[2];
        strings[0] = name.getClass().getName();
        strings[1] = "java.lang.Integer";
        Object[] objs = new Object[2];
        objs[0] = name;
        objs[1] = numTrainPieces;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updatePlayerTrainPieces", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param name String representing username corresponding to player.
     * @param route Route to be added to player's list of claimed routes.
     * @pre name != NULL
     * @pre route != NULL
     * @post Adds updatePlayerRoutes Command to list of commands.
     */
    @Override
    public void updatePlayerRoutes(String name, Route route) {
        String[] strings = new String[2];
        strings[0] = name.getClass().getName();
        strings[1] = route.getClass().getName();
        Object[] objs = new Object[2];
        objs[0] = name;
        objs[1] = route;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updatePlayerRoutes", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param claimedRoute Route to be added to Game's list of claimed routes.
     * @pre claimedRoute != NULL
     * @post Adds updateClaimedGameRoutes Command to list of commands.
     */
    @Override
    public void updateClaimedGameRoutes(Route claimedRoute) {
        String[] strings = new String[1];
        strings[0] = claimedRoute.getClass().getName();
        Object[] objs = new Object[1];
        objs[0] = claimedRoute;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updateClaimedGameRoutes", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param numTrainCards Integer for number of Train Cards in Deck.
     * @pre numTrainCards != NULL
     * @post Adds updateNumTrainCardsInDeck Command to list of commands.
     */
    @Override
    public void updateNumTrainCardsInDeck(Integer numTrainCards) {
        String[] strings = new String[1];
        strings[0] = "java.lang.Integer";
        Object[] objs = new Object[1];
        objs[0] = numTrainCards;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updateNumTrainCardsInDeck", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param numDestinationCards Integer for number of Destination Cards in Deck.
     * @pre numDestinationCards != NULL
     * @post Adds updateNumDestinationCardsInDeck Command to list of commands.
     */
    @Override
    public void updateNumDestinationCardsInDeck(Integer numDestinationCards) {
        String[] strings = new String[1];
        strings[0] = "java.lang.Integer";
        Object[] objs = new Object[1];
        objs[0] = numDestinationCards;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updateNumDestinationCardsInDeck", strings, objs);

        gameCommands.add(command);
    }

    /**
     * @param newEvent String containing a String representation of a Command.
     * @pre newEvent != NULL
     * @post Adds updateGameHistory Command to list of commands.
     */
    @Override
    public void updateGameHistory(String newEvent) {
        String[] strings = new String[1];
        strings[0] = newEvent.getClass().getName();
        Object[] objs = new Object[1];
        objs[0] = newEvent;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updateGameHistory", strings, objs);

        gameCommands.add(command);
    }

    @Override
    public void lastRoundStarted() {
        Command command = new Command("ateam.tickettoride.ClientFacade", "lastRoundStarted", new String[0], new Object[0]);

        gameCommands.add(command);
    }

    @Override
    public void gameOver() {
        Command command = new Command("ateam.tickettoride.ClientFacade", "gameOver", new String[0], new Object[0]);

        gameCommands.add(command);
    }

    @Override
    public void allPlayersLongestPathCalculated(Integer[] longestPaths) {
        String[] strings = new String[1];
        strings[0] = longestPaths.getClass().getName();
        Object[] objs = new Object[1];
        objs[0] = longestPaths;

        Command command = new Command("ateam.tickettoride.ClientFacade", "allPlayersLongestPathCalculated", strings, objs);

        gameCommands.add(command);
    }

    public ArrayList<Command> getGameCommands() {
        return gameCommands;
    }
}

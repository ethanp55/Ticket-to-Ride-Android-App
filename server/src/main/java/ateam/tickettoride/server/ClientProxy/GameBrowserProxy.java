package ateam.tickettoride.server.ClientProxy;

import java.util.ArrayList;
import java.util.List;

import ateam.tickettoride.common.Command;
import ateam.tickettoride.common.IClient;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.GameInfo;

//The GameBrowserProxy is the ClientProxy for the GameBrowser.
//GameBrowserProxy holds a list of commands
//There is one and only one instance of a GameBrowserProxy. Each user will share this proxy's list of commands.
//The GameBrowserProxy is separate from the GameProxy in order to prevent unnecessary polling
public class GameBrowserProxy implements IClient {
    private static final GameBrowserProxy ourInstance = new GameBrowserProxy();
    private ArrayList<Command> browserCommands;

    public static GameBrowserProxy getInstance() {
        return ourInstance;
    }

    private GameBrowserProxy() {
        browserCommands = new ArrayList<>();
    }

    //Creates a command of updateGameBrowser and adds it to the list.
    @Override
    public void updateGameBrowser(GameInfo[] gameList) {
        System.out.println("Adding browser command...");

        String[] strings = new String[1];
        strings[0] = gameList.getClass().getName();
        Object[] objs = new Object[1];
        objs[0] = gameList;

        Command command = new Command("ateam.tickettoride.ClientFacade", "updateGameBrowser", strings, objs);

        browserCommands.add(command);
    }

    @Override
    public void updateLobby(String[] playerNames, Boolean isStarted) {
    }

    @Override
    public void updateChatHistory(String message) {
    }

    @Override
    public void updatePlayerDestinationCards(String name, DestinationCard[] destinationCards) {
    }

    @Override
    public void updatePlayerTrainCards(String name, TrainCard[] trainCards) {
    }

    @Override
    public void updateTurn(Integer currentPlayerIndex) {
    }

    @Override
    public void updateFaceUpCards(FaceUpCards faceUpCards) {
    }

    @Override
    public void updatePlayerPoints(String name, Integer points) {
    }

    @Override
    public void updatePlayerTrainPieces(String name, Integer numTrainPieces) {
    }

    @Override
    public void updatePlayerRoutes(String name, Route route) {
    }

    @Override
    public void updateClaimedGameRoutes(Route claimedRoute) {
    }

    @Override
    public void updateNumTrainCardsInDeck(Integer numTrainCards) {
    }

    @Override
    public void updateNumDestinationCardsInDeck(Integer numDestinationCards) {
    }

    @Override
    public void updateGameHistory(String newEvent) {
    }

    @Override
    public void lastRoundStarted() {
    }

    @Override
    public void gameOver() {
    }

    @Override
    public void allPlayersLongestPathCalculated(Integer[] longestPaths) {
    }

    public ArrayList<Command> getBrowserCommands() {
        return browserCommands;
    }
}

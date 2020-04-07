package ateam.tickettoride.common;

import ateam.tickettoride.common.card.DestinationCard;

import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.GameInfo;

/**
 * Interface for the ClientFacade and proxies on the server.
 */
public interface IClient {

    public void updateGameBrowser(GameInfo[] gameList);

    public void updateLobby(String[] playerNames, Boolean isStarted);

    public void updateChatHistory(String message);

    public void updatePlayerDestinationCards(String name, DestinationCard[] destinationCards);

    public void updatePlayerTrainCards(String name, TrainCard[] trainCards);

    public void updateTurn(Integer currentPlayerIndex);

    public void updateFaceUpCards(FaceUpCards faceUpCards);

    public void updatePlayerPoints(String name, Integer points);

    public void updatePlayerTrainPieces(String name, Integer numTrainPieces);

    public void updatePlayerRoutes(String name, Route route);

    public void updateClaimedGameRoutes(Route claimedRoute);

    public void updateNumTrainCardsInDeck(Integer numTrainCards);

    public void updateNumDestinationCardsInDeck(Integer numDestinationCards);

    public void updateGameHistory(String newEvent);

    public void lastRoundStarted();

    public void gameOver();

    public void allPlayersLongestPathCalculated(Integer[] longestPaths);
}



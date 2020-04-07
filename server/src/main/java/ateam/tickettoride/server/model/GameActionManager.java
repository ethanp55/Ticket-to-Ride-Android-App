package ateam.tickettoride.server.model;

import java.util.ArrayList;

import ateam.tickettoride.common.Command;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.Player;
import ateam.tickettoride.common.requests.ChatRequest;
import ateam.tickettoride.common.requests.ClaimRouteRequest;
import ateam.tickettoride.common.requests.CompletedDestinationCardRequest;
import ateam.tickettoride.common.requests.DiscardRequest;
import ateam.tickettoride.common.requests.DrawDestinationCardRequest;
import ateam.tickettoride.common.requests.DrawFaceUpCardRequest;
import ateam.tickettoride.common.requests.DrawTrainCardRequest;
import ateam.tickettoride.common.requests.InitialDiscardRequest;
import ateam.tickettoride.common.requests.LongestPathRequest;
import ateam.tickettoride.common.responses.DrawDestinationCardResponse;
import ateam.tickettoride.common.responses.DrawTrainCardResponse;
import ateam.tickettoride.common.responses.EmptyResponse;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.ClientProxy.GameProxy;
import ateam.tickettoride.server.ClientProxy.GameProxyContainer;
import ateam.tickettoride.server.persistence.PersistenceHolder;
import ateam.tickettoride.server.persistence.SerializerDeserializer;

//Class for performing any actions players make in a game
public class GameActionManager {
    private static final GameActionManager ourInstance = new GameActionManager();

    public static GameActionManager getInstance() {
        return ourInstance;
    }

    private GameActionManager() {
    }

    //Helper function for making a command when the database needs to store it
    private Command makeCommand(String method, Object request){
        Object[] objs = new Object[1];
        objs[0] = request;
        String[] strings = new String[1];
        strings[0] = request.getClass().getName();
        return new Command("ateam.tickettoride.server.service.ServerFacade", method, strings, objs);
    }

    //Helper function for storing commands in the database
    private void storeCommand(String gameID, Command command) {
        String serializedCommands = PersistenceHolder.getFactory().getCommandDao().getCommands(gameID);
        ArrayList<Command> commands = new ArrayList<>();
        if(serializedCommands != null){
            commands = (ArrayList<Command>)SerializerDeserializer.deserializeObject(serializedCommands);
            commands.add(command);
        }
        else{
            commands.add(command);
        }

        PersistenceHolder.getFactory().getCommandDao().replace(gameID, SerializerDeserializer.serializeObject(commands));
    }

    //Helper function for updating the database whenever the number of commands reaches the number of commands for a checkpoint
    private void checkNumCommandsForCheckpoint(Game game) {
        //Get the list of commands from the database
        String serializedCommands = PersistenceHolder.getFactory().getCommandDao().getCommands(game.getGameID());
        ArrayList<Command> commands = (ArrayList<Command>)SerializerDeserializer.deserializeObject(serializedCommands);

        //If the list has reached the number of commands for a checkpoint, perform the checkpoint
        if (commands.size() >= PersistenceHolder.getNumCommandsBetweenCheckpoints()) {
            //Replace the list of commands in the database with an empty list
            ArrayList<Command> emptyCommandList = new ArrayList<>();
            PersistenceHolder.getFactory().getCommandDao().replace(game.getGameID(), SerializerDeserializer.serializeObject(emptyCommandList));

            //Serialize the game and its proxy and put it in the database
            String serializedGame = SerializerDeserializer.serializeObject(game);
            String serializedGameProxy = SerializerDeserializer.serializeObject(GameProxyContainer.getInstance().getProxy(game.getGameID()));
            PersistenceHolder.getFactory().getGameDao().replace(game.getGameID(), serializedGame, serializedGameProxy);
        }
    }

    public Response setLongestPath(Game game, Player player, String authToken, int longestPath) {
        //Set the player's longest path
        player.setLongestPath(longestPath);

        //If all the player's in the game have their longest path calculated, send all the paths to each client
        if (game.allPlayersLongestPathCalculated()) {
            Integer[] longestPaths = new Integer[game.getPlayers().length];

            for (int i = 0; i < game.getPlayers().length; i++) {
                longestPaths[i] = game.getPlayers()[i].getLongestPath();
            }

            GameProxyContainer.getInstance().getProxy(game.getGameID()).allPlayersLongestPathCalculated(longestPaths);
        }

        //Store the command in the database
        LongestPathRequest request = new LongestPathRequest(authToken, game.getGameID(), longestPath);
        Command command = makeCommand("setLongestPath", request);
        storeCommand(game.getGameID(), command);

        //Perform a checkpoint if needed
        checkNumCommandsForCheckpoint(game);

        //Return an empty response to indicate that there were no errors
        return new EmptyResponse();
    }

    public Response completedDestinationCard(Game game, Player player, String authToken, DestinationCard destinationCard) {
        //Mark the player's destination card as completed
        for (DestinationCard card : player.getDestinationCards()) {
            if (card.equals(destinationCard)) {
                card.setCompleted(true);
            }
        }

        //Send the updated card to all the other clients/players
        DestinationCard[] cardArray = new DestinationCard[player.getDestinationCards().size()];
        DestinationCard[] cards = player.getDestinationCards().toArray(cardArray);

        GameProxyContainer.getInstance().getProxy(game.getGameID()).updatePlayerDestinationCards(player.getUsername(), cards);

        //Store the command in the database
        CompletedDestinationCardRequest request = new CompletedDestinationCardRequest(authToken, game.getGameID(), destinationCard);
        Command command = makeCommand("completedDestinationCard", request);
        storeCommand(game.getGameID(), command);

        //Perform a checkpoint if needed
        checkNumCommandsForCheckpoint(game);

        //Return an empty response to indicate that there were no errors
        return new EmptyResponse();
    }

    public Response claimRoute(Game game, Player player, String authToken, Route route, TrainCard[] cardsUsedToClaimRoute) {
        //Make sure the game isn't over yet
        if (game.isFinished()) {
            return new ErrorResponse("The game has ended");
        }

        //Find the game proxy
        GameProxyContainer gameProxyContainer = GameProxyContainer.getInstance();
        GameProxy gameProxy = gameProxyContainer.getProxy(game.getGameID());

        //If there are 2 or 3 players in a game, remove any available routes that go between the same two cities
        if (game.getNumPlayers() <= 3) {
            Route twinRoute = game.removeRouteBetweenSameTwoCities(route);

            if (twinRoute != null) {
                gameProxy.updateClaimedGameRoutes(twinRoute);
            }
        }

        //Give the claimed route to the player, increase their points, and decrease their number of train pieces
        player.addClaimedRoute(route, cardsUsedToClaimRoute);
        player.modifyPoints(route.getPointValue());
        player.decrementNumRemainingTrainPieces(route.getRouteLength());

        //Add the used train cards to the discard pile
        game.addDiscards(cardsUsedToClaimRoute);

        //If there were less than 5 face up cards when the player claimed a route, try to fill them up
        if (game.lessThanFiveCardsInFaceUp()) {
            game.updateFaceUpCards(-1);
            gameProxy.updateFaceUpCards(game.getFaceUpCards());
            gameProxy.updateNumTrainCardsInDeck(game.getTrainCardDeck().getCards().size());
        }

        //If there were no cards in the deck, try to add the player's discards to it
        if (game.getTrainCardDeck().getCards().size() == 0) {
            game.addDiscardsToTrainDeck();
            gameProxy.updateNumTrainCardsInDeck(game.getTrainCardDeck().getCards().size());
        }

        //Change and update the turn
        game.changeTurns();
        gameProxy.updateTurn(game.getCurrentPlayer());

        //Update the player's points, number of claimed routes, and number of train pieces
        //Also update the available routes in the game
        gameProxy.updatePlayerPoints(player.getUsername(), player.getPoints());
        gameProxy.updatePlayerTrainPieces(player.getUsername(), player.getNumRemainingTrainPieces());
        gameProxy.updatePlayerRoutes(player.getUsername(), route);
        gameProxy.updateClaimedGameRoutes(route);

        //Convert the list of the player's train cards to an array (for JSON purposes)
        TrainCard[] array = new TrainCard[player.getTrainCards().size()];
        TrainCard[] array1 = player.getTrainCards().toArray(array);

        //Update so everyone can see how many cards the player now has
        gameProxy.updatePlayerTrainCards(player.getUsername(), array1);

        //Update gameHistory
        game.getGameHistory().add(player.getUsername() + " claimed a route. +" + route.getPointValue() + " points!");
        gameProxy.updateGameHistory(player.getUsername() + " claimed a route. +" + route.getPointValue() + " points!");

        //If the game is on the last round, indicate that the player has finished their last turn
        if (game.isOnLastRound()) {
            player.setFinishedLastTurn(true);

            //If the game is on the last round and all players have finished their last turn, end the game and let all players know that it is over
            if (game.allPlayersFinished()) {
                game.setFinished(true);
                gameProxy.gameOver();

                //Store the command in the database
                ClaimRouteRequest request = new ClaimRouteRequest(authToken, game.getGameID(), route, cardsUsedToClaimRoute);
                Command command = makeCommand("claimRoute", request);
                storeCommand(game.getGameID(), command);

                //Perform a checkpoint if needed
                checkNumCommandsForCheckpoint(game);

                return new EmptyResponse();
            }
        }

        //If the player now has 2 or fewer train pieces, start the last round if it hasn't already started
        if (player.getNumRemainingTrainPieces() <= 2 && !game.isOnLastRound()) {
            game.setOnLastRound(true);
            gameProxy.lastRoundStarted();
        }

        //Store the command in the database
        ClaimRouteRequest request = new ClaimRouteRequest(authToken, game.getGameID(), route, cardsUsedToClaimRoute);
        Command command = makeCommand("claimRoute", request);
        storeCommand(game.getGameID(), command);

        //Perform a checkpoint if needed
        checkNumCommandsForCheckpoint(game);

        //Return an empty response to indicate there were no errors
        return new EmptyResponse();
    }

    public Response drawTrainCard(Game game, Player player, String authToken, boolean changeTurn) {
        //Make sure the game isn't over yet
        if (game.isFinished()) {
            return new ErrorResponse("The game has ended");
        }

        //Draw a train card
        TrainCard card = game.getTrainCardDeck().drawCard();

        //If the card is null, shuffle the discard deck/pile and put them into the train card deck
        if (card == null) {
            game.getDiscardTrainCards().shuffle();
            game.setTrainCardDeck(game.getDiscardTrainCards());
            game.getDiscardTrainCards().getCards().clear();

            card = game.getTrainCardDeck().drawCard();

            //If the card is still null, that means that there are no more train cards in both the deck and discard pile
            if (card == null) {
                //Store the command in the database
                DrawTrainCardRequest request = new DrawTrainCardRequest(authToken, game.getGameID(), changeTurn);
                Command command = makeCommand("drawTrainCard", request);
                storeCommand(game.getGameID(), command);

                return new DrawTrainCardResponse(null);
            }
        }

        //Add the train card to the player's hand
        player.addTrainCard(card);

        //Find the game proxy
        GameProxyContainer gameProxyContainer = GameProxyContainer.getInstance();
        GameProxy gameProxy = gameProxyContainer.getProxy(game.getGameID());

        //Convert the list of cards to an array (for JSON purposes)
        TrainCard[] array = new TrainCard[player.getTrainCards().size()];
        TrainCard[] array1 = player.getTrainCards().toArray(array);

        //Update so everyone can see how many cards the player now has
        gameProxy.updatePlayerTrainCards(player.getUsername(), array1);

        //Update the number of cards in the deck
        gameProxy.updateNumTrainCardsInDeck(game.getTrainCardDeck().getCards().size());

        //Update gameHistory
        game.getGameHistory().add(player.getUsername() + " drew a train card!");
        gameProxy.updateGameHistory(player.getUsername() + " drew a train card!");

        //If the game is on the last round, indicate that the player has finished their last turn
        if (game.isOnLastRound() && changeTurn) {
            player.setFinishedLastTurn(true);

            //If the game is on the last round and all players have finished their last turn, end the game and let all players know that it is over
            if (game.allPlayersFinished()) {
                game.setFinished(true);
                gameProxy.gameOver();

                //Store the command in the database
                DrawTrainCardRequest request = new DrawTrainCardRequest(authToken, game.getGameID(), changeTurn);
                Command command = makeCommand("drawTrainCard", request);
                storeCommand(game.getGameID(), command);

                //Perform a checkpoint if needed
                checkNumCommandsForCheckpoint(game);

                return new DrawTrainCardResponse(card);
            }
        }

        //If this is the end of the player's turn, change and update the turn
        if (changeTurn) {
            game.changeTurns();
            gameProxy.updateTurn(game.getCurrentPlayer());
        }

        //Store the command in the database
        DrawTrainCardRequest request = new DrawTrainCardRequest(authToken, game.getGameID(), changeTurn);
        Command command = makeCommand("drawTrainCard", request);
        storeCommand(game.getGameID(), command);

        //Perform a checkpoint if needed
        checkNumCommandsForCheckpoint(game);

        //Return the train card to the player
        return new DrawTrainCardResponse(card);
    }

    public Response drawFaceUpCard(Game game, Player player, String authToken, int cardIndex, boolean changeTurn) {
        //Make sure the game isn't over yet
        if (game.isFinished()) {
            return new ErrorResponse("The game has ended");
        }

        //Find the game proxy
        GameProxyContainer gameProxyContainer = GameProxyContainer.getInstance();
        GameProxy gameProxy = gameProxyContainer.getProxy(game.getGameID());

        //Add the card to the player's hand
        TrainCard card = game.getFaceUpCards().getTrainCards()[cardIndex];

        player.addTrainCard(card);

        //Convert the list of cards to an array (for JSON purposes)
        TrainCard[] array = new TrainCard[player.getTrainCards().size()];
        TrainCard[] array1 = player.getTrainCards().toArray(array);

        //Update so everyone can see how many cards the player now has
        gameProxy.updatePlayerTrainCards(player.getUsername(), array1);

        //Update the game's face up cards
        game.updateFaceUpCards(cardIndex);
        gameProxy.updateFaceUpCards(game.getFaceUpCards());

        //Update the number of cards in the deck
        gameProxy.updateNumTrainCardsInDeck(game.getTrainCardDeck().getCards().size());

        //Update gameHistory
        game.getGameHistory().add(player.getUsername() + " drew a face up train card!");
        gameProxy.updateGameHistory(player.getUsername() + " drew a face up train card!");

        //If the game is on the last round, indicate that the player has finished their last turn
        if (game.isOnLastRound()) {
            player.setFinishedLastTurn(true);

            //If the game is on the last round and all players have finished their last turn, end the game and let all players know that it is over
            if (game.allPlayersFinished() && changeTurn) {
                game.setFinished(true);
                gameProxy.gameOver();

                //Store the command in the database
                DrawFaceUpCardRequest request = new DrawFaceUpCardRequest(authToken, game.getGameID(), cardIndex, changeTurn);
                Command command = makeCommand("drawFaceUpCard", request);
                storeCommand(game.getGameID(), command);

                //Perform a checkpoint if needed
                checkNumCommandsForCheckpoint(game);

                return new EmptyResponse();
            }
        }

        //If this is the end of the player's turn, change and update the turn
        if (changeTurn) {
            game.changeTurns();
            gameProxy.updateTurn(game.getCurrentPlayer());
        }

        //Store the command in the database
        DrawFaceUpCardRequest request = new DrawFaceUpCardRequest(authToken, game.getGameID(), cardIndex, changeTurn);
        Command command = makeCommand("drawFaceUpCard", request);
        storeCommand(game.getGameID(), command);

        //Perform a checkpoint if needed
        checkNumCommandsForCheckpoint(game);

        //Return an empty response to indicate there were no errors
        return new EmptyResponse();
    }

    public Response drawDestinationCard(Game game, Player player, String authToken) {
        //Make sure the game isn't over yet
        if (game.isFinished()) {
            return new ErrorResponse("The game has ended");
        }

        //Draw 3 destination cards from the top of the deck and add it to the player's hand
        DestinationCard[] cards = new DestinationCard[3];

        for (int i = 0; i < 3; i++) {
            DestinationCard card = game.getDestinationCardDeck().drawCard();

            //If the deck is out of destination cards, don't try to give any to the player
            if (card == null) {
                break;
            }

            cards[i] = card;
            player.addDestinationCard(card);
        }

        //If no destination cards could be drawn (the deck is empty), return an error response
        if (destinationCardArrayIsEmpty(cards)) {
            return new ErrorResponse("Destination card deck is empty");
        }

        //Find the game proxy
        GameProxyContainer gameProxyContainer = GameProxyContainer.getInstance();
        GameProxy gameProxy = gameProxyContainer.getProxy(game.getGameID());

        //Convert the list of cards to an array (for JSON purposes)
        DestinationCard[] array = new DestinationCard[player.getDestinationCards().size()];
        DestinationCard[] array1 = player.getDestinationCards().toArray(array);

        //Update the player's cards so other players can see how many they have
        gameProxy.updatePlayerDestinationCards(player.getUsername(), array1);

        //Update the number of cards in the destination card deck
        gameProxy.updateNumDestinationCardsInDeck(game.getDestinationCardDeck().getCards().size());

        //Update gameHistory
        game.getGameHistory().add(player.getUsername() + " drew destination cards!");
        gameProxy.updateGameHistory(player.getUsername() + " drew destination cards!");

        //Store the command in the database
        DrawDestinationCardRequest request = new DrawDestinationCardRequest(authToken, game.getGameID());
        Command command = makeCommand("drawDestinationCard", request);
        storeCommand(game.getGameID(), command);

        //Perform a checkpoint if needed
        checkNumCommandsForCheckpoint(game);

        //Return the 3 destination cards
        return new DrawDestinationCardResponse(cards);
    }

    public Response discard(Game game, Player player, String authToken, ArrayList<DestinationCard> cards) {
        //Find the game proxy
        GameProxyContainer gameProxyContainer = GameProxyContainer.getInstance();
        GameProxy gameProxy = gameProxyContainer.getProxy(game.getGameID());

        //If the player isn't discarding any destination cards, change and update the turn and return
        if (cards == null) {
            game.changeTurns();
            gameProxy.updateTurn(game.getCurrentPlayer());

            //Store the command in the database
            DiscardRequest request = new DiscardRequest(authToken, game.getGameID(), cards);
            Command command = makeCommand("drawDestinationCard", request);
            storeCommand(game.getGameID(), command);

            //Perform a checkpoint if needed
            checkNumCommandsForCheckpoint(game);

            return new EmptyResponse();
        }

        //Remove the cards from the player's hand and put them at the bottom of the deck
        for (DestinationCard card: cards) {
            player.removeDestCard(card);
            game.addDestinationDiscard(card);
        }

        //Convert the list of cards to an array (for JSON purposes)
        DestinationCard[] array = new DestinationCard[player.getDestinationCards().size()];
        DestinationCard[] array1 = player.getDestinationCards().toArray(array);

        //Update the player's cards so other players can see how many they have
        gameProxy.updatePlayerDestinationCards(player.getUsername(), array1);

        //Update the number of cards in the destination card deck
        gameProxy.updateNumDestinationCardsInDeck(game.getDestinationCardDeck().getCards().size());

        //If the player discarded a card/multiple cards, display it in the game history
        if (cards.size() != 0) {
            //Update gameHistory
            game.getGameHistory().add(player.getUsername() + " discarded " + cards.size() + " destination card(s)");
            gameProxy.updateGameHistory(player.getUsername() + " discarded " + cards.size() + " destination card(s)");
        }

        //If the game is on the last round, indicate that the player has finished their last turn
        if (game.isOnLastRound()) {
            player.setFinishedLastTurn(true);

            //If the game is on the last round and all players have finished their last turn, end the game and let all players know that it is over
            if (game.allPlayersFinished()) {
                game.setFinished(true);
                gameProxy.gameOver();

                //Store the command in the database
                DiscardRequest request = new DiscardRequest(authToken, game.getGameID(), cards);
                Command command = makeCommand("drawDestinationCard", request);
                storeCommand(game.getGameID(), command);

                //Perform a checkpoint if needed
                checkNumCommandsForCheckpoint(game);

                return new EmptyResponse();
            }
        }

        //Change and update the turn
        game.changeTurns();
        gameProxy.updateTurn(game.getCurrentPlayer());

        //Store the command in the database
        DiscardRequest request = new DiscardRequest(authToken, game.getGameID(), cards);
        Command command = makeCommand("drawDestinationCard", request);
        storeCommand(game.getGameID(), command);

        //Perform a checkpoint if needed
        checkNumCommandsForCheckpoint(game);

        //Return an empty response to indicate there were no errors
        return new EmptyResponse();
    }

    public Response initialDiscard(Game game, Player player, String authToken, DestinationCard destinationCard) {
        //Indicate that the player has made their initial discards and is now ready to play
        player.setReady(true);

        //Find the game proxy
        GameProxyContainer gameProxyContainer = GameProxyContainer.getInstance();
        GameProxy gameProxy = gameProxyContainer.getProxy(game.getGameID());

        //Check to see if they want to discard one of their initial cards
        if (destinationCard != null) {
            //Remove the card from their hand
            player.removeDestCard(destinationCard);

            //Put the card at the bottom of the deck
            game.addDestinationDiscard(destinationCard);

            //Update this information for other players to be able to see how many destination cards this player has
            DestinationCard[] array = new DestinationCard[player.getDestinationCards().size()];
            DestinationCard[] array1 = player.getDestinationCards().toArray(array);

            gameProxy.updatePlayerDestinationCards(player.getUsername(), array1);

            //Update the number of cards in the destination card deck
            gameProxy.updateNumDestinationCardsInDeck(game.getDestinationCardDeck().getCards().size());

            //Update gameHistory
            game.getGameHistory().add(player.getUsername() + " discarded one of their initial destination cards.");
            gameProxy.updateGameHistory(player.getUsername() + " discarded one of their initial destination cards.");
        }

        //If everyone is now ready to play, set the player turn index to 0 (the first player in the player list)
        if (game.allPlayersReadyToPlay()) {
            game.setCurrentPlayer(0);
            gameProxy.updateTurn(0);
        }

        //Store the command in the database
        InitialDiscardRequest request = new InitialDiscardRequest(authToken, game.getGameID(), destinationCard);
        Command command = makeCommand("drawDestinationCard", request);
        storeCommand(game.getGameID(), command);

        //Perform a checkpoint if needed
        checkNumCommandsForCheckpoint(game);

        //Return an empty response to indicate there were no errors
        return new EmptyResponse();
    }

    public Response sendChatMessage(Game game, String playerUserName, String authToken, String message) {
        //Add the message to the game's chat history
        game.addChat(playerUserName + ": " + message);

        //Find the game proxy
        GameProxyContainer gameProxyContainer = GameProxyContainer.getInstance();
        GameProxy gameProxy = gameProxyContainer.getProxy(game.getGameID());

        //Update the chat history
        gameProxy.updateChatHistory(playerUserName + ": " + message);

        //Update gameHistory
        game.getGameHistory().add(playerUserName + " sent a chat message.");
        gameProxy.updateGameHistory(playerUserName + " sent a chat message.");

        //Store the command in the database
        ChatRequest request = new ChatRequest(authToken, game.getGameID(), message);
        Command command = makeCommand("drawDestinationCard", request);
        storeCommand(game.getGameID(), command);

        //Perform a checkpoint if needed
        checkNumCommandsForCheckpoint(game);

        //Return an empty response to indicate there were no errors
        return new EmptyResponse();
    }

    private boolean destinationCardArrayIsEmpty(DestinationCard[] cards) {
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != null) {
                return false;
            }
        }

        return true;
    }
}
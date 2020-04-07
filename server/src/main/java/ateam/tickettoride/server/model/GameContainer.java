package ateam.tickettoride.server.model;

import java.util.ArrayList;
import java.util.UUID;

import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.GameInfo;
import ateam.tickettoride.common.model.Player;
import ateam.tickettoride.common.responses.CreateGameResponse;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.GameSetupResponse;
import ateam.tickettoride.common.responses.JoinGameResponse;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.common.responses.StartGameResponse;
import ateam.tickettoride.server.ClientProxy.GameBrowserProxy;
import ateam.tickettoride.server.ClientProxy.GameProxy;
import ateam.tickettoride.server.ClientProxy.GameProxyContainer;
import ateam.tickettoride.server.persistence.PersistenceHolder;
import ateam.tickettoride.server.persistence.SerializerDeserializer;

public class GameContainer {
    private static final GameContainer ourInstance = new GameContainer();

    private ArrayList<GameInfo> unstartedGames;
    private ArrayList<Game> allGames;

    static GameContainer getInstance() {
        return ourInstance;
    }

    /**
     * The GameContainer constructor
     * @pre
     * @post The unstartedGames and allGames arrays are initialized
     */
    private GameContainer() {
        unstartedGames = new ArrayList<>();
        allGames = new ArrayList<>();
    }

    public Response setLongestPath(String authToken, String gameID, int longestPath) {
        //Find the game using the game ID
        Game game = findGame(gameID);

        //Make sure the game was found
        if (game == null) {
            return new ErrorResponse("Game not found");
        }

        //Find the player object in the game
        Player player = game.findPlayer(getPlayersUserName(authToken));

        //Have the game action manager set the player's longest path and check if all the other players in the game
        // have their longest paths calculated
        return GameActionManager.getInstance().setLongestPath(game, player, authToken, longestPath);
    }

    public Response completedDestinationCard(String authToken, String gameID, DestinationCard destinationCard) {
        //Find the game using the game ID
        Game game = findGame(gameID);

        //Make sure the game was found
        if (game == null) {
            return new ErrorResponse("Game not found");
        }

        //Find the player object in the game
        Player player = game.findPlayer(getPlayersUserName(authToken));

        //Mark the player's destination card as completed
        for (DestinationCard card : player.getDestinationCards()) {
            if (card.equals(destinationCard)) {
                card.setCompleted(true);
            }
        }

        //Send the updated card to all the other clients/players
        DestinationCard[] cards = new DestinationCard[1];
        cards[0] = destinationCard;

        GameProxyContainer.getInstance().getProxy(gameID).updatePlayerDestinationCards(getPlayersUserName(authToken), cards);

        //Have the game action manager perform the player's action
        return GameActionManager.getInstance().completedDestinationCard(game, player, authToken, destinationCard);
    }

    /**
     * The player associated with the authToken claims the route in the game represented by the gameID.
     * @param authToken the authentication token associated with a player in the game
     * @param gameID the game identification string that will be used to find the specific game
     * @param route the route the player wants to claim
     * @param cardsUsedToClaimRoute the cards the user will use to claim the route
     * @return an response with an error string if something went wrong or an empty response if the route
     * claiming was successful
     * @pre a valid authentication token must be given, a valid gameId must be given, the route must be
     * unclaimed and a valid route, and the train cards must match the color (or fill with locomotive cards)
     * of the route.
     * @post The route will be removed from the unclaimed route list, the route will be added to the player's
     * list of claimed routes, the player's points will be incremented based on the length of the claimed route,
     * the turn is incremented so the next player can do their turn, all other players are notified that the player
     * claimed the route
     */

    public Response claimRoute(String authToken, String gameID, Route route, TrainCard[] cardsUsedToClaimRoute) {
//        System.out.println(ServerModelFacade.getInstance().getUserName(authToken) + " is claiming " + route.toString());

        //Find the game using the game ID
        Game game = findGame(gameID);

        //Make sure the game was found
        if (game == null) {
            return new ErrorResponse("Game not found");
        }

        //Find the player object in the game
        Player player = game.findPlayer(getPlayersUserName(authToken));

        //Have the game action manager perform the player's action
        return GameActionManager.getInstance().claimRoute(game, player, authToken, route, cardsUsedToClaimRoute);
    }

    public Response drawTrainCard(String authToken, String gameID, boolean changeTurn) {
        //Find the game using the game ID
        Game game = findGame(gameID);

        //Make sure the game was found
        if (game == null) {
            return new ErrorResponse("Game not found");
        }

        //Find the player object in the game
        Player player = game.findPlayer(getPlayersUserName(authToken));

        //Have the game action manager perform the player's action
        return GameActionManager.getInstance().drawTrainCard(game, player, authToken, changeTurn);
    }

    public Response drawFaceUpCard(String authToken, String gameID, int cardIndex, boolean changeTurn) {
        //Find the game using the game ID
        Game game = findGame(gameID);

        //Make sure the game was found
        if (game == null) {
            return new ErrorResponse("Game not found");
        }

        //Find the player object in the game
        Player player = game.findPlayer(getPlayersUserName(authToken));

        //Have the game action manager perform the player's action
        return GameActionManager.getInstance().drawFaceUpCard(game, player, authToken, cardIndex, changeTurn);
    }

    /**
     * Draws a destination card three destination cards for the specified player (associated with the
     * authentication token) in the game associated with the gameID
     * @param authToken the authentication token associated with a player in the game
     * @param gameID the game identification string that will be used to find the specific game
     * @return an error response if something bad happened or a DrawDestinationCardResponse if with
     * the three cards they drew
     * @pre A valid authentication token must be given, a valid gameID must be given and the player must be associated with
     * that game
     * @post The player will have three new destination cards, the number of cards in the destination card deck will
     * be decremented by 3, all other players will be updated
     */
    public Response drawDestinationCard(String authToken, String gameID) {
        //Find the game using the game ID
        Game game = findGame(gameID);

        //Make sure the game was found
        if (game == null) {
            return new ErrorResponse("Game not found");
        }

        //Find the player object in the game
        Player player = game.findPlayer(getPlayersUserName(authToken));

        //Have the game action manager perform the player's action
        return GameActionManager.getInstance().drawDestinationCard(game, player, authToken);
    }

    /**
     * Discard a players unwanted destination cards
     * @param authToken the authentication token associated with a player in the game
     * @param gameID the game identification string that will be used to find the specific game
     * @param cards the array of destination cards to remove from the players hand
     * @return An error response if an error occurs or an empty response if everything went well
     * @pre A valid authentication token must be given, a valid gameID must be given and the player must be
     * associated with that game, the list of destination cards must be full of cards the player currently has
     * @post The list of destination cards will be removed from the players hand and added to the discarded
     * destination card pile, everyone will be notified of the discard
     */
    public Response discard(String authToken, String gameID, ArrayList<DestinationCard> cards) {
        //Find the game using the game ID
        Game game = findGame(gameID);

        //Make sure the game was found
        if (game == null) {
            return new ErrorResponse("Game not found");
        }

        //Find the player object in the game
        Player player = game.findPlayer(getPlayersUserName(authToken));

        //Have the game action manager perform the player's action
        return GameActionManager.getInstance().discard(game, player, authToken, cards);
    }

    /**
     * The initial discarding of destination cards for the specified player in the specified game during game setup
     * @param authToken the authentication token associated with a player in the game
     * @param gameID the game identification string that will be used to find the specific game
     * @param destinationCard the destination card to remove from the player's current hand
     * @return An error response if there was any problem with the discard or an empty response if everything went well
     * @pre A valid authentication token must be given, a valid gameID must be given and the player must be
     * associated with that game, the destination card must be in the player's current hand
     * @post The destination card will be removed from the player's hand and added to the discarded
     * destination card pile, the player's ready flag will be set, everyone will be notified of the discard
     */
    public Response initialDiscard(String authToken, String gameID, DestinationCard destinationCard) {
        //Find the game using the game ID
        Game game = findGame(gameID);

        //Make sure the game was found
        if (game == null) {
            return new ErrorResponse("Game not found");
        }

        //Find the player using their auth token
        Player player = game.findPlayer(getPlayersUserName(authToken));

        //Have the game action manager perform the player's action
        return GameActionManager.getInstance().initialDiscard(game, player, authToken, destinationCard);
    }

    /**
     * Send a chat message to all the other players in the specified game
     * @param authToken the authentication token associated with a player in the game
     * @param gameID the game identification string that will be used to find the specific game
     * @param message the message to be sent
     * @return An error response if something went wrong or an empty response if everything went well
     * @pre A valid authentication token must be given, a valid gameID must be given and the player must be
     * associated with that game, message is not null
     * @post Message is added to the games chat history, everyone else is notified of the message
     */
    public Response sendChatMessage(String authToken, String gameID, String message) {
        //Find the game using the game ID
        Game game = findGame(gameID);

        //Make sure the game was found
        if (game == null) {
            return new ErrorResponse("Game not found");
        }

        //Find the player's username using their authorization token
        String playerUserName = getPlayersUserName(authToken);

        //Have the game action manager perform the player's action
        return GameActionManager.getInstance().sendChatMessage(game, playerUserName, authToken, message);
    }

    /**
     * Adds a player specified by the authentication token to the game specified by the gameID
     * @param authToken the authentication token associated with a user
     * @param gameID the game identification string that will be used to find the unstarted game
     * @return An error response if the game was full or if there was any problems adding the user to
     * the game or a JoinGameResponse with the game info if everything worked out well
     * @pre A valid authentication token for a logged in user must be given, a valid gameID for an
     * unstarted game must be given
     * @post A player object associated with the user will be created and added to the list of players
     * in the specified game, the other players who have joined the game will be notified
     */
    public Response addPlayerToGame(String authToken, String gameID) {
        //Find the game info using the game ID
        GameInfo gameInfo = null;

        for (GameInfo currGame : unstartedGames) {
            if (currGame.getGameID().equals(gameID)) {
                gameInfo = currGame;
                break;
            }
        }

        //Find the actual game object and add the player object to it
        Game game = findGame(gameID);

        //Make a new player object using the player's authorization token
        Player player = new Player(getPlayersUserName(authToken), gameID);

        //If the player is already in the game, return an error response
        if (game.playerInGame(player.getUsername())) {
            return new ErrorResponse("You cannot join the same game twice");
        }

        //If the max number of players are already in the game and the player can't be added, return an error
        if (!gameInfo.addPlayerName(player.getUsername())) {
            return new ErrorResponse("Game is full");
        }

        game.addPlayer(player);

        if (game.getNumPlayers() == game.getMaxPlayers()) {
            game.startGame();
            unstartedGames.remove(gameInfo);
        }

        //Update the proxies
        startOrJoinUpdateProxies(game.getGameInfo(), game.isStarted());

        //Update the game in the database
        String serializedGame = SerializerDeserializer.serializeObject(game);
        String serializedGameProxy = SerializerDeserializer.serializeObject(GameProxyContainer.getInstance().getProxy(game.getGameID()));
        PersistenceHolder.getFactory().getGameDao().replace(game.getGameID(), serializedGame, serializedGameProxy);

        //Return the response
        return new JoinGameResponse(game.getGameInfo());
    }

    /**
     * Creates a new game with the specified name with the number of players hosted by the user
     * with the specified authentication token
     * @param authToken the authentication token associated with a user
     * @param gameName the name of the game to be created
     * @param maxPlayers the max number of players that can join the game
     * @return A create game response with the created game's information
     * @pre A valid authentication token must be given, gameName != null, maxPlayers >= 2 && maxPlayers <=5
     * @post A new game is created and a player object is created for the user and added to the game, the newly
     * created game is added to the list of unstarted games, everyone in the game browser is notified
     */
    public Response createGame(String authToken, String gameName, int maxPlayers) {
        //Generate a unique game ID for the new game
        String gameID = UUID.randomUUID().toString();

        //Get the player's username from the database and make a new player object
        Player hostPlayer = new Player(getPlayersUserName(authToken), gameID);

        //Create a new game and add it to the list of all games
        Game newGame = new Game(maxPlayers, gameName, gameID, hostPlayer);
        allGames.add(newGame);

        //Create a new game info object and add it to the list of unstarted games
        GameInfo gameInfo = newGame.getGameInfo();
        unstartedGames.add(gameInfo);

        //Update the proxies
        createGameUpdateProxies(gameID);

        //Add the game to the database;
        String serializedGame = SerializerDeserializer.serializeObject(newGame);
        String serializedGameProxy = SerializerDeserializer.serializeObject(GameProxyContainer.getInstance().getProxy(newGame.getGameID()));
        PersistenceHolder.getFactory().getGameDao().addGame(gameID, serializedGame, serializedGameProxy);

        //Return the result
        return new CreateGameResponse(gameInfo);
    }

    /**
     * Starts the specified game
     * @param gameID the gameID of the game to start
     * @return An error response if the game has already been started or if the game
     * does not exist
     * @pre gameId != null
     * @post The isStarted flag on the game is set and all players in the game are notified
     */
    public Response startGame(String gameID) {
        //Find the game from the list of games
        Game game = findGame(gameID);

        //If the game hasn't started, start it
        if (!game.isStarted()) {
            game.startGame();

            //Remove the game info from the list of unstarted games
            for (GameInfo currGame : unstartedGames) {
                if (currGame.getGameID().equals(gameID)) {
                    unstartedGames.remove(currGame);
                    break;
                }
            }

            //Update the proxies
            startOrJoinUpdateProxies(game.getGameInfo(), game.isStarted());

            //Update the game in the database
            String serializedGame = SerializerDeserializer.serializeObject(game);
            String serializedGameProxy = SerializerDeserializer.serializeObject(GameProxyContainer.getInstance().getProxy(game.getGameID()));
            PersistenceHolder.getFactory().getGameDao().replace(game.getGameID(), serializedGame, serializedGameProxy);

            //Return the result
            return new StartGameResponse(game);
        }

        //If the game has already started, return an error
        else {
            return new ErrorResponse("Game has already started");
        }
    }

    /**
     * Returns the initial game for setup purposes
     * @param authToken the authenticaiton token of the user asking for the initial game
     * @param gameID the gameID of the game to return
     * @return An error response if the game is not found or if the player associated with the
     * auth token is not a part of the game or a gameSetupResponse with the game object if everything
     * worked out fine
     */
    public Response getGameSetup(String authToken, String gameID) {
        Game game = findGame(gameID);

        if (game == null) {
            return new ErrorResponse("Game not found");
        }

        String username = getPlayersUserName(authToken);

        for (int i = 0; i < game.getPlayers().length; i++) {
            if (game.getPlayers()[i] != null && game.getPlayers()[i].getUsername().equals(username)) {
                return new GameSetupResponse(game);
            }
        }

        return new ErrorResponse("Player does not belong to that game");
    }

    /**
     * Creates a new game proxy for the given gameId
     * @param gameID the gameId for the game proxy to be created
     * @pre gameId != null
     * @post A new game proxy is created and added to the list of game proxies, the game
     * game browser is notified
     */
    private void createGameUpdateProxies(String gameID) {
        //Make a new game lobby/proxy and store it
        GameProxyContainer gameProxyContainer = GameProxyContainer.getInstance();
        gameProxyContainer.insertProxy(gameID, new GameProxy());

        //Update the game browser proxy
        updateGameBroswerProxy();
    }

    /**
     * Updates a game proxy
     * @param gameInfo the game info to update the game proxy with
     * @param isStarted the flag that shows whether a game is started
     * @pre GameInfo != null and isStarted != null
     * @post The proxy is updated with the player information in the game info and
     * the isStarted flag
     */
    private void startOrJoinUpdateProxies(GameInfo gameInfo, boolean isStarted) {
        //Get the game lobby/proxy for the specific game the player is joining
        GameProxyContainer gameProxyContainer = GameProxyContainer.getInstance();
        GameProxy gameProxy = gameProxyContainer.getProxy(gameInfo.getGameID());

        //Update the game lobby/proxy
        gameProxy.updateLobby(gameInfo.getPlayerNames(), isStarted);

        //Update the game browser proxy
        updateGameBroswerProxy();
    }

    /**
     * Converts the game info list to an array and updates the game browser
     * @pre
     * @post Updates the game browser with the list of unstarted games
     */
    private void updateGameBroswerProxy() {
        GameBrowserProxy gameBrowserProxy = GameBrowserProxy.getInstance();

        //Convert the game info list to an array (for gson purposes)
        GameInfo[] infos = new GameInfo[unstartedGames.size()];
        GameInfo[] infos1 = unstartedGames.toArray(infos);

        //Update
        gameBrowserProxy.updateGameBrowser(infos1);
    }

    public ArrayList<GameInfo> getUnstartedGames() {
        return unstartedGames;
    }

    public ArrayList<Game> getAllGames() {
        return allGames;
    }

    //Helper function for finding a player's username using their authorization

    /**
     * A helper function for finding a player's username using their authorization token
     * @param authToken the authorization token to find a player's username
     * @return the player's username
     * @pre a valid authentication token should be given, and there must be a player object
     * associated with the authentication token
     * @post
     */
    private String getPlayersUserName(String authToken) {
        String playerUserName = PersistenceHolder.getFactory().getAuthTokenDao().getUsername(authToken);

        return playerUserName;
    }

    /**
     * A helper function for finding the game from the gameID
     * @param gameID The gameID for the desired game
     * @return the game object associated with the gameID
     */
    private Game findGame(String gameID) {
        for (Game currGame : allGames) {
            if (currGame.getGameID().equals(gameID)) {
                return currGame;
            }
        }

        return null;
    }
}
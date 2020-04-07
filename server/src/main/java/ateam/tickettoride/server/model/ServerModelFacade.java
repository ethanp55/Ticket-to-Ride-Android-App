package ateam.tickettoride.server.model;

import java.util.ArrayList;

import ateam.tickettoride.common.Command;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.GameInfo;
import ateam.tickettoride.common.model.User;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.ClientProxy.GameProxy;
import ateam.tickettoride.server.ClientProxy.GameProxyContainer;
import ateam.tickettoride.server.persistence.PersistenceHolder;
import ateam.tickettoride.server.persistence.SerializerDeserializer;


public class ServerModelFacade {
    private static final ServerModelFacade ourInstance = new ServerModelFacade();

    public static ServerModelFacade getInstance() {
        return ourInstance;
    }

    private ServerModelFacade() {
    }

    public Response addPlayerToGame(String authToken, String gameID) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.addPlayerToGame(authToken, gameID);
    }

    public Response createGame(String authToken, String gameName, int maxPlayers) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.createGame(authToken, gameName, maxPlayers);
    }

    public Response startGame(String gameID) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.startGame(gameID);
    }

    public Response getGameSetup(String authToken, String gameID) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.getGameSetup(authToken, gameID);
    }

    public boolean addUser(User user) {
        return PersistenceHolder.getFactory().getUserDao().addUser(user.getUsername(), user.getPassword());
    }

    public void addAuthToken(String token, String userName) {
        PersistenceHolder.getFactory().getAuthTokenDao().addToken(userName, token);
    }

    public String getUserName(String token) {
        return PersistenceHolder.getFactory().getAuthTokenDao().getUsername(token);
    }

    public boolean checkPassword(String username, String password) {
        return PersistenceHolder.getFactory().getUserDao().checkPassword(username, password);
    }

    public boolean checkUsername(String username) {
        return PersistenceHolder.getFactory().getUserDao().checkUsername(username);
    }

    public ArrayList<GameInfo> getUnstartedGames() {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.getUnstartedGames();
    }

    public ArrayList<Game> getAllGames() {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.getAllGames();
    }

    public Response sendChatMessage(String authToken, String gameID, String message) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.sendChatMessage(authToken, gameID, message);
    }

    public Response initialDiscard(String authToken, String gameID, DestinationCard destinationCard) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.initialDiscard(authToken, gameID, destinationCard);
    }

    public Response discard(String authToken, String gameID, ArrayList<DestinationCard> cards) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.discard(authToken, gameID, cards);
    }

    public Response drawDestinationCard(String authToken, String gameID) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.drawDestinationCard(authToken, gameID);
    }

    public Response drawFaceUpCard(String authToken, String gameID, int cardIndex, boolean changeTurn) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.drawFaceUpCard(authToken, gameID, cardIndex, changeTurn);
    }

    public Response drawTrainCard(String authToken, String gameID, boolean changeTurn) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.drawTrainCard(authToken, gameID, changeTurn);
    }

    public Response claimRoute(String authToken, String gameID, Route route, TrainCard[] cardsUsedToClaimRoute) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.claimRoute(authToken, gameID, route, cardsUsedToClaimRoute);
    }

    public Response completedDestinationCard(String authToken, String gameID, DestinationCard destinationCard) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.completedDestinationCard(authToken, gameID, destinationCard);
    }

    public Response setLongestPath(String authToken, String gameID, int longestPath) {
        GameContainer gameContainer = GameContainer.getInstance();

        return gameContainer.setLongestPath(authToken, gameID, longestPath);
    }

    public void syncGameData() {
        String[][] gameData = PersistenceHolder.getFactory().getGameDao().getGames();

        if(gameData != null) {
            for (int i = 0; i < gameData.length; i++) {
                //Get the game and game proxy from the database
                Game game = (Game) SerializerDeserializer.deserializeObject(gameData[i][0]);
                GameProxy gameProxy = (GameProxy) SerializerDeserializer.deserializeObject(gameData[i][1]);

                //Store the game and game proxy in memory
                GameContainer.getInstance().getAllGames().add(game);

                if (!game.isStarted()) {
                    GameContainer.getInstance().getUnstartedGames().add(game.getGameInfo());
                }

                GameProxyContainer.getInstance().insertProxy(game.getGameID(), gameProxy);

                //Get the commands for the game from the database
                String serializedCommands = PersistenceHolder.getFactory().getCommandDao().getCommands(game.getGameID());

                if (serializedCommands != null) {
                    ArrayList<Command> commands = (ArrayList<Command>) SerializerDeserializer.deserializeObject(serializedCommands);

                    //Execute all of the commands
                    for (Command command : commands) {
                        command.execute();
                    }
                }
            }
        }
    }
}

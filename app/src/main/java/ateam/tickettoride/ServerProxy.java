package ateam.tickettoride;


import ateam.tickettoride.common.Command;
import ateam.tickettoride.common.IServer;
import ateam.tickettoride.common.requests.ChatRequest;
import ateam.tickettoride.common.requests.ClaimRouteRequest;
import ateam.tickettoride.common.requests.CompletedDestinationCardRequest;
import ateam.tickettoride.common.requests.CreateGameRequest;
import ateam.tickettoride.common.requests.DiscardRequest;
import ateam.tickettoride.common.requests.DrawDestinationCardRequest;
import ateam.tickettoride.common.requests.DrawFaceUpCardRequest;
import ateam.tickettoride.common.requests.DrawTrainCardRequest;
import ateam.tickettoride.common.requests.GameSetupRequest;
import ateam.tickettoride.common.requests.InitialDiscardRequest;
import ateam.tickettoride.common.requests.JoinGameRequest;
import ateam.tickettoride.common.requests.LoginRequest;
import ateam.tickettoride.common.requests.LongestPathRequest;
import ateam.tickettoride.common.requests.RegisterRequest;
import ateam.tickettoride.common.requests.StartGameRequest;
import ateam.tickettoride.common.requests.UpdateGameBrowserRequest;
import ateam.tickettoride.common.requests.UpdateGameRequest;
import ateam.tickettoride.common.responses.CreateGameResponse;
import ateam.tickettoride.common.responses.DrawDestinationCardResponse;
import ateam.tickettoride.common.responses.DrawTrainCardResponse;
import ateam.tickettoride.common.responses.EmptyResponse;
import ateam.tickettoride.common.responses.GameSetupResponse;
import ateam.tickettoride.common.responses.JoinGameResponse;
import ateam.tickettoride.common.responses.LoginResponse;
import ateam.tickettoride.common.responses.RegisterResponse;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.common.responses.StartGameResponse;
import ateam.tickettoride.common.responses.UpdateGameBrowserResponse;
import ateam.tickettoride.common.responses.UpdateGameResponse;

/**
 * Class for the client to attempt to call methods on the server.
 */
public class ServerProxy implements IServer {
    private static ServerProxy singleServerProxy;
    //fully qualified class name of the ServerFacade
    private static String SERVERNAME = "ateam.tickettoride.server.service.ServerFacade";
    private ClientCommunicator comm;

    private ServerProxy(){
        comm = ClientCommunicator.getInstance();
    }

    /**
     * Get the singleton ServerProxy.
     * @return  The singleton ServerProxy.
     */
    public static ServerProxy getInstance(){
        if(singleServerProxy == null){
            singleServerProxy = new ServerProxy();
        }
        return singleServerProxy;
    }

    /**
     * Attempts to login to the server.
     * @param request   The request for the login.
     * @return  A response based on whatever happens in the server, a LoginResponse if successful.
     */
    public Response login(LoginRequest request){
        return comm.connect(makeCommand("login", request), LoginResponse.class);
    }

    /**
     * Attempts to register a user on the server.
     * @param request   The request to use to register.
     * @return  A response from the server or the communicator, a RegisterResponse if successful.
     */
    public Response register(RegisterRequest request){
        return comm.connect(makeCommand("register", request), RegisterResponse.class);
    }

    /**
     * Attempts to create a game on the server.
     * @param request   A request to create the game.
     * @return  A response from the communicator or server, a CreateGameResponse if successful.
     */
    @Override
    public Response createGame(CreateGameRequest request) {
        return comm.connect(makeCommand("createGame", request), CreateGameResponse.class);
    }

    /**
     * Attempts to join a game on the server.
     * @param request   The request to join the game.
     * @return  A response from the communicator or server, a JoinGameResponse if successful.
     */
    @Override
    public Response joinGame(JoinGameRequest request) {
        return comm.connect(makeCommand("joinGame", request), JoinGameResponse.class);
    }

    /**
     * Attempts to retrieve updates for the game browser from the server.
     * @param request   A request for the updates.
     * @return  A response from the communicator or server, an UpdateGameBrowserResponse if successful.
     */
    @Override
    public Response updateGameBrowser(UpdateGameBrowserRequest request) {
        return comm.connect(makeCommand("updateGameBrowser", request), UpdateGameBrowserResponse.class);
    }

    /**
     * Attempts to retrieve updates for the current game from the server.
     * @param request   A request for the updates.
     * @return  A response from the communicator or server, an UpdateGameResponse if successful.
     */
    @Override
    public Response updateGame(UpdateGameRequest request) {
        return comm.connect(makeCommand("updateGame", request), UpdateGameResponse.class);
    }

    /**
     * Attempts to start the game the user is in.
     * @param request   A request to start the game.
     * @return  A response from the communicator or server, a StartGameResponse if successful.
     */
    @Override
    public Response startGame(StartGameRequest request) {
        return comm.connect(makeCommand("startGame", request), StartGameResponse.class);
    }

    @Override
    public Response chat(ChatRequest request) {
        return comm.connect(makeCommand("chat", request), EmptyResponse.class);
    }

    @Override
    public Response gameSetup(GameSetupRequest request) {
        return comm.connect(makeCommand("gameSetup", request), GameSetupResponse.class);
    }

    @Override
    public Response initialDiscard(InitialDiscardRequest request) {
        return comm.connect(makeCommand("initialDiscard", request), EmptyResponse.class);
    }

    @Override
    public Response discard(DiscardRequest request) {
        return comm.connect(makeCommand("discard", request), EmptyResponse.class);
    }

    @Override
    public Response drawDestinationCard(DrawDestinationCardRequest request) {
        return comm.connect(makeCommand("drawDestinationCard", request), DrawDestinationCardResponse.class);
    }

    @Override
    public Response drawFaceUpCard(DrawFaceUpCardRequest request) {
        return comm.connect(makeCommand("drawFaceUpCard", request), EmptyResponse.class);
    }

    @Override
    public Response drawTrainCard(DrawTrainCardRequest request) {
        return comm.connect(makeCommand("drawTrainCard", request), DrawTrainCardResponse.class);
    }

    @Override
    public Response claimRoute(ClaimRouteRequest request) {
        return comm.connect(makeCommand("claimRoute", request), EmptyResponse.class);
    }

    @Override
    public Response completedDestinationCard(CompletedDestinationCardRequest request) {
        return comm.connect(makeCommand("completedDestinationCard", request), EmptyResponse.class);
    }

    @Override
    public Response setLongestPath(LongestPathRequest request) {
        return comm.connect(makeCommand("setLongestPath", request), EmptyResponse.class);
    }

    /**
     * Makes commands for the ServerProxy to send to the Server via the ClientCommunicator.
     * @param method    The name of the method to be called on the ServerFacade.
     * @param request   The request to be send to the server.
     * @return  A Command object representing the command to be executed on the server.
     */
    private Command makeCommand(String method, Object request){
        Object[] objs = new Object[1];
        objs[0] = request;
        String[] strings = new String[1];
        strings[0] = request.getClass().getName();
        return new Command(SERVERNAME, method, strings, objs);
    }
}

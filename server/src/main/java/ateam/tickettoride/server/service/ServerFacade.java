package ateam.tickettoride.server.service;

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
import ateam.tickettoride.common.requests.LongestPathRequest;
import ateam.tickettoride.common.requests.UpdateGameBrowserRequest;
import ateam.tickettoride.common.requests.JoinGameRequest;
import ateam.tickettoride.common.requests.LoginRequest;
import ateam.tickettoride.common.requests.RegisterRequest;
import ateam.tickettoride.common.requests.StartGameRequest;
import ateam.tickettoride.common.requests.UpdateGameRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;


public class ServerFacade implements IServer {
    private static final ServerFacade ourInstance = new ServerFacade();

    public static ServerFacade getInstance() {
        return ourInstance;
    }

    private ServerFacade() {
    }

    @Override
    public Response login(LoginRequest request) {
        LoginService service = new LoginService();

        return service.performService(request);
    }

    @Override
    public Response register(RegisterRequest request) {
        RegisterService service = new RegisterService();

        return service.performService(request);
    }

    @Override
    public Response createGame(CreateGameRequest request) {
        CreateGameService service = new CreateGameService();

        return service.performService(request);
    }

    @Override
    public Response joinGame(JoinGameRequest request) {
        JoinGameService service = new JoinGameService();

        return service.performService(request);
    }

    @Override
    public Response startGame(StartGameRequest request) {
        StartGameService service = new StartGameService();

        return service.performService(request);
    }

    @Override
    public Response chat(ChatRequest request) {
        ChatService service = new ChatService();

        return service.performService(request);
    }

    @Override
    public Response gameSetup(GameSetupRequest request) {
        GameSetupService service = new GameSetupService();

        return service.performService(request);
    }

    @Override
    public Response initialDiscard(InitialDiscardRequest request) {
        InitialDiscardService service = new InitialDiscardService();

        return service.performService(request);
    }

    @Override
    public Response discard(DiscardRequest request) {
        DiscardService service = new DiscardService();

        return service.performServide(request);
    }

    @Override
    public Response drawDestinationCard(DrawDestinationCardRequest request) {
        DrawDestinationCardService service = new DrawDestinationCardService();

        return service.performService(request);
    }

    @Override
    public Response drawFaceUpCard(DrawFaceUpCardRequest request) {
        DrawFaceUpCardService service = new DrawFaceUpCardService();

        return service.performService(request);
    }

    @Override
    public Response drawTrainCard(DrawTrainCardRequest request) {
        DrawTrainCardService service = new DrawTrainCardService();

        return service.performService(request);
    }

    @Override
    public Response claimRoute(ClaimRouteRequest request) {
        ClaimRouteService service = new ClaimRouteService();

        return service.performService(request);
    }

    @Override
    public Response completedDestinationCard(CompletedDestinationCardRequest request) {
        CompletedDestinationCardService service = new CompletedDestinationCardService();

        return service.performService(request);
    }

    @Override
    public Response setLongestPath(LongestPathRequest request) {
        LongestPathService service = new LongestPathService();

        return service.performService(request);
    }

    @Override
    public Response updateGameBrowser(UpdateGameBrowserRequest request) {
        UpdateGameBrowserService service = new UpdateGameBrowserService();

        return service.performService(request);
    }

    @Override
    public Response updateGame(UpdateGameRequest request) {
        UpdateGameService service = new UpdateGameService();

        return service.performService(request);
    }

    public void syncGameData() {
        ServerModelFacade.getInstance().syncGameData();
    }
}

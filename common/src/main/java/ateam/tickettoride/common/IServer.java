package ateam.tickettoride.common;


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

/**
 * Interface for the ServerFacade and the ServerProxy.
 */

public interface IServer {
    public Response login(LoginRequest request);

    public Response register(RegisterRequest request);

    public Response createGame(CreateGameRequest request);

    public Response joinGame(JoinGameRequest request);

    public Response updateGameBrowser(UpdateGameBrowserRequest request);

    public Response updateGame(UpdateGameRequest request);

    public Response startGame(StartGameRequest request);

    public Response chat(ChatRequest request);

    public Response gameSetup(GameSetupRequest request);

    public Response initialDiscard(InitialDiscardRequest request);

    public Response discard(DiscardRequest request);

    public Response drawDestinationCard(DrawDestinationCardRequest request);

    public Response drawFaceUpCard(DrawFaceUpCardRequest request);

    public Response drawTrainCard(DrawTrainCardRequest request);

    public Response claimRoute(ClaimRouteRequest request);

    public Response completedDestinationCard(CompletedDestinationCardRequest request);

    public Response setLongestPath(LongestPathRequest request);
}

package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.ClaimRouteRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class ClaimRouteService {
    public Response performService(ClaimRouteRequest request) {
        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        return serverModelFacade.claimRoute(request.getAuthToken(), request.getGameID(), request.getRoute(), request.getCardsUsedToClaimRoute());
    }
}

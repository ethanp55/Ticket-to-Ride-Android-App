package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.CompletedDestinationCardRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class CompletedDestinationCardService {
    public Response performService(CompletedDestinationCardRequest request) {
        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        return serverModelFacade.completedDestinationCard(request.getAuthToken(), request.getGameID(), request.getDestinationCard());
    }
}

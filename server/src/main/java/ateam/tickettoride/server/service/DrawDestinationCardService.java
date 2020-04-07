package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.DrawDestinationCardRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class DrawDestinationCardService {
    public Response performService(DrawDestinationCardRequest request) {
        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        return serverModelFacade.drawDestinationCard(request.getAuthToken(), request.getGameID());
    }
}

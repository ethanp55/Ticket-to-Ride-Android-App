package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.InitialDiscardRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class InitialDiscardService {
    public Response performService(InitialDiscardRequest request) {
        System.out.println("Initial discard...");

        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        return serverModelFacade.initialDiscard(request.getAuthToken(), request.getGameID(), request.getDestinationCard());
    }
}

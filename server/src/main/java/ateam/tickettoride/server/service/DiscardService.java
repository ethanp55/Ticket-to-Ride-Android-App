package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.DiscardRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class DiscardService {
    public Response performServide(DiscardRequest request) {
        System.out.println("Discarding...");

        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        return serverModelFacade.discard(request.getAuthToken(), request.getGameID(), request.getCardsToDiscard());
    }
}

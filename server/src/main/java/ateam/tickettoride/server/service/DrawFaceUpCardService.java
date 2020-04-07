package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.DrawFaceUpCardRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class DrawFaceUpCardService {
    public Response performService(DrawFaceUpCardRequest request) {
        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        return serverModelFacade.drawFaceUpCard(request.getAuthToken(), request.getGameID(), request.getCardIndex(), request.isChangeTurn());
    }
}

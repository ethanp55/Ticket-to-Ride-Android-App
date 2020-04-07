package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.DrawTrainCardRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class DrawTrainCardService {
    public Response performService(DrawTrainCardRequest request) {
        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        return serverModelFacade.drawTrainCard(request.getAuthToken(), request.getGameID(), request.isChangeTurn());
    }
}

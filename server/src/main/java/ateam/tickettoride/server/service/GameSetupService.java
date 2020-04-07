package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.GameSetupRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class GameSetupService {
    public Response performService(GameSetupRequest request) {
        System.out.println("Getting game setup...");

        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        return serverModelFacade.getGameSetup(request.getAuthToken(), request.getGameID());
    }
}

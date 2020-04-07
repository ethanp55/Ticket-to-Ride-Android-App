package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.LongestPathRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class LongestPathService {
    public Response performService(LongestPathRequest request) {
        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        return serverModelFacade.setLongestPath(request.getAuthToken(), request.getGameID(), request.getLongestPath());
    }
}

package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.CreateGameRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;


public class CreateGameService {
    Response performService(CreateGameRequest request) {
        System.out.println("Creating game...");

        ServerModelFacade modelFacade = ServerModelFacade.getInstance();

        String gameName = request.getName();
        int maxPlayers = request.getMaxPlayers();
        String authToken = request.getAuthToken();

        return modelFacade.createGame(authToken, gameName, maxPlayers);
    }
}

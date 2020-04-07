package ateam.tickettoride.server.service;;


import ateam.tickettoride.common.requests.StartGameRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class StartGameService {
    Response performService(StartGameRequest request) {
        System.out.println("Starting game...");

        ServerModelFacade modelFacade = ServerModelFacade.getInstance();

        String gameID = request.getGameID();
        String authToken = request.getAuthToken();

        return modelFacade.startGame(gameID);
    }
}

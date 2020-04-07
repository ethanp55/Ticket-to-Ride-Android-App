package ateam.tickettoride.server.service;;


import ateam.tickettoride.common.requests.JoinGameRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class JoinGameService {
    Response performService(JoinGameRequest request) {
        System.out.println("Joining game...");

        ServerModelFacade modelFacade = ServerModelFacade.getInstance();

        String gameID = request.getGameID();
        String gameName = request.getGameName();
        String authToken = request.getAuthToken();

        return modelFacade.addPlayerToGame(authToken, gameID);
    }
}

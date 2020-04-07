package ateam.tickettoride.server.service;

import java.util.ArrayList;
import java.util.UUID;

import ateam.tickettoride.common.model.GameInfo;
import ateam.tickettoride.common.model.User;
import ateam.tickettoride.common.requests.RegisterRequest;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.RegisterResponse;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;


public class RegisterService {
    //Performs register service
    Response performService(RegisterRequest request) {
        System.out.println("Registering...");

        String username = request.getUsername();
        String password = request.getPassword();
        User newUser = new User(username, password);
        ServerModelFacade modelFacade = ServerModelFacade.getInstance();

        //Add successful
        //Adds AuthToken to container
        if(modelFacade.addUser(newUser)) {
            String authToken = UUID.randomUUID().toString();
            ArrayList<GameInfo> initialList = GetInitialGameList.getGameList();
            RegisterResponse result = new RegisterResponse(authToken, username, initialList);
            modelFacade.addAuthToken(authToken, username);
            return result;
        }
        //Add failed (name already taken)
        else {
            ErrorResponse result = new ErrorResponse("username already in use");
            return result;
        }
    }
}

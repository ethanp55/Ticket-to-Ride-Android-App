package ateam.tickettoride.server.service;

import java.util.ArrayList;
import java.util.UUID;

import ateam.tickettoride.common.model.GameInfo;
import ateam.tickettoride.common.requests.LoginRequest;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.LoginResponse;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;




public class LoginService {
    Response performService(LoginRequest request) {
        System.out.println("Logging in...");

        String username = request.getUsername();
        String password = request.getPassword();
        ServerModelFacade modelFacade = ServerModelFacade.getInstance();

        //Check if username exists
        if(modelFacade.checkUsername(username)) {
            //Check if password matches
            if (modelFacade.checkPassword(username, password)) {
                String authToken = UUID.randomUUID().toString();
                modelFacade.addAuthToken(authToken, username);
                ArrayList<GameInfo> initialList = GetInitialGameList.getGameList();
                LoginResponse result = new LoginResponse(authToken, username, initialList);

                return result;
            }
            //Password does not match
            else {
                System.out.println("Incorrect Password");
                ErrorResponse result = new ErrorResponse("Incorrect password");
                return result;
            }
        }
        //Username does not exist
        else {
            ErrorResponse result = new ErrorResponse("Username does not exist");
            return result;
        }
    }
}

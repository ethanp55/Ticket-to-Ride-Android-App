package ateam.tickettoride.server.service;

import java.util.ArrayList;

import ateam.tickettoride.common.Command;
import ateam.tickettoride.common.requests.UpdateGameBrowserRequest;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.common.responses.UpdateGameBrowserResponse;
import ateam.tickettoride.server.ClientProxy.GameBrowserProxy;
import ateam.tickettoride.server.model.ServerModelFacade;


public class UpdateGameBrowserService {
    //Creates a copy of the master command list from index getNextCommandNumber until the end.
    //Returns the subset of commands and the index of the last command from the master.
    public Response performService(UpdateGameBrowserRequest request) {
        if (!checkToken(request.getAuthToken())) {
            return new ErrorResponse("Invalid auth token");
        }

        GameBrowserProxy gameBrowserProxy = GameBrowserProxy.getInstance();

        ArrayList<Command> commands = gameBrowserProxy.getBrowserCommands();

        ArrayList<Command> commandsToExecute = new ArrayList<>();

        for (int i = request.getNextCommandNumber(); i < commands.size(); i++) {
            commandsToExecute.add(commands.get(i));
        }

        //System.out.println("Returning commands to execute...");

        return new UpdateGameBrowserResponse(commandsToExecute, commands.size());
    }

    private boolean checkToken(String authToken) {
        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        if (serverModelFacade.getUserName(authToken) == null) {
            return false;
        }

        else {
            return true;
        }
    }
}

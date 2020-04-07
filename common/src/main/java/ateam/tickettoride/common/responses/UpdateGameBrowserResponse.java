package ateam.tickettoride.common.responses;

import java.util.ArrayList;
import java.util.List;



import ateam.tickettoride.common.Command;
import ateam.tickettoride.common.responses.Response;

/**
 * Response for a successful request for an update to the game broswer list.
 */

public class UpdateGameBrowserResponse extends Response {
    //Commands to execute on the client side to update the game browser.
    private List<Command> command;
    //The number of the next command after the ones that have been executed.
    private int nextCommandNumber;

    public UpdateGameBrowserResponse(List<Command> cmd, int next){
        command = cmd;
        nextCommandNumber = next;
    }

    public List<Command> getCommand(){
        return command;
    }

    public int getNextCommandNumber(){
        return nextCommandNumber;
    }
}

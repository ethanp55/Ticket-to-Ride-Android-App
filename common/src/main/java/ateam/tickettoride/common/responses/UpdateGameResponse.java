package ateam.tickettoride.common.responses;

import java.util.List;

import ateam.tickettoride.common.Command;

/**
 * Response for a successful request to update the game.
 */
public class UpdateGameResponse extends Response {
    // A list of commands to execute on the client to update the game.
    private List<Command> command;
    //The number of the next command that will be in line.
    private int nextCommandNumber;

    public UpdateGameResponse(List<Command> command, int nextCommandNumber) {
        this.command = command;
        this.nextCommandNumber = nextCommandNumber;
    }

    public List<Command> getCommand() {
        return command;
    }

    public int getNextCommandNumber() {
        return nextCommandNumber;
    }
}

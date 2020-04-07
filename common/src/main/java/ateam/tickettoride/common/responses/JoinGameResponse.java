package ateam.tickettoride.common.responses;


import ateam.tickettoride.common.model.GameInfo;

/**
 * Class for a response to a successful request to join a game.
 */

public class JoinGameResponse extends Response {
    //The object representing the information for the game.
    private GameInfo gameInfo;

    public JoinGameResponse(GameInfo info){
        gameInfo = info;
    }

    public GameInfo getGameInfo(){
        return gameInfo;
    }
}

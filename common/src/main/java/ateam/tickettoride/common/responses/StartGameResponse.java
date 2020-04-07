package ateam.tickettoride.common.responses;


import ateam.tickettoride.common.model.Game;

/**
 * Response for a successful request to start a game.
 */

public class StartGameResponse extends Response {
    //The game that has just been started.
    private Game theGame;

    public StartGameResponse(Game game){
        theGame = game;
    }

    public Game getGame(){
        return theGame;
    }
}

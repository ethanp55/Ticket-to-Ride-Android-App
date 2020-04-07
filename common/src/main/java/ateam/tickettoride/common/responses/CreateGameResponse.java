package ateam.tickettoride.common.responses;


import ateam.tickettoride.common.model.GameInfo;

/**
 * Class representing a successful response to a request to create a game.
 */

public class CreateGameResponse extends Response {
    //The object representing the game just created.
    private GameInfo gameInfo;

    public CreateGameResponse(GameInfo info){
        gameInfo = info;
    }

    public GameInfo getGameInfo(){
        return gameInfo;
    }
}

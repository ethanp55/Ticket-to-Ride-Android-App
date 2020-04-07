package ateam.tickettoride.common.responses;

import ateam.tickettoride.common.model.Game;

public class GameSetupResponse extends Response {
    //Game that is returned from game setup
    private Game game;

    public GameSetupResponse(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

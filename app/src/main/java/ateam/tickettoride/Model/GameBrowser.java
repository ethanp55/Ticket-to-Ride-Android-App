package ateam.tickettoride.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.common.model.GameInfo;

/**
 * A GameBrowser object that holds the GameInfo objects to be displayed in the GameBrowser
 */
public class GameBrowser{
    private static GameBrowser instance = null;
    private List<GameInfo> games;

    public static GameBrowser getInstance(){
        if(instance == null){
            instance = new GameBrowser();
        }
        return instance;
    }


    public GameBrowser(){
        games = new ArrayList<>();
    }

    public List<GameInfo> getGames() {
        return games;
    }

    public void setGames(List<GameInfo> games){
        this.games = games;
    }


}

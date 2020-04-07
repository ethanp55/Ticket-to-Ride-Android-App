package ateam.tickettoride.server.service;;

import java.util.ArrayList;

import ateam.tickettoride.common.model.GameInfo;
import ateam.tickettoride.server.model.ServerModelFacade;


public class GetInitialGameList {
    public static ArrayList<GameInfo> getGameList() {
        ServerModelFacade modelFacade = ServerModelFacade.getInstance();
        return modelFacade.getUnstartedGames();
    }
}

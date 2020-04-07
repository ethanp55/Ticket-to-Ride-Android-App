package ateam.tickettoride.Logging;

import android.util.Log;

import java.util.ArrayList;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.Player;

public class GameLogging {
    public static String getCurrentGameState(){
        Game game = ModelFacade.getInstance().getRunningGame();
        int currPlayer = game.getCurrentPlayer();
        Player[] players = game.getPlayers();
        StringBuilder sb = new StringBuilder();

        // PlayerInformation Information
        sb.append( "PLAYER INFORMATION: \n" );
        for(int i = 0; i < players.length; i++){
            if(players[i] != null){
                sb.append("\t" + players[i].getUsername() + " is color " + players[i].getPlayerColor() + " \n");
            }
        }

        // Destination Card Information
        sb.append( "DESTCARD INFORMATION: \n" );
        for(int i = 0; i < players.length; i++){
            if(players[i] != null){
                sb.append("\t" + players[i].getUsername() + " has: \n");
                ArrayList<DestinationCard> destinationCards = new ArrayList<>(players[i].getDestinationCards());
                for(int j = 0; j < destinationCards.size(); j++){
                    sb.append("\t \t" + destinationCards.get(j).toStringDest() + "\n");
                }
            }
        }

        // Train Card Information, possibly add color and types???
        sb.append( "TRAIN CARD INFORMATION: \n" );
        for(int i = 0; i < players.length; i++){
            if(players[i] != null){
                sb.append("\t" + players[i].getUsername() + " has " + players[i].getTrainCards().size() + " train cards. \n");
            }

        }

        sb.append( "ROUTE INFORMATION: \n" );
        for(int i = 0; i < players.length; i++){
            if(players[i] != null){
                sb.append("\t" + players[i].getUsername() + " has " + players[i].getTrainCards().size() + " . \n");
            }

        }

        sb.append( "POINT INFORMATION: \n" );
        for(int i = 0; i < players.length; i++){
            if(players[i] != null){
                sb.append("\t" + players[i].getUsername() + " has " + players[i].getPoints() + " point(s). \n");
            }
        }

        sb.append("CURRENT PLAYER TURN: \n" +  "\t " + currPlayer);

        return sb.toString();
    }
}

package ateam.tickettoride;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Looper;
import android.widget.Toast;

import java.util.ArrayList;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Model.RunningGameContainer;
import ateam.tickettoride.Model.UserInfoContainer;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.City;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Player;


/**
 * Class for running tests to change the model to see if the presenters update the views
 * properly.
 */
public class MagicButton implements Runnable {
    private Thread magicThread;
    private Activity activity;

    public MagicButton(Activity activity){
        magicThread = new Thread(this);
        this.activity = activity;
    }

    public void start(){
        magicThread.start();
    }

    public void run(){
        //allows Toasts
        Looper.prepare();

        updatePlayerPoints();
        pause();
        changeTrainCardsForPlayer();
        pause();
        changePlayerDestinationCards();
        pause();
        updateOpponentTrainCards();
        pause();
        updateOpponentTrainCars();
        pause();
        updateOpponentDestinationCards();
        pause();
        updateFaceUpCards();
        pause();
        updateTrainCardDeckNum();
        pause();
        updateDestinationCardDeckNum();
        pause();
        addClaimedRoute();
        pause();
        incrementTurn();
    }

    private void pause(){
        try{
            //sleep 7 seconds in between
            Thread.sleep(6000);
        }catch(InterruptedException ie){

        }
    }

    /**
     * Updates player points.
     */
    private void updatePlayerPoints(){
        Player[] players = RunningGameContainer.getInstance().getGame().getPlayers();
        sendToast("Updating player points to random values.");
        pause();
        for( int i = 0; i< players.length; i++){
            String name = players[i].getUsername();
            int numPoints = (int)(Math.random()*1000);
            ClientFacade.getInstance().updatePlayerPoints(name, numPoints);
        }
    }

    /**
     * Add/remove train cards for this player.
     */
    private void changeTrainCardsForPlayer(){
        sendToast("Updating this player's train car cards.");
        pause();
        String name = ModelFacade.getInstance().getUsername();
        TrainCard[] cards = ModelFacade.getInstance().getRunningGame().getFaceUpCards().getTrainCards();
        ClientFacade.getInstance().updatePlayerTrainCards(name, cards);
    }

    /**
     * Add/remove player destination cards for this player.
     */
    private void changePlayerDestinationCards(){
        sendToast("Updating this player's destination cards.");
        pause();
        String name = ModelFacade.getInstance().getUsername();
        DestinationCard[] cards = new DestinationCard[3];
        cards[0] = new DestinationCard("Provo", "Orem", 2);
        cards[1] = ModelFacade.getInstance().getPlayer(name).getDestinationCards().get(0);
        cards[2] = new DestinationCard("The Moon", "Houston", 9001);
        ClientFacade.getInstance().updatePlayerDestinationCards(name, cards);
    }

    /**
     * Updates the number of train cards for opponent players.
     */
    private void updateOpponentTrainCards(){
        sendToast("Updating enemy train cards now.");
        pause();
        Player[] players = ModelFacade.getInstance().getRunningGame().getPlayers();
        String name = ModelFacade.getInstance().getUsername();
        for( int i = 0; i<players.length; i++){
            TrainCard[] cards = ModelFacade.getInstance().getRunningGame().getFaceUpCards().getTrainCards();
            if(!players[i].getUsername().equals(name)){
                ClientFacade.getInstance().updatePlayerTrainCards(players[i].getUsername(), cards);
            }
        }
    }

    /**
     * Updates the number of train cars for opponent players.
     */
    private void updateOpponentTrainCars(){
        sendToast("Updating enemy train cars.");
        pause();
        Player[] players = ModelFacade.getInstance().getRunningGame().getPlayers();
        for(int i = 0; i<players.length; i++){
            String name = players[i].getUsername();
            if(!name.equals(ModelFacade.getInstance().getUsername())){
                int trainCars = (int)(Math.random()*9000);
                ClientFacade.getInstance().updatePlayerTrainPieces(name, trainCars);
            }
        }
    }

    /**
     * Updates the number of destination cards for opponent players.
     */
    private void updateOpponentDestinationCards(){
        sendToast("updating enemy destination cards.");
        pause();
        Player[] players = ModelFacade.getInstance().getRunningGame().getPlayers();
        for(int i = 0; i<players.length; i++){
            String name = players[i].getUsername();
            if(!name.equals(ModelFacade.getInstance().getUsername())){
                DestinationCard[] cards = new DestinationCard[4];
                for(int j = 0; j<cards.length; j++){
                    ArrayList<City> cities = ModelFacade.getInstance().getRunningGame().getGameBoard().getCities();
                    String name1 = cities.get((i*j)%cities.size()).getName();
                    String name2 = cities.get((i*(j+1))/cities.size()).getName();
                    cards[j] = new DestinationCard(name1, name2, j*7);
                }
                ClientFacade.getInstance().updatePlayerDestinationCards(name, cards);
            }
        }
    }

    /**
     * Updates the visible (face up) cards in the train card deck.
     */
    private void updateFaceUpCards(){
        sendToast("Updating Face Up Cards.");
        pause();
        TrainCard[] cards = new TrainCard[5];
        int randMult = 0xffffff;
        for(int i = 0; i<cards.length; i++){
            int color = (0xff << 24) + (int)(Math.random()*randMult);
            cards[i] = new TrainCard(color);
        }
        ClientFacade.getInstance().updateFaceUpCards(new FaceUpCards(cards));
    }

    /**
     * Updates the number of invisible (face down) cards in train card deck.
     */
    private void updateTrainCardDeckNum(){
        sendToast("Updating Number of Train Cards in the Deck.");
        pause();
        ClientFacade.getInstance().updateNumTrainCardsInDeck((int)(Math.random()*100));
    }

    /**
     * Updates the number of cards in destination card deck.
     */
    private void updateDestinationCardDeckNum(){
        sendToast("Updating number of destination cards in the deck.");
        pause();
        ClientFacade.getInstance().updateNumDestinationCardsInDeck((int)(Math.random()*25));
    }

    /**
     * Adds claimed route (for any player). Shows this on the map.
     */
    private void addClaimedRoute(){
        String username = ModelFacade.getInstance().getUsername();
        Route route = ModelFacade.getInstance().getUnclaimedRoute();
        sendToast(username + " is claiming " + route.toString());
        pause();
        ClientFacade.getInstance().updatePlayerRoutes(username, route);
    }

    private void incrementTurn(){
        sendToast("Incrementing player turn number.");
        pause();
        int currTurn = RunningGameContainer.getInstance().getGame().getCurrentPlayer();
        ClientFacade.getInstance().updateTurn(currTurn+1);
    }

    private void sendToast(final String message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}

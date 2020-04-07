package ateam.tickettoride.Presenter;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.R;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.Player;

public class ClaimRoutePresenter implements Observer {
    private static final String TAG = "ClaimRoutePresenter";
    private static final String NOT_ENOUGH_CARDS = "You do not have the right cards";
    private static ClaimRoutePresenter instance = null;
    private Activity activity;

    public static ClaimRoutePresenter getInstance(){
        if(instance == null){
            instance = new ClaimRoutePresenter();
        }
        return instance;
    }

   private ClaimRoutePresenter(){
        activity = null;
        ModelFacade.getInstance().addRunningGameObserver(this);
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public void claimRoute(Route routeToClaim, Integer color){
        Game game = ModelFacade.getInstance().getRunningGame();
        String username = ModelFacade.getInstance().getUsername();
        Player me = game.findPlayer(username);
        ArrayList<TrainCard> myCards = me.getTrainCards();

        Integer routeLength = routeToClaim.getRouteLength();
        ArrayList<TrainCard> trainCardsToUse;
        trainCardsToUse = getTrainCardsToClaimRoute(myCards, routeLength, color);

        if(trainCardsToUse == null){
            ViewFacade.getInstance().showErrorMessageGame(activity, NOT_ENOUGH_CARDS);
        }
        else{
            Log.i(TAG, "I had the correct number and type of cards");
            // Claim the route
            MapPresenter.getInstance().claimRoute(routeToClaim, trainCardsToUse);
        }
    }

    private ArrayList<TrainCard> getTrainCardsToClaimRoute(ArrayList<TrainCard> cards, Integer routeLength, Integer color){
        TrainCard currentCard = null;
        ArrayList<TrainCard> cardsToUse = new ArrayList<>();

        // Find cards that match the actual color
        for(int i = 0; i < cards.size(); i++){
            if(cardsToUse.size() < routeLength) {
                currentCard = cards.get(i);
                if(currentCard.getCardColor() == color){
                    cardsToUse.add(currentCard);
                }
            }
            else{
                break;
            }
        }

        // Find locomotive cards to fill in the rest
        if(cardsToUse.size() < routeLength && color != 0xff808080){
            for(int i = 0; i < cards.size(); i++){
                if(cardsToUse.size() < routeLength) {
                    currentCard = cards.get(i);
                    if(currentCard.getCardColor() == 0xff808080){
                        cardsToUse.add(currentCard);
                    }
                }
                else{
                    break;
                }
            }
        }

        // If you do not have enough cards return null
        if(cardsToUse.size() != routeLength){
            return null;
        }

        return cardsToUse;
    }

    public ArrayList<Route> getUnclaimedRoutes(){
        return ModelFacade.getInstance().getUnclaimedRoutes();
    }

    @Override
    public void update(Observable observable, Object o) {
        if(o.equals("updateClaimedGameRoutes") && activity != null){
            ArrayList<Route> unclaimedRoutes = new ArrayList<>(ModelFacade.getInstance().getUnclaimedRoutes());
            ViewFacade.getInstance().updateUnclaimedRoutes(activity, unclaimedRoutes);
        }
    }

    // updateCityGraph(Route)
    // seeIfDestinationCardsAreDone
}

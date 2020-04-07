package ateam.tickettoride.Presenter;

import android.app.Activity;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Model.RunningGameContainer;
import ateam.tickettoride.View.Fragment.DrawTrainCardFragment;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.card.TrainCardDeck;
import ateam.tickettoride.common.model.Game;

public class DrawTrainCardPresenter implements Observer {
    private static final String TAG = "DrawTrainCardPresenter";
    private static DrawTrainCardPresenter instance = null;
    private Activity activity;
//    private DrawTrainCardFragment fragment;

    public static DrawTrainCardPresenter getInstance(){
        if(instance == null){
            instance = new DrawTrainCardPresenter();
        }
        return instance;
    }

    private DrawTrainCardPresenter(){
        activity = null;
        ModelFacade.getInstance().addRunningGameObserver(this);
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

//    public void setFragment(DrawTrainCardFragment fragment){
//        this.fragment = fragment;
//    }

    public void drawFaceUpTrainCard(int cardIndex){
        MapPresenter.getInstance().drawFaceUpTrainCard(cardIndex);
    }

    public void drawDeckTrainCard(){
        MapPresenter.getInstance().drawDeckTrainCard();
    }

    public void updateFaceUpCards(FaceUpCards faceUpCards){
        ModelFacade.getInstance().updateFaceUpCards(faceUpCards);
    }

    public FaceUpCards getFaceUpCards(){
        Game game = ModelFacade.getInstance().getRunningGame();

        return game.getFaceUpCards();
    }

    public int getSizeOfTrainCardDeck(){
        return ModelFacade.getInstance().getTrainCardDeckSize();
    }

    @Override
    public void update(Observable observable, Object o) {
        if(o.equals("updateFaceUpCards") && activity != null){
            Log.i(TAG, "updating faceup cards in presenter (observer)");
            Game game = ModelFacade.getInstance().getRunningGame();

            ViewFacade.getInstance().updateFaceUpCards(activity, game.getFaceUpCards());
        }
        if(o.equals("updateNumTrainCardsInDeck") && activity != null){
            int deckSize = ModelFacade.getInstance().getTrainCardDeckSize();

            ViewFacade.getInstance().updateNumTrainCardsInDeck(activity, deckSize);
        }

    }

    public void updatePlayerTrainCards(String name, TrainCard[] trainCards){
        ModelFacade.getInstance().updatePlayerTrainCards(name, trainCards);
    }
}

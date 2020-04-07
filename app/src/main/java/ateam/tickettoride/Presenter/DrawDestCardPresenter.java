package ateam.tickettoride.Presenter;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.card.DestinationCard;

public class DrawDestCardPresenter implements Observer {
    private static final String TAG = "DrawDestCardPresenter";
    private static DrawDestCardPresenter instance = null;
    private Activity activity;

    public static DrawDestCardPresenter getInstance(){
        if(instance == null){
            instance = new DrawDestCardPresenter();
        }
        return instance;
    }



    private DrawDestCardPresenter(){
        activity = null;
        ModelFacade.getInstance().addRunningGameObserver(this);
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public void drawDestCards(){
        MapPresenter.getInstance().drawDestCards();
    }

    public void discardDestCards(ArrayList<DestinationCard> cards){
        MapPresenter.getInstance().discardDestCards(cards);
    }

    public int getDestCardDeckSize(){
        return ModelFacade.getInstance().getDestCardDeckSize();
    }

    @Override
    public void update(Observable observable, Object o) {
        if(o.equals("updateNumDestinationCardsInDeck") && activity != null){
            int deckSize = ModelFacade.getInstance().getDestCardDeckSize();

            ViewFacade.getInstance().updateNumDestinationCardsInDeck(activity, deckSize);
        }
    }
}

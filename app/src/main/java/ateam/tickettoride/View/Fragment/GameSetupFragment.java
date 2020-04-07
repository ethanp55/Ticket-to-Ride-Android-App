package ateam.tickettoride.View.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import ateam.tickettoride.Logging.GameLogging;
import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.R;
import ateam.tickettoride.View.IView;
import ateam.tickettoride.View.ViewFacade;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.Player;


public class GameSetupFragment extends Fragment implements IView {
    private static final String TAG = "GameSetupFragment";
    private static final int FIRST_CARD_INDEX = 0;
    private static final int SECOND_CARD_INDEX = 1;
    private static final int THIRD_CARD_INDEX = 2;

    private ArrayList<DestinationCard> destCards = new ArrayList<>();
    private Button readyButton;
    private Button destCard1Button;
    private Button destCard2Button;
    private Button destCard3Button;

    public GameSetupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_game_setup, container, false);
        readyButton = (Button) v.findViewById(R.id.ready_button_setup);
        destCard1Button = (Button) v.findViewById(R.id.dest_card_1);
        destCard2Button = (Button) v.findViewById(R.id.dest_card_2);
        destCard3Button = (Button) v.findViewById(R.id.dest_card_3);

        destCards = PresenterFacade.getInstance().getDestCardsSetup();

        Log.i(TAG, GameLogging.getCurrentGameState());

        displayCards();
        setButtonListeners();

        return v;
    }

    public void displayCards(){
        Log.i(TAG, "displayingCards...");
        destCard1Button.setText(destCards.get(FIRST_CARD_INDEX).toString());
        destCard2Button.setText(destCards.get(SECOND_CARD_INDEX).toString());
        destCard3Button.setText(destCards.get(THIRD_CARD_INDEX).toString());
    }

    public void setButtonListeners(){
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Not Discarding Cards");
                PresenterFacade.getInstance().discardInitialDestCard(null);
            }
        });

        destCard1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Discarding " + destCards.get(FIRST_CARD_INDEX).toStringDest());
                PresenterFacade.getInstance().discardInitialDestCard(destCards.get(FIRST_CARD_INDEX));
            }
        });

        destCard2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Discarding " + destCards.get(SECOND_CARD_INDEX).toStringDest());
                PresenterFacade.getInstance().discardInitialDestCard(destCards.get(SECOND_CARD_INDEX));
            }
        });

        destCard3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Discarding " + destCards.get(THIRD_CARD_INDEX).toStringDest());
                PresenterFacade.getInstance().discardInitialDestCard(destCards.get(THIRD_CARD_INDEX));
            }
        });

    }

    @Override
    public void update() {

    }

    @Override
    public void showErrorMessage(String errorMessage) {

    }


}

package ateam.tickettoride.View.Fragment;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import ateam.tickettoride.Presenter.DrawDestCardPresenter;
import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.R;
import ateam.tickettoride.View.IView;
import ateam.tickettoride.common.card.DestinationCard;


public class DrawDestCardFragment extends Fragment implements IView {
    private static final String TAG = "DrawDestCardFragment";
    private static final int FIRST_CARD_INDEX = 0;
    private static final int SECOND_CARD_INDEX = 1;
    private static final int THIRD_CARD_INDEX = 2;

    private int mDestCardDeckSize;
    private Button mDeckButton;
    private Button mDestCard1Button;
    private Button mDestCard2Button;
    private Button mDestCard3Button;
    private Button mReadyButton;
    private DestinationCard[] mDisplayedDestCards = new DestinationCard[3];
    private ArrayList<DestinationCard> mCardsClicked = new ArrayList<>();

    private boolean[] discardFlags;
    private ShapeDrawable selectedCardBackground;
    private ShapeDrawable deselectedCardBackground;

    public DrawDestCardFragment() {

        mDisplayedDestCards[FIRST_CARD_INDEX] = null;
        mDisplayedDestCards[SECOND_CARD_INDEX] = null;
        mDisplayedDestCards[THIRD_CARD_INDEX] = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_draw_dest_card, container, false);

        PresenterFacade.getInstance().setDrawDestCardPresenterActivity(getActivity());

        mDestCardDeckSize = PresenterFacade.getInstance().getSizeOfDestCardDeck();

        discardFlags = new boolean[3];

        mDeckButton = (Button) v.findViewById(R.id.deck_button_dest);
        mDestCard1Button = (Button) v.findViewById(R.id.dest_card_1_draw);
        mDestCard2Button = (Button) v.findViewById(R.id.dest_card_2_draw);
        mDestCard3Button = (Button) v.findViewById(R.id.dest_card_3_draw);
        mReadyButton = (Button) v.findViewById(R.id.ready_button_dest);

        setButtonListeners();

        bindNumDestCardsInDeck();
        bindDestCardInformation();

        selectedCardBackground = new ShapeDrawable();
        float[] cornerRadii = {5, 5, 5, 5, 5, 5, 5, 5};
        selectedCardBackground.setShape(new RoundRectShape(cornerRadii, null, null));
        selectedCardBackground.getPaint().setStrokeWidth(5);
        selectedCardBackground.getPaint().setStyle(Paint.Style.FILL);
        selectedCardBackground.getPaint().setColor(Color.YELLOW);

        deselectedCardBackground = new ShapeDrawable();
        deselectedCardBackground.setShape(new RoundRectShape(cornerRadii, null, null));
        deselectedCardBackground.getPaint().setStrokeWidth(5);
        deselectedCardBackground.getPaint().setStyle(Paint.Style.FILL);
        deselectedCardBackground.getPaint().setColor(Color.LTGRAY);

        return v;
    }

    private void setButtonListeners(){
        mDestCard1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!discardFlags[FIRST_CARD_INDEX]){
                    discardFlags[FIRST_CARD_INDEX] = true;
                    mDestCard1Button.setBackground(selectedCardBackground);
                }
                else{
                    discardFlags[FIRST_CARD_INDEX] = false;
                    mDestCard1Button.setBackground(deselectedCardBackground);
                }
            }
        });

        mDestCard2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!discardFlags[SECOND_CARD_INDEX]){
                    discardFlags[SECOND_CARD_INDEX] = true;
                    mDestCard2Button.setBackground(selectedCardBackground);
                }
                else{
                    discardFlags[SECOND_CARD_INDEX] = false;
                    mDestCard2Button.setBackground(deselectedCardBackground);
                }
            }
        });

        mDestCard3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!discardFlags[THIRD_CARD_INDEX]){
                    discardFlags[THIRD_CARD_INDEX] = true;
                    mDestCard3Button.setBackground(selectedCardBackground);
                }
                else{
                    discardFlags[THIRD_CARD_INDEX] = false;
                    mDestCard3Button.setBackground(deselectedCardBackground);
                }
            }
        });

        mReadyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ArrayList<DestinationCard> cardsToDiscard = new ArrayList<>();
                for (int i = 0; i < discardFlags.length; i++){
                    if (discardFlags[i]){
                        DestinationCard singleCard = mDisplayedDestCards[i];
                        if(singleCard != null) {
                            cardsToDiscard.add(singleCard);
                        }
                    }
                }
                //all is good
                if(cardsToDiscard.size() < 3) {
                    DrawDestCardPresenter.getInstance().discardDestCards(cardsToDiscard);
                }
                else{//too many cards selected to discard
                    Toast.makeText(getContext(), "You may only discard up to 2 Destination Cards.", Toast.LENGTH_LONG).show();
                }
            }
        });

        mDeckButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DrawDestCardPresenter.getInstance().drawDestCards();
                for (int i = 0; i < discardFlags.length; i++){
                    discardFlags[i] = false;
                }
            }
        });
    }

    private boolean canClick(){
        if(mCardsClicked.size() > 2){
            return false;
        }
        else{
            return true;
        }
    }

    private void bindDestCardInformation(){
        String text1 = null;
        String text2 = null;
        String text3 = null;

        if (mDisplayedDestCards[FIRST_CARD_INDEX] != null) {
            text1 = mDisplayedDestCards[FIRST_CARD_INDEX].toString();
        }
        mDestCard1Button.setText(text1);

        if(mDisplayedDestCards[SECOND_CARD_INDEX] != null) {
            text2 = mDisplayedDestCards[SECOND_CARD_INDEX].toString();
        }
        mDestCard2Button.setText(text2);

        if (mDisplayedDestCards[THIRD_CARD_INDEX] != null) {
            text3 = mDisplayedDestCards[THIRD_CARD_INDEX].toString();
        }
        mDestCard3Button.setText(text3);
    }

    public void bindNumDestCardsInDeck(){
        String deckString = "Deck \n " + mDestCardDeckSize;
        mDeckButton.setText(deckString);
    }

    public void updateNumDestinationCardsInDeck(int newDeckSize){
        mDestCardDeckSize = newDeckSize;
        update();
    }

    @Override
    public void update() {
        bindNumDestCardsInDeck();
        bindDestCardInformation();
    }

    public void updateDrawnDestCards(DestinationCard[] cards){
        mDisplayedDestCards[FIRST_CARD_INDEX] = cards[0];
        mDisplayedDestCards[SECOND_CARD_INDEX] = cards[1];
        mDisplayedDestCards[THIRD_CARD_INDEX] = cards[2];

        //reset discard flags
        discardFlags[0] = false;
        mDestCard1Button.setBackground(deselectedCardBackground);
        discardFlags[1] = false;
        mDestCard2Button.setBackground(deselectedCardBackground);
        discardFlags[2] = false;
        mDestCard3Button.setBackground(deselectedCardBackground);


        update();
    }

    @Override
    public void showErrorMessage(String errorMessage) {

    }
}

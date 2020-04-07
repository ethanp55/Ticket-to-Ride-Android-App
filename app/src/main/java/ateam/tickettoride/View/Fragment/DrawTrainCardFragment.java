package ateam.tickettoride.View.Fragment;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ateam.tickettoride.Presenter.DrawTrainCardPresenter;
import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.R;
import ateam.tickettoride.View.IView;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.card.TrainCardDeck;

public class DrawTrainCardFragment extends Fragment implements IView{
    private static final String TAG = "DrawTrainCardFragment";
    private static final int FIRST_CARD_INDEX = 0;
    private static final int SECOND_CARD_INDEX = 1;
    private static final int THIRD_CARD_INDEX = 2;
    private static final int FOURTH_CARD_INDEX = 3;
    private static final int FIFTH_CARD_INDEX = 4;

    private FaceUpCards mFaceUpCards;
    private int mSizeOfDeck;
    private Button mDeckButton;
    private Button mFaceUp1Button;
    private Button mFaceUp2Button;
    private Button mFaceUp3Button;
    private Button mFaceUp4Button;
    private Button mFaceUp5Button;
    private TextView mNumTrainCardsInDeckTextView;
    private Button[] buttons;

    private ShapeDrawable emptyFaceUpCardSpot;

    public DrawTrainCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        PresenterFacade.getInstance().setDrawTrainCardPresenterActivity(getActivity());

        View v = inflater.inflate(R.layout.fragment_draw_train_card, container, false);

        mDeckButton = (Button) v.findViewById(R.id.deck_button_train);
        mFaceUp1Button = (Button) v.findViewById(R.id.face_up_card_1);
        mFaceUp2Button = (Button) v.findViewById(R.id.face_up_card_2);
        mFaceUp3Button = (Button) v.findViewById(R.id.face_up_card_3);
        mFaceUp4Button = (Button) v.findViewById(R.id.face_up_card_4);
        mFaceUp5Button = (Button) v.findViewById(R.id.face_up_card_5);

        mFaceUpCards = PresenterFacade.getInstance().getFaceUpCards();
        mSizeOfDeck = PresenterFacade.getInstance().getSizeOfTrainCardDeck();

        emptyFaceUpCardSpot = new ShapeDrawable();
        emptyFaceUpCardSpot.setShape(new RectShape());
        emptyFaceUpCardSpot.getPaint().setStrokeWidth(5);
        emptyFaceUpCardSpot.getPaint().setStyle(Paint.Style.STROKE);
        //set color to "chocolate"
        emptyFaceUpCardSpot.getPaint().setColor(0xffD2691E);

        buttons = new Button[5];
        buttons[0] = mFaceUp1Button;
        buttons[1] = mFaceUp2Button;
        buttons[2] = mFaceUp3Button;
        buttons[3] = mFaceUp4Button;
        buttons[4] = mFaceUp5Button;

        bindCardColor();
        bindNumTrainCardsInDeck();

        setButtonListeners();

        return v;
    }

    @Override
    public void update() {
        Log.i(TAG, "Updating Train card fragment");
        bindCardColor();
        bindNumTrainCardsInDeck();
    }


    public void setButtonListeners(){
        mDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Deck button clicked", Toast.LENGTH_SHORT).show();
                DrawTrainCardPresenter.getInstance().drawDeckTrainCard();
            }
        });

        mFaceUp1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Face1 button clicked", Toast.LENGTH_SHORT).show();
                DrawTrainCardPresenter.getInstance().drawFaceUpTrainCard(0);
            }
        });

        mFaceUp2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Face2 button clicked", Toast.LENGTH_SHORT).show();
                DrawTrainCardPresenter.getInstance().drawFaceUpTrainCard(1);
            }
        });

        mFaceUp3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Face3 button clicked", Toast.LENGTH_SHORT).show();
                DrawTrainCardPresenter.getInstance().drawFaceUpTrainCard(2);
            }
        });

        mFaceUp4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Face4 button clicked", Toast.LENGTH_SHORT).show();
                DrawTrainCardPresenter.getInstance().drawFaceUpTrainCard(3);
            }
        });

        mFaceUp5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Face5 button clicked", Toast.LENGTH_SHORT).show();
                DrawTrainCardPresenter.getInstance().drawFaceUpTrainCard(4);
            }
        });
    }

    public void bindCardColor(){
        Log.i(TAG, "Binding train card colors");
        TrainCard[] faceUpCardsArray = mFaceUpCards.getTrainCards();

        for (int i = 0; i<buttons.length; i++){
            if (faceUpCardsArray[i] != null){
                buttons[i].setBackgroundColor(faceUpCardsArray[i].getCardColor());
            }
            else{
                buttons[i].setBackground(emptyFaceUpCardSpot);
            }
        }

//        mFaceUp1Button.setBackgroundColor(faceUpCardsArray[FIRST_CARD_INDEX].getCardColor());
//        mFaceUp2Button.setBackgroundColor(faceUpCardsArray[SECOND_CARD_INDEX].getCardColor());
//        mFaceUp3Button.setBackgroundColor(faceUpCardsArray[THIRD_CARD_INDEX].getCardColor());
//        mFaceUp4Button.setBackgroundColor(faceUpCardsArray[FOURTH_CARD_INDEX].getCardColor());
//        mFaceUp5Button.setBackgroundColor(faceUpCardsArray[FIFTH_CARD_INDEX].getCardColor());
    }

    public void bindNumTrainCardsInDeck(){
        String deckString = "Deck \n " + mSizeOfDeck;
        mDeckButton.setText(deckString);
    }


    @Override
    public void showErrorMessage(String errorMessage) {

    }

    public void updateFaceUpCards(FaceUpCards faceUpCards){
        Log.i(TAG, "Updating Face Up Cards in Fragment");
        mFaceUpCards = faceUpCards;
        update();
    }

    public void updateNumTrainCardsInDeck(int deckSize){
        Log.i(TAG, "Updating num train Cards in Fragment");
        mSizeOfDeck = deckSize;
        update();
    }
}

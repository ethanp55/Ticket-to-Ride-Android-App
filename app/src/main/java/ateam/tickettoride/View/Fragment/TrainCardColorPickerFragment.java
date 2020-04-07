package ateam.tickettoride.View.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.R;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Player;


public class TrainCardColorPickerFragment extends DialogFragment {
    private static final String TAG = "CardColorPickerFragment";

    private TextView mPurpleCardNumTextView;
    private TextView mWhiteCardNumTextView;
    private TextView mBlueCardNumTextView;
    private TextView mYellowCardNumTextView;
    private TextView mOrangeCardNumTextView;
    private TextView mBlackCardNumTextView;
    private TextView mRedCardNumTextView;
    private TextView mGreenCardNumTextView;
    private TextView mLocoCardNumTextView;
    private Player[] mPlayers;
    private String mMyUsername;
    private Player mMyPlayer;
    private Integer mColor;
    private Route mRoute;
    AlertDialog mAlertDialog;

    public static TrainCardColorPickerFragment newInstance(Route route) {
        TrainCardColorPickerFragment f = new TrainCardColorPickerFragment();

        Bundle args = new Bundle();
        args.putSerializable("route", route);
        f.setArguments(args);

        return f;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoute = (Route)getArguments().get("route");

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_train_card_color_picker, null);

        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Pick which color you would like to use.")
                .setPositiveButton("Pick", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(mColor == null){
                            Toast.makeText(getContext(), "You must select a color", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            PresenterFacade.getInstance().claimRoute(mRoute, mColor);
                        }
                    }
                })
                .create();

        mPlayers = PresenterFacade.getInstance().getPlayers();
        mMyUsername = PresenterFacade.getInstance().getUsername();

        mPurpleCardNumTextView = (TextView) v.findViewById(R.id.purple_train_cards_num_pick);
        mWhiteCardNumTextView = (TextView) v.findViewById(R.id.white_train_cards_num_pick);
        mBlueCardNumTextView = (TextView) v.findViewById(R.id.blue_train_cards_num_pick);
        mYellowCardNumTextView = (TextView) v.findViewById(R.id.yellow_train_cards_num_pick);
        mOrangeCardNumTextView = (TextView) v.findViewById(R.id.orange_train_cards_num_pick);
        mBlackCardNumTextView = (TextView) v.findViewById(R.id.black_train_cards_num_pick);
        mRedCardNumTextView = (TextView) v.findViewById(R.id.red_train_cards_num_pick);
        mGreenCardNumTextView = (TextView) v.findViewById(R.id.green_train_cards_num_pick);
        mLocoCardNumTextView = (TextView) v.findViewById(R.id.loco_train_cards_num_pick);

        bindMyTrainCardInformation();
        setTextViewColorListeners();

        return mAlertDialog;
    }

    private Player findMyself(){
        for(int i = 0; i < mPlayers.length; i++){
            if(mPlayers[i].getUsername().equals(mMyUsername)){
                return mPlayers[i];
            }
        }
        return null;
    }

    private void setTextViewColorListeners(){
        mPurpleCardNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorText(0xff800080, "purple");
            }
        });

        mWhiteCardNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorText(0xffffffff, "white");
            }
        });

        mBlueCardNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorText(0xff0000ff, "blue");
            }
        });

        mYellowCardNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorText(0xffffff00, "yellow");
            }
        });

        mOrangeCardNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorText(0xffffa500, "orange");
            }
        });

        mBlackCardNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorText(0xff000000, "black");
            }
        });

        mRedCardNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorText(0xffff0000, "red");
            }
        });

        mGreenCardNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorText(0xff008000, "green");
            }
        });

        mLocoCardNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorText(0xff808080, "whatever");
            }
        });
    }

    private void setColorText(Integer color, String name){
        mColor = color;
        String text = "You selected the color " + name;
        mAlertDialog.setTitle(text);
    }

    private void bindMyTrainCardInformation(){
        mMyPlayer = findMyself();
        ArrayList<TrainCard> myCards = new ArrayList<>(mMyPlayer.getTrainCards());
        int numLocoCards = 0;
        int numWhiteCards = 0;
        int numBlueCards = 0;
        int numBlackCards = 0;
        int numRedCards = 0;
        int numYellowCards = 0;
        int numGreenCards = 0;
        int numPurpleCards = 0;
        int numOrangeCards = 0;

        for(int i = 0; i < myCards.size(); i++){
            if(myCards.get(i).getCardColor() ==  0xff808080){
                numLocoCards+=1;
            }
            else if(myCards.get(i).getCardColor() == 0xffffffff) {
                numWhiteCards+=1;
            }
            else if(myCards.get(i).getCardColor() == 0xff0000ff) {
                numBlueCards+=1;
            }
            else if(myCards.get(i).getCardColor() == 0xff000000) {
                numBlackCards+=1;
            }
            else if(myCards.get(i).getCardColor() == 0xffff0000) {
                numRedCards+=1;
            }
            else if(myCards.get(i).getCardColor() == 0xffffff00) {
                numYellowCards+=1;
            }
            else if(myCards.get(i).getCardColor() == 0xff008000) {
                numGreenCards+=1;
            }
            else if(myCards.get(i).getCardColor() == 0xff800080) {
                numPurpleCards+=1;
            }
            else if(myCards.get(i).getCardColor() == 0xffffa500) {
                numOrangeCards+=1;
            }
            else {
                Log.e(TAG, "Unknown Card Type Found");
            }
        }

        mLocoCardNumTextView.setText(Integer.toString(numLocoCards));
        mWhiteCardNumTextView.setText(Integer.toString(numWhiteCards));
        mBlueCardNumTextView.setText(Integer.toString(numBlueCards));
        mBlackCardNumTextView.setText(Integer.toString(numBlackCards));
        mRedCardNumTextView.setText(Integer.toString(numRedCards));
        mYellowCardNumTextView.setText(Integer.toString(numYellowCards));
        mGreenCardNumTextView.setText(Integer.toString(numGreenCards));
        mPurpleCardNumTextView.setText(Integer.toString(numPurpleCards));
        mOrangeCardNumTextView.setText(Integer.toString(numOrangeCards));

        setTextViewColors();
    }



    public void setTextViewColors(){
        mLocoCardNumTextView.setBackgroundColor(0xff808080);
        mWhiteCardNumTextView.setBackgroundColor(0xffffffff);
        mBlueCardNumTextView.setBackgroundColor(0xff0000ff);
        mBlackCardNumTextView.setBackgroundColor(0xff000000);
        mRedCardNumTextView.setBackgroundColor(0xffff0000);
        mYellowCardNumTextView.setBackgroundColor(0xffffff00);
        mGreenCardNumTextView.setBackgroundColor(0xff008000);
        mPurpleCardNumTextView.setBackgroundColor(0xff800080);
        mOrangeCardNumTextView.setBackgroundColor(0xffffa500);

        mBlackCardNumTextView.setTextColor(0xffffffff);
        mRedCardNumTextView.setTextColor(0xffffffff);
        mPurpleCardNumTextView.setTextColor(0xffffffff);
        mBlueCardNumTextView.setTextColor(0xffffffff);
        mGreenCardNumTextView.setTextColor(0xffffffff);
        mLocoCardNumTextView.setTextColor(0xffffffff);
        mOrangeCardNumTextView.setTextColor(0xffffffff);
    }

}


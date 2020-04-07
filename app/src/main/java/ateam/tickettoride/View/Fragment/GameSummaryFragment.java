package ateam.tickettoride.View.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.R;
import ateam.tickettoride.View.IView;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.model.Player;

public class GameSummaryFragment extends Fragment implements IView {
    private static final String TAG = "GameSummaryFragment";

    private Button mChatButton;
    private Button mGameHistoryButton;
    private Button mDestinationCardButton;
    private Button mSendChatButton;
    private EditText mMessageBox;
    private RecyclerView mTabRecyclerView;
    private RecyclerView mSummaryRecyclerView;
    private TabRecyclerAdapter mTabAdapter;
    private SummaryInfoAdapter mSummaryAdapter;
    private TextView mPurpleCardNumTextView;
    private TextView mWhiteCardNumTextView;
    private TextView mBlueCardNumTextView;
    private TextView mYellowCardNumTextView;
    private TextView mOrangeCardNumTextView;
    private TextView mBlackCardNumTextView;
    private TextView mRedCardNumTextView;
    private TextView mGreenCardNumTextView;
    private TextView mLocoCardNumTextView;

    private String mChatMessage;
    private ArrayList<String> allMessages = ModelFacade.getInstance().getRunningGame().getChatHistory();
    private ArrayList<String> history = ModelFacade.getInstance().getRunningGame().getGameHistory();
    private Player mMyPlayer;
    private String mMyUsername;
    private ArrayList<String> allDestCards;
    private ArrayList<String> displayedList = new ArrayList<>();
    private ArrayList<Player> mPlayers;
    private int ghettoState;


    public GameSummaryFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_summary, container, false);
        PresenterFacade.getInstance().setGameSummaryPresenterActivity(getParentFragment());

        // Get the players to display on the summary page
        mMyUsername = PresenterFacade.getInstance().getUsername();
        mPlayers = new ArrayList<> (Arrays.asList(PresenterFacade.getInstance().getPlayers()));
        mMyPlayer = findMyself();

        mPurpleCardNumTextView = (TextView) v.findViewById(R.id.purple_train_cards_num_text);
        mWhiteCardNumTextView = (TextView) v.findViewById(R.id.white_train_cards_num_text);
        mBlueCardNumTextView = (TextView) v.findViewById(R.id.blue_train_cards_num_text);
        mYellowCardNumTextView = (TextView) v.findViewById(R.id.yellow_train_cards_num_text);
        mOrangeCardNumTextView = (TextView) v.findViewById(R.id.orange_train_cards_num_text);
        mBlackCardNumTextView = (TextView) v.findViewById(R.id.black_train_cards_num_text);
        mRedCardNumTextView = (TextView) v.findViewById(R.id.red_train_cards_num_text);
        mGreenCardNumTextView = (TextView) v.findViewById(R.id.green_train_cards_num_text);
        mLocoCardNumTextView = (TextView) v.findViewById(R.id.loco_train_cards_num_text);
        mChatButton = (Button) v.findViewById(R.id.chatButton);
        mGameHistoryButton = (Button) v.findViewById(R.id.gameHistoryButton);
        mDestinationCardButton = (Button) v.findViewById(R.id.destCardButton);
        mSendChatButton = (Button) v.findViewById(R.id.sendChatButton);
        mMessageBox = (EditText) v.findViewById(R.id.messageBox);
        mTabRecyclerView = (RecyclerView) v.findViewById(R.id.tabRecyclerView);
        mSummaryRecyclerView = (RecyclerView) v.findViewById(R.id.summaryRecyclerView);
        mSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        System.out.println("The number of players is: " + mPlayers.size());

        mSummaryAdapter = new SummaryInfoAdapter(mPlayers);
        mSummaryRecyclerView.setAdapter(mSummaryAdapter);

        mTabRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(allMessages.size() == 0) {
            ArrayList<String> testArray = new ArrayList<>();
            testArray.add("Welcome to the chat room");
            mTabAdapter = new TabRecyclerAdapter(testArray);
        }
        else {
            mTabAdapter = new TabRecyclerAdapter(allMessages);
        }
        mTabRecyclerView.setAdapter(mTabAdapter);

        mMessageBox.setInputType(InputType.TYPE_CLASS_TEXT);
        ghettoState = 0;

        setListeners();

        // Display the player information
        bindPlayerInformation();
        bindMyTrainCardInformation();

        return v;
    }

    void bindPlayerInformation(){
        mSummaryAdapter = new SummaryInfoAdapter(mPlayers);
        mSummaryRecyclerView.setAdapter(mSummaryAdapter);
    }

    public void updateChat(ArrayList<String> chatHistory) {
        System.out.println("Updating chat");
        if(ghettoState == 0) {
            mTabAdapter = new TabRecyclerAdapter(chatHistory);
            mTabRecyclerView.setAdapter(mTabAdapter);
        }
    }

    public void updateGameHistory(ArrayList<String> gameHistory) {
        history = gameHistory;
        if(ghettoState == 1) {
            mTabAdapter = new TabRecyclerAdapter(gameHistory);
            mTabRecyclerView.setAdapter(mTabAdapter);
        }
    }

    //Create List of Strings representing Destination Cards
    public void loadDestinationCards() {
        mMyPlayer = findMyself();
        allDestCards = new ArrayList<>();
        ArrayList<DestinationCard> DestinationCards = mMyPlayer.getDestinationCards();
        for(int i = 0; i < DestinationCards.size(); i++) {
            //TODO: fix bug with null destination cards from server, when deck runs out
            String destCardString = DestinationCards.get(i).toString();
            if(DestinationCards.get(i).isCompleted()){
                destCardString+= " COMPLETED!";
                Log.i(TAG, "COMPLETED!");
            }
            allDestCards.add(destCardString);
        }
        mTabAdapter = new TabRecyclerAdapter(allDestCards);
        mTabRecyclerView.setAdapter(mTabAdapter);
    }

    //Could use states here I think
    //0 is chat, 1 is game history, 2 is destination cards.
    public void setListeners() {
        mChatButton.setOnClickListener(new View.OnClickListener() {
            //Get Chat List
            @Override
            public void onClick(View v) {
                if(ghettoState != 0) {
                    System.out.println("Chat Button Clicked: " + ghettoState);
                    mTabAdapter = new TabRecyclerAdapter(allMessages);
                    mTabRecyclerView.setAdapter(mTabAdapter);
                    ghettoState = 0;
                }
            }
        });
        mGameHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //Get Game History List
            public void onClick(View v) {
                if(ghettoState != 1) {
                    mTabAdapter = new TabRecyclerAdapter(history);
                    mTabRecyclerView.setAdapter(mTabAdapter);
                    ghettoState = 1;
                }
            }
        });
        mDestinationCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //Get Destination Card List
            public void onClick(View v) {
                if(ghettoState != 2) {
                    ghettoState = 2;
                }
                loadDestinationCards();
            }
        });
        mSendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //Get Destination Card List
            public void onClick(View v) {
                //System.out.println("Test Button Click");
                mChatMessage = mMessageBox.getText().toString();
                if(ghettoState == 0 && mChatMessage.length() > 0) {
                    //System.out.println("Test Button Click2");
                    PresenterFacade.getInstance().sendChat(mChatMessage);
                    mMessageBox.getText().clear();
                }
            }
        });
        mMessageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ghettoState != 0) {
                    mTabAdapter = new TabRecyclerAdapter(allMessages);
                    mTabRecyclerView.setAdapter(mTabAdapter);
                    ghettoState = 0;
                }
            }
        });
        mMessageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    mChatMessage = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
       });
    }

    public void updatePlayers(ArrayList<Player> players){
        mPlayers = players;
        update();
    }

    public Player findMyself(){
        for(int i = 0; i < mPlayers.size(); i++){
            if(mPlayers.get(i).getUsername().equals(mMyUsername)){
                return mPlayers.get(i);
            }
        }
        return null;
    }

    @Override
    public void update() {
        bindPlayerInformation();
        bindMyTrainCardInformation();
        loadDestinationCards();
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

    @Override
    public void showErrorMessage(String errorMessage) {

    }

    //TODO Refactor these into seperate classes!
    private class TabInfoHolder extends RecyclerView.ViewHolder {
        private TextView mStringHolder;

        public TabInfoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.summary_recycler_item, parent, false));

            mStringHolder = (TextView) itemView.findViewById(R.id.string_holder);

        }

        public void bind(String message) {
            mStringHolder.setText(message);
        }
    }

    private class TabRecyclerAdapter extends RecyclerView.Adapter<TabInfoHolder> {
        public TabRecyclerAdapter(ArrayList<String> messages) {
            displayedList = messages;
        }

        @NonNull
        @Override
        public TabInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());

            return new TabInfoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TabInfoHolder tabInfoHolder, int i) {
            tabInfoHolder.bind(displayedList.get(i));
        }

        @Override
        public int getItemCount() {
            return displayedList.size();
        }
    }

    private class SummaryInfoHolder extends RecyclerView.ViewHolder {
        private TextView mPlayerName;
        private TextView mNumPoints;
        private TextView mNumTrains;
        private TextView mNumDestCards;
        private TextView mNumTrainCards;
        private TextView mNumRoutes;

        public SummaryInfoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.summary_list_item, parent, false));

            mPlayerName = (TextView) itemView.findViewById(R.id.playerName);
            mNumPoints = (TextView) itemView.findViewById(R.id.totalPoints);
            mNumTrains = (TextView) itemView.findViewById(R.id.destinationGaine);
            mNumDestCards = (TextView) itemView.findViewById(R.id.destinationLost);
            mNumTrainCards = (TextView) itemView.findViewById(R.id.cardsHeld);
            mNumRoutes = (TextView) itemView.findViewById(R.id.routes);


        }

        public void bind(Player player) {
            mPlayerName.setText(player.getUsername());
            mNumPoints.setText(String.valueOf(player.getPoints()));
            mNumTrains.setText(String.valueOf(player.getNumRemainingTrainPieces()));
            mNumDestCards.setText(String.valueOf(player.getDestinationCards().size()));
            mNumTrainCards.setText(String.valueOf(player.getTrainCards().size()));
            mNumRoutes.setText(String.valueOf(player.getClaimedRoutes().size()));
        }
    }

    private class SummaryInfoAdapter extends RecyclerView.Adapter<SummaryInfoHolder> {
        //private ArrayList<Player> gamePlayers;

        public SummaryInfoAdapter(ArrayList<Player> players) {
            //gamePlayers = players;
        }

        @NonNull
        @Override
        public SummaryInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());

            return new SummaryInfoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SummaryInfoHolder summaryInfoHolder, int i) {
            summaryInfoHolder.bind(mPlayers.get(i));
        }

        @Override
        public int getItemCount() {
            return mPlayers.size();
        }
    }
}

package ateam.tickettoride.View.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.EndgamePresenter;
import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.R;
import ateam.tickettoride.View.IView;
import ateam.tickettoride.common.model.Player;
import ateam.tickettoride.common.requests.LongestPathRequest;

public class EndgameFragment extends Fragment implements IView {
    private RecyclerView mTabRecyclerView;
    private RecyclerView mSummaryRecylcerView;
    private TabRecyclerAdapter mTabAdapter;
    private SummaryInfoAdapter mSummaryAdapter;
    private Button mGameHistoryButton;
    private Button mExitButton;
    private TextView mLongestRoute;
    private TextView mWinner;

    private int ghettoState;
    private ArrayList<Player> longestRouteHolder = new ArrayList<>();
    private ArrayList<Player> winnerHolder = new ArrayList<>();
    private ArrayList<String> displayedList = new ArrayList<>();
    private ArrayList<String> history = ModelFacade.getInstance().getRunningGame().getGameHistory();
    private ArrayList<Player> mPlayers = new ArrayList<>();

    public EndgameFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PresenterFacade.getInstance().setEndgamePresenterActivity(getActivity());
        mPlayers = new ArrayList<>(Arrays.asList(ModelFacade.getInstance().getRunningGame().getPlayers()));
        calculatePoints();
        calculateLongest();
        winnerHolder = calculateWinner();

        View v = inflater.inflate(R.layout.fragment_endgame, container, false);

        mTabRecyclerView = (RecyclerView) v.findViewById(R.id.tabRecyclerView);
        mSummaryRecylcerView = (RecyclerView) v.findViewById(R.id.summaryRecyclerView);
        mExitButton = (Button) v.findViewById(R.id.exitButton);
        mGameHistoryButton = (Button) v.findViewById(R.id.gameHistoryButton);
        mExitButton = (Button) v.findViewById(R.id.exitButton);
        mLongestRoute = (TextView) v.findViewById(R.id.longestRoute);
        mWinner = (TextView) v.findViewById(R.id.winner);

        mTabRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTabAdapter = new TabRecyclerAdapter(history);
        mTabRecyclerView.setAdapter(mTabAdapter);

        mSummaryRecylcerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSummaryAdapter = new SummaryInfoAdapter();
        mSummaryRecylcerView.setAdapter(mSummaryAdapter);

        ghettoState = 0;

        setLongestRouteString(longestRouteHolder);
        setWinnerString();
        setListeners();

        return v;
    }

    public void setListeners() {
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
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Exiting");
                EndgamePresenter.getInstance().exitGame(getActivity());
            }
        });
    }

    void bindPlayerInformation(){
        mSummaryAdapter = new SummaryInfoAdapter();
        mSummaryRecylcerView.setAdapter(mSummaryAdapter);
    }

    public void updateGameHistory(ArrayList<String> gameHistory) {
        history = gameHistory;
        if(ghettoState == 1) {
            mTabAdapter = new TabRecyclerAdapter(gameHistory);
            mTabRecyclerView.setAdapter(mTabAdapter);
        }
    }

    public void updatePlayers(ArrayList<Player> players){
        mPlayers = players;
        update();
    }

    //longestPath is the scores of players in order.
    public void updateLongest(Integer[] longestPath) {
        System.out.println("Updating EndgameFragment Longest");
        ArrayList<Player> longestPlayers = new ArrayList<>();
        int longest = 0;
        for(int i = 0; i < longestPath.length; i++) {
            System.out.println("CurrPlayer " + mPlayers.get(i).getUsername() + ": " + longestPath[i].toString());
            if(longestPath[i] > longest) {
                longest = longestPath[i];
                longestPlayers.clear();
                longestPlayers.add(mPlayers.get(i));
            }
            else if(longestPath[i] == longest) {
                longestPlayers.add(mPlayers.get(i));
            }
            else {}
        }
        longestRouteHolder = longestPlayers;
        //Add 10 points to winner of longest path
        for(int i = 0; i < longestPlayers.size(); i++) {
            for(int j = 0; j < mPlayers.size(); j++) {
                if(longestPlayers.get(i).getUsername().equals(mPlayers.get(j).getUsername())) {
                    mPlayers.get(j).setTotalPoints(longestPlayers.get(i).getTotalPoints() + 10);
                }
            }
        }
        update();
        setLongestRouteString(longestRouteHolder);
    }

    @Override
    public void update() {
        bindPlayerInformation();
    }

    @Override
    public void showErrorMessage(String errorMessage) {

    }

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
        private TextView mTotalPoints;
        private TextView mDestinationGained;
        private TextView mDestinationLost;
        private TextView mRoutePoints;

        public SummaryInfoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.endgame_list_item, parent, false));

            mPlayerName = (TextView) itemView.findViewById(R.id.playerName);
            mTotalPoints = (TextView) itemView.findViewById(R.id.totalPoints);
            mDestinationGained = (TextView) itemView.findViewById(R.id.destinationGained);
            mDestinationLost = (TextView) itemView.findViewById(R.id.destinationLost);
            mRoutePoints = (TextView) itemView.findViewById(R.id.routePoints);
        }

        public void bind(Player player) {
            mPlayerName.setText(player.getUsername());
            mTotalPoints.setText(Integer.toString(player.getTotalPoints()));
            mDestinationGained.setText(Integer.toString(player.getDestinationPointsGained()));
            mDestinationLost.setText(Integer.toString(player.getDestinationPointsLost()));
            mRoutePoints.setText(Integer.toString(player.getPoints()));
        }
    }

    private class SummaryInfoAdapter extends RecyclerView.Adapter<SummaryInfoHolder> {
        //private ArrayList<Player> gamePlayers;

        public SummaryInfoAdapter() {
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

    private void calculatePoints() {
        System.out.println("Calculating points");
        for(int i = 0; i < mPlayers.size(); i++) {
            mPlayers.get(i).calculatePoints();
            System.out.println(mPlayers.get(i).getUsername() + ": " + mPlayers.get(i).getTotalPoints());
        }
    }

    private void calculateLongest() {
        Player myself = findMyself();
        System.out.println("I am: " + myself.getUsername());
        int myLongest = myself.calculateLongestPath();
        EndgamePresenter.getInstance().sendLongest(myLongest);
        /*
        System.out.println("Calculating Longest");
        ArrayList<Player> listOfLongest = new ArrayList<>();
        int currLongest = 0;

        for(int i = 0; i < inputList.size(); i++) {
            Player currPlayer = inputList.get(i);
            int currPlayerLength = currPlayer.calculateLongestPath();
            System.out.println("CurrentHolder: " + currLongest);
            System.out.println("Contender " + currPlayer.getUsername() + ": " + currPlayerLength);
            if(currPlayerLength > currLongest) {
                System.out.println("currPlayer has more routes");
                currLongest = currPlayerLength;
                listOfLongest.clear();
                listOfLongest.add(currPlayer);
            }
            else if(currPlayerLength == currLongest) {
                System.out.println("currPlayer has same number");
                listOfLongest.add(currPlayer);
            }
            else {}

        }
        //
        Legacy code, uncomment if not doing extra credit.
        for(int i = 1; i < inputList.size(); i++) {
            Player currPlayer = inputList.get(i);
            System.out.println("Current Holder: " + longestRoutePlayer.getClaimedRoutes().size());
            System.out.println("Current Player: " + currPlayer.getClaimedRoutes().size());
            //currPlayer has more claimed routes than current Longest Route Holder.

            if(currPlayer.getClaimedRoutes().size() > longestRoutePlayer.getClaimedRoutes().size()) {
                System.out.println("currPlayer has more routes");
                longestRoutePlayer = currPlayer;
                //If there were two sharing the prize
                //if(listOfLongest.size() > 0) {
                    listOfLongest.clear();
                    listOfLongest.add(currPlayer);
                //}
            }
            //Number of claimed routes is the same.
            else if(currPlayer.getClaimedRoutes().size() == longestRoutePlayer.getClaimedRoutes().size()) {
                System.out.println("They have same number of routes");
                listOfLongest.add(currPlayer);
            }
            //Number of claimed routes is less, do nothing.
            else {}
        }
        //
        //Add 10 points to winner of longest path
        for(int i = 0; i < listOfLongest.size(); i++) {
            listOfLongest.get(i).setTotalPoints(listOfLongest.get(i).getTotalPoints() + 10);
        }
        return listOfLongest;
        */
    }

    private void setLongestRouteString(ArrayList<Player> longestPlayers) {
        StringBuilder sb = new StringBuilder();
        sb.append("The person(s) with the longest route: ");
        for(int i = 0; i < longestPlayers.size(); i++) {
            sb.append(longestPlayers.get(i).getUsername() + " ");
        }
        mLongestRoute.setText(sb.toString());
    }

    //TODO Break up/Refactor this and maybe put it somewhere else. CALEB
    private ArrayList<Player> calculateWinner() {
        System.out.println("Calculating Winner");
        ArrayList<Player> winnerList = new ArrayList<>();
        ArrayList<Player> winnerDestination = new ArrayList<>();
        ArrayList<Player> winnerLongest = new ArrayList<>();
        Player currWinner = mPlayers.get(0);

        winnerList.add(currWinner);
        //Find player(s) with highest points
        for(int i = 1; i < mPlayers.size(); i++) {
            Player currPlayer = mPlayers.get(i);
            if(currPlayer.getTotalPoints() > currWinner.getTotalPoints()) {
                System.out.println("currPlayer has more points");
                winnerList.clear();
                currWinner = currPlayer;
                winnerList.add(currWinner);
            }
            else if(currPlayer.getTotalPoints() == currWinner.getTotalPoints()) {

                winnerList.add(currPlayer);
            }
            else {}
        }
        //if more than >1 player has high score, find who completed most destination cards.
        if(winnerList.size() > 1) {
            System.out.println("They have same number of points");
            currWinner = winnerList.get(0);
            winnerDestination.add(currWinner);
            for(int i = 1; i < winnerList.size(); i++) {
                Player currPlayer = winnerList.get(i);
                if(currPlayer.getDestinationsCompleted().size() > currWinner.getDestinationsCompleted().size()) {
                    winnerDestination.clear();
                    currWinner = currPlayer;
                    winnerDestination.add(currWinner);
                }
                else if(currPlayer.getDestinationsCompleted().size() == currWinner.getDestinationsCompleted().size()) {
                    winnerDestination.add(currPlayer);
                }
                else{}
            }
        }
        //Winner has been chosen based on points.
        else{
            return winnerList;
        }

        //if winner is still not decided after destination cards, look at who has longest road.
        //If winner is still not decided after longest route, they all win.
        //TODO REDO CHECKING IF LONGEST ROUTE NEEDED TO DECIDE.
        if(winnerDestination.size() > 1) {
            System.out.println("Welp");
            return winnerLongest;
        }
        //winner has been chosen based off completed destination cards.
        else {
            return winnerDestination;
        }
    }

    private void setWinnerString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The winner(s): ");
        for(int i = 0; i < winnerHolder.size(); i++) {
            sb.append(winnerHolder.get(i).getUsername() + " ");
        }
        mWinner.setText(sb.toString());
    }

    public Player findMyself(){
        for(int i = 0; i < mPlayers.size(); i++){
            if(mPlayers.get(i).getUsername().equals(ModelFacade.getInstance().getUsername())){
                return mPlayers.get(i);
            }
        }
        return null;
    }
}

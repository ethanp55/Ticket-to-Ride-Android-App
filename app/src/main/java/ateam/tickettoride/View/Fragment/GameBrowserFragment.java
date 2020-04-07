package ateam.tickettoride.View.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ateam.tickettoride.Model.GameBrowser;
import ateam.tickettoride.Model.GameBrowserContainer;
import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.GameBrowserPresenter;
import ateam.tickettoride.R;
import ateam.tickettoride.View.IView;
import ateam.tickettoride.common.model.GameInfo;
import ateam.tickettoride.common.requests.CreateGameRequest;
import ateam.tickettoride.common.requests.JoinGameRequest;

public class GameBrowserFragment extends Fragment implements IView {


    private static final String TAG = "GameBrowserFragment";

    private String mGameName;
    private int mNumOfPlayers;

    private EditText mGameNameField;
    private EditText mNumOfPlayersField;
    private Button mCreateGameButton;
    private RecyclerView mGameBrowserRecyclerView;

    private GameInfoAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameBrowserPresenter.getInstance().setActivity(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_browser, container, false);

        mGameBrowserRecyclerView = (RecyclerView)v.findViewById(R.id.browser_recycler_view);
        mGameBrowserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        createGameNameListener(v);
        createNumOfPlayersListener(v);

        mCreateGameButton = (Button) v.findViewById(R.id.create_game_button);
        mCreateGameButton.setEnabled(false);
        mCreateGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameBrowserPresenter.getInstance().setActivity(getActivity());
                GameBrowserPresenter.getInstance().createGame(mGameName, mNumOfPlayers);
            }
        });

        updateUI();

        return v;
    }

    /**
     * Create a listener for the Game Name edit text box and updates the mGameName string
     * @param v the view associated with the listener
     */
    private void createGameNameListener(View v){
        mGameNameField = (EditText) v.findViewById(R.id.game_name_input);
        final View view = v;
        mGameNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mGameName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkForEmptyCreateGameValues(view);
            }
        });
    }

    /**
     * Create a listener for the number of players edit text and updates the mNumOfPlayers string
     * @param v the view associated with the listener
     */
    private void createNumOfPlayersListener(View v){
        mNumOfPlayersField = (EditText) v.findViewById(R.id.num_players_input);
        final View view = v;
        mNumOfPlayersField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){

                }
                else if(isAcceptableNumOfPlayers(s.toString())){
                    mNumOfPlayers = Integer.parseInt(s.toString());
                }
                else{
                    Toast.makeText(getContext(), "Invalid number of players.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkForEmptyCreateGameValues(view);
            }
        });
    }

    /**
     * Checks that each field that is needed for game creation is filled and enables the Create Game button
     * @param v the current view
     */
    private void checkForEmptyCreateGameValues(View v){
        Button createGameButton = (Button) v.findViewById(R.id.create_game_button);

        String s1 = mGameNameField.getText().toString();
        String s2 = mNumOfPlayersField.getText().toString();

        if(s1.equals("") || s2.equals("") || !isAcceptableNumOfPlayers(s2)){
            createGameButton.setEnabled(false);
        } else {
            createGameButton.setEnabled(true);
        }
    }

    /**
     * Checks if the number of players is valid
     * @param s the number of players in string format
     * @return true if number of players is between 2 and 5, false otherwise
     */
    private boolean isAcceptableNumOfPlayers(String s){
        int numPlayer = 0;
        try{
            numPlayer =  Integer.parseInt(s);
        }
        catch (NumberFormatException ex){
            return false;
        }

        if(numPlayer > 5 || numPlayer < 2){
            return false;
        }

        return true;
    }

    /**
     * Updates UI to adjust to user scrolling through joinable games
     */
    public void updateUI() {
        List<GameInfo> allGameInfo = ModelFacade.getInstance().getGames();

        mAdapter = new GameInfoAdapter(allGameInfo);
        mGameBrowserRecyclerView.setAdapter(mAdapter);
    }

    /**
     * GameInfoHolder class to hold information for each joinable game and to bind it to the corresponding view holder
     */
    private class GameInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String mGameName;
        private String mCreatedBy;
        private String mNumPlayers;
        private String mGameId;
        private TextView mGameNameView;
        private TextView mCreatedByView;
        private TextView mNumPlayersView;

        public GameInfoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.browser_list_item, parent, false));
            itemView.setOnClickListener(this);

            mGameNameView = (TextView) itemView.findViewById(R.id.browser_list_game_name);
            mCreatedByView = (TextView) itemView.findViewById(R.id.browser_list_created_by);
            mNumPlayersView = (TextView) itemView.findViewById(R.id.browser_list_num_player);
        }

        public void bind(GameInfo gameInfo){

            mGameName = gameInfo.getGameName();
            mCreatedBy = "Created by: " + gameInfo.getHost();
            mNumPlayers = gameInfo.getNumPlayers() + " out of " + gameInfo.getMaxNumPlayers() + " players";
            mGameId = gameInfo.getGameID();

            mGameNameView.setText(mGameName);
            mCreatedByView.setText(mCreatedBy);
            mNumPlayersView.setText(mNumPlayers);
        }


        @Override
        public void onClick(View view){
            GameBrowserPresenter.getInstance().setActivity(getActivity());
            GameBrowserPresenter.getInstance().joinGame(mGameId,  mGameName);
        }
    }

    private class GameInfoAdapter extends RecyclerView.Adapter<GameInfoHolder> {
        private List<GameInfo> mBrowserGameInfo;

        public GameInfoAdapter(List<GameInfo> gameInfo) {
            mBrowserGameInfo = gameInfo;
        }

        @NonNull
        @Override
        public GameInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());

            return new GameInfoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull GameInfoHolder holder, int position) {
            holder.bind(mBrowserGameInfo.get(position));
        }

        @Override
        public int getItemCount() {
            return mBrowserGameInfo.size();
        }
    }

    @Override
    public void update() {
        System.out.println("in Browser update");
        updateUI();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}

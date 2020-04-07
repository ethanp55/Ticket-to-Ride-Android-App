package ateam.tickettoride.View.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ateam.tickettoride.Model.GameBrowserContainer;
import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.GameLobbyPresenter;
import ateam.tickettoride.R;
import ateam.tickettoride.View.IView;
import ateam.tickettoride.common.model.GameInfo;

public class GameLobbyFragment extends Fragment implements IView {
    private static final String TAG = "GameBrowserFragment";


    private Button mStartGameButton;
    private RecyclerView mGameLobbyRecyclerView;
    private TextView mCreatedBy;
    private TextView mNumberOfPlayers;
    private View mView;

    private GameLobbyFragment.GameLobbyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameLobbyPresenter.getInstance().setActivity(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lobby, container, false);
        mView = v;
        mGameLobbyRecyclerView = (RecyclerView)v.findViewById(R.id.lobby_recycler_view);
        mGameLobbyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCreatedBy = (TextView) v.findViewById(R.id.create_by_text);
        String created_by = "Created by: " + ModelFacade.getInstance().getJoinedGame().getHost();
        mCreatedBy.setText(created_by);

        disableButtonForEveryoneButHost(v);

        mStartGameButton = (Button) v.findViewById(R.id.start_game_button);
        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameLobbyPresenter.getInstance().startGame(ModelFacade.getInstance().getJoinedGame().getGameId());
            }
        });

        updateUI();

        return v;
    }


    /**
     * Disables start game button for everyone but the host
     * @param v the view the button is on
     */
    private void disableButtonForEveryoneButHost(View v){
        Button createGameButton = (Button) v.findViewById(R.id.start_game_button);

        if(ModelFacade.getInstance().getUsername().equals(ModelFacade.getInstance().getJoinedGame().getHost())){
            createGameButton.setEnabled(true);
        } else {
            createGameButton.setEnabled(false);
        }
    }

    /**
     * Updates the UI pulling in information from the model to display
     */
    private void updateUI() {
        String[] playerNames = ModelFacade.getInstance().getJoinedGame().getPlayerList();
        displayNumPlayers(mView);

        mAdapter = new GameLobbyFragment.GameLobbyAdapter(playerNames);
        mGameLobbyRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Displays the current number of players vs the max number of players in a game
     * @param v the current view the text box is at
     */
    private void displayNumPlayers(View v){
        mNumberOfPlayers = (TextView) v.findViewById(R.id.lobby_num_player);
        int currNumPlayers = ModelFacade.getInstance().getJoinedGame().getCurrNumPlayers();
        int maxNumPlayers = ModelFacade.getInstance().getJoinedGame().getMaxNumPlayers();
        String num_of_players = currNumPlayers + " out of " + maxNumPlayers + " players";
        mNumberOfPlayers.setText(num_of_players);
    }

    private class CurrentPlayersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mPlayerName;

        public CurrentPlayersHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.lobby_list_item, parent, false));
            itemView.setOnClickListener(this);

            mPlayerName = (TextView) itemView.findViewById(R.id.name_holder);
        }

        public void bind(String name){
            mPlayerName.setText(name);
        }

        @Override
        public void onClick(View view){

        }
    }

    private class GameLobbyAdapter extends RecyclerView.Adapter<GameLobbyFragment.CurrentPlayersHolder> {
        private String[] mPlayerNames;


        public GameLobbyAdapter(String[] playerNames) {
            mPlayerNames = playerNames;
        }

        @NonNull
        @Override
        public GameLobbyFragment.CurrentPlayersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());

            return new GameLobbyFragment.CurrentPlayersHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull GameLobbyFragment.CurrentPlayersHolder holder, int position) {
            holder.bind(mPlayerNames[position]);
        }

        @Override
        public int getItemCount() {
            return mPlayerNames.length;
        }
    }

    @Override
    public void update() {
        System.out.println("in Lobby update");
        updateUI();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}

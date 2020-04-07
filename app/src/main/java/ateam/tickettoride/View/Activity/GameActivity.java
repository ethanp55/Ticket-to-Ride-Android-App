package ateam.tickettoride.View.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Presenter.DrawTrainCardPresenter;
import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.R;
import ateam.tickettoride.View.Fragment.ClaimRouteFragment;
import ateam.tickettoride.View.Fragment.DrawDestCardFragment;
import ateam.tickettoride.View.Fragment.DrawTrainCardFragment;
import ateam.tickettoride.View.Fragment.EndgameFragment;
import ateam.tickettoride.View.Fragment.GameBrowserFragment;
import ateam.tickettoride.View.Fragment.GameSetupFragment;
import ateam.tickettoride.View.Fragment.GameSummaryFragment;
import ateam.tickettoride.View.Fragment.LoginRegisterFragment;
import ateam.tickettoride.View.Fragment.MapFragment;
import ateam.tickettoride.View.IView;
import ateam.tickettoride.View.MyRunnable;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.card.FaceUpCards;
import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.card.TrainCardDeck;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Player;

public class GameActivity extends AppCompatActivity implements IView {
    private static final String TAG = "GameActivity";

    private TextView mGameName;
    private int mapID;
    private int endID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_game);

        PresenterFacade.getInstance().setMapPresenterActivity(this);
        PresenterFacade.getInstance().setGameSetupPresenterActivity(this);

        PresenterFacade.getInstance().setInMapFragment(false);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        android.support.v4.app.Fragment fragment = new GameSetupFragment();
        fm.beginTransaction().add(R.id.game_fragment_container, fragment).commit();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this, "You've come this far; there's no going back now.", Toast.LENGTH_SHORT).show();
    }

    public void goToMapFragment(){
        Log.i(TAG, "Moving to Map Fragment");

        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();
        mapID = fragment.getId();
        fm.beginTransaction().replace(R.id.game_fragment_container, fragment).commit();

        PresenterFacade.getInstance().setInMapFragment(true);
    }

    public void goToEndgameFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = new EndgameFragment();
        endID = fragment.getId();
        fm.beginTransaction().replace(R.id.game_fragment_container, fragment).commit();
    }

    public void updatePlayerTurn(int playerTurn){
        if(!PresenterFacade.getInstance().getInMapFragment()){
            // Band-aid on race conditions....
            try{
                Thread.sleep(1500);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
        runOnUiThread(new MyRunnable(playerTurn) {
            @Override
            public void run() {
                MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container);
                if(fragment != null){
                    fragment.updatePlayerTurn((int)getData());
                }
            }
        });
    }

    public void updateFaceUpCards(FaceUpCards faceUpCards){
        runOnUiThread(new MyRunnable(faceUpCards) {
            @Override
            public void run() {
//                DrawTrainCardFragment fragment = (DrawTrainCardFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container).getChildFragmentManager().findFragmentById(R.id.nav_view);
                DrawTrainCardFragment fragment = ((MapFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container)).getDrawTrainCardFragment();
                Log.i(TAG, "updating Face up Cards in GameActivity1");
                if(fragment != null){
                    Log.i(TAG, "updating Face up Cards in GameActivity2");
                    fragment.updateFaceUpCards((FaceUpCards)getData());
                }
            }
        });
    }

    public void updateNumTrainCardsInDeck(int deckSize){
        Log.i(TAG, "updating num train Cards in GameActivity");
        runOnUiThread(new MyRunnable(deckSize) {
            @Override
            public void run() {
//                DrawTrainCardFragment fragment = (DrawTrainCardFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container).getChildFragmentManager().findFragmentById(R.id.nav_view);
                DrawTrainCardFragment fragment = ((MapFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container)).getDrawTrainCardFragment();
                if(fragment != null){
                    fragment.updateNumTrainCardsInDeck((int) getData());
                }
            }
        });
    }

    public void updateUnclaimedRoutes(ArrayList<Route> unclaimedRoutes){
        runOnUiThread(new MyRunnable(unclaimedRoutes) {
            @Override
            public void run() {
//                ClaimRouteFragment fragment = (ClaimRouteFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container);
                ClaimRouteFragment fragment = ((MapFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container)).getClaimRouteFragment();
                if(fragment != null){
                    fragment.updateUnclaimedRoutes((ArrayList<Route>) getData());
                }
            }
        });
    }

    public void updateChat(final ArrayList<String> chatHistory) {
        runOnUiThread(new MyRunnable(chatHistory) {
            @Override
            public void run() {
                GameSummaryFragment fragment = (GameSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container).getChildFragmentManager().findFragmentById(R.id.summary_view);
                fragment.updateChat(chatHistory);
            }
        });
    }

    public void updateGameHistory(final ArrayList<String> gameHistory) {
        runOnUiThread(new MyRunnable(gameHistory) {
            @Override
            public void run() {
                GameSummaryFragment fragment = (GameSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container).getChildFragmentManager().findFragmentById(R.id.summary_view);
                fragment.updateGameHistory(gameHistory);
            }
        });
    }

    public void updateGameHistoryEnd(final ArrayList<String> gameHistory) {
        runOnUiThread(new MyRunnable(gameHistory) {
            @Override
            public void run() {
                EndgameFragment fragment = (EndgameFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container);
                fragment.updateGameHistory(gameHistory);
            }
        });
    }

    public void updateNumDestinationCardsInDeck(int deckSize){
        runOnUiThread(new MyRunnable(deckSize) {
            @Override
            public void run() {
//                DrawDestCardFragment fragment = (DrawDestCardFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container).getChildFragmentManager().findFragmentById(R.id.nav_view);
                DrawDestCardFragment fragment = ((MapFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container)).getDrawDestCardFragment();
                if(fragment != null){
                    fragment.updateNumDestinationCardsInDeck((int)getData());
                }
            }
        });
    }

    public void updateDrawnDestCards(final DestinationCard[] cards){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DrawDestCardFragment fragment = ((MapFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container)).getDrawDestCardFragment();
                if(fragment != null){
                    fragment.updateDrawnDestCards(cards);
                }
            }
        });
    }

    public void updateLastTurn(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container);
                if(fragment != null){
                    fragment.updateLastTurn();
                }
            }
        });
    }

    public void updatePlayers(Player[] players){
        runOnUiThread(new MyRunnable(players) {
            @Override
            public void run() {
                GameSummaryFragment fragment = (GameSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container).getChildFragmentManager().findFragmentById(R.id.summary_view);
                ArrayList<Player> tempList = new ArrayList<> (Arrays.asList((Player[])getData()));
                fragment.updatePlayers(tempList);
            }
        });
    }

    public void updatePlayersForRoutes(Player[] players){
        runOnUiThread(new MyRunnable(players) {
            @Override
            public void run() {
                MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container);
                fragment.updatePlayersForRoutes((Player[])getData());
            }
        });
    }

    public void updatePlayersLongest(final Integer[] longestPaths){
        runOnUiThread(new MyRunnable(longestPaths) {
            @Override
            public void run() {
                EndgameFragment fragment = (EndgameFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment_container);
                fragment.updateLongest(longestPaths);
            }
        });
    }

    @Override
    public void update() {

    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void moveToMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra("exited" , 1);
        startActivity(mainIntent);
    }
}

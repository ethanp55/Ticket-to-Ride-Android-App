package ateam.tickettoride.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import ateam.tickettoride.R;
import ateam.tickettoride.View.Fragment.GameBrowserFragment;
import ateam.tickettoride.View.Fragment.GameLobbyFragment;
import ateam.tickettoride.View.Fragment.LoginRegisterFragment;
import ateam.tickettoride.View.IView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int mLobbyId;
    private int mBrowserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Creating Main Activity");

        super.onCreate(savedInstanceState);
        //Iconify.with(new FontAwesomeModule());

        setContentView(R.layout.activity_main);


        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        Bundle b = getIntent().getExtras();
        if(b != null) {
            android.support.v4.app.Fragment fragment = new GameBrowserFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        else {
            android.support.v4.app.Fragment fragment = new LoginRegisterFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this, "There's no going back.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Switches the fragment displayed on MainActivity to the GameBrowserActivity
     */
    public void goToBrowserFragment(){
        Log.i(TAG, "Moving to Browser Fragment");

        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = new GameBrowserFragment();
        mBrowserId = fragment.getId();
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

    }


    /**
     * Switches the fragment displayed on MainActivity to the GameLobbyFragment
     */
    public void goToLobbyFragment(){
        Log.i(TAG, "Moving to Lobby Fragment");

        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = new GameLobbyFragment();
        mLobbyId = fragment.getId();
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

    }

    public void showErrorMessage(String errorMessage) {
        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void updateGameBrowser(){
        Log.i(TAG, "Updating Game Browser");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                ((IView)fragment).update();
            }
        });
    }

    public void updateGameLobby(){
        Log.i(TAG, "Updating Game Lobby");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                ((IView)fragment).update();
            }
        });
    }

    public void moveToGameActivity(){
        Log.i(TAG, "Moving to Game Activity");
        Intent gameIntent = new Intent(this, GameActivity.class);
        startActivity(gameIntent);
    }
}

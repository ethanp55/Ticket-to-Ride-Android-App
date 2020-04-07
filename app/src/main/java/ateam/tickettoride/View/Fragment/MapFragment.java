package ateam.tickettoride.View.Fragment;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ateam.tickettoride.ClientFacade;
import ateam.tickettoride.Logging.GameLogging;
import ateam.tickettoride.MagicButton;
import ateam.tickettoride.Model.ModelFacade;
import ateam.tickettoride.Model.RunningGameContainer;
import ateam.tickettoride.Presenter.MapPresenter;
import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.R;
import ateam.tickettoride.View.IView;
import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.map.City;
import ateam.tickettoride.common.map.Route;
import ateam.tickettoride.common.model.Game;
import ateam.tickettoride.common.model.Player;

public class MapFragment extends Fragment implements IView {
    private static final String TAG = "MapFragment";
    private ImageView mapView;
    private Button drawTrainCardButton;
    private Button summaryButton;
    private Button mDrawTrainCardButton;
    private Button mDrawDestCardButton;
    private Button mClaimRouteButton;
//    private Button mMagicButton;
    private TextView playerInfo;
    private TextView[] playerTextViews;
    private Player[] players;
    private int playerTurn;
    private float screenRatio;
    private String mPlayerUsername;
    private ArrayList<City> cities;
    private View mView;

    private DrawTrainCardFragment mDrawTrainCardFragment;
    private DrawDestCardFragment mDrawDestCardFragment;
    private ClaimRouteFragment mClaimRouteFragment;

    private MapDraw routeDrawing;
    private ShapeDrawable playerNameBorder;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private NavigationView mSummaryView;

//    private MagicButton magicButtonGuts;

    private Handler mHandler = new Handler();



    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_drawers, container, false);
        mView = v;

        Log.i(TAG, GameLogging.getCurrentGameState());

        mDrawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) v.findViewById(R.id.nav_view);
        mSummaryView = (NavigationView) v.findViewById(R.id.summary_view);
        mPlayerUsername = PresenterFacade.getInstance().getUsername();

        mDrawDestCardFragment = new DrawDestCardFragment();
        mDrawTrainCardFragment = new DrawTrainCardFragment();
        mClaimRouteFragment = new ClaimRouteFragment();

        setupActionButtons();

        Game game = PresenterFacade.getInstance().getGame();

        players = game.getPlayers();
        cities = game.getGameBoard().getCities();
        playerTurn = game.getCurrentPlayer();

//        magicButtonGuts = new MagicButton(getActivity());

//        disableButtonForEveryoneButCurrPlayer();


        playerTextViews = new TextView[5];
        playerTextViews[0] = v.findViewById(R.id.player_text_view_0);
        playerTextViews[1] = v.findViewById(R.id.player_text_view_1);
        playerTextViews[2] = v.findViewById(R.id.player_text_view_2);
        playerTextViews[3] = v.findViewById(R.id.player_text_view_3);
        playerTextViews[4] = v.findViewById(R.id.player_text_view_4);


        //place the MapDraw so it can draw the lines
        ConstraintLayout cl = v.findViewById(R.id.frameLayout2);
        routeDrawing = new MapDraw(getContext());
        cl.addView(routeDrawing);

        //get the screen size to scale the drawn lines on the canvas
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        screenRatio = width/1920.0f;



        //set player names of text views
        int playerCounter = 0;
        while(playerCounter < players.length){
            playerTextViews[playerCounter].setText(players[playerCounter].getUsername());
            playerTextViews[playerCounter].setBackgroundColor(players[playerCounter].getPlayerColor());
            playerCounter++;
        }
        while(playerCounter < playerTextViews.length){
            playerTextViews[playerCounter].setText("");
            playerCounter++;
        }

        playerNameBorder = new ShapeDrawable();
        playerNameBorder.setShape(new RectShape());
        playerNameBorder.getPaint().setStrokeWidth(5);
        playerNameBorder.getPaint().setStyle(Paint.Style.STROKE);


        return v;
    }

    private void setupActionButtons(){
        mDrawTrainCardButton = (Button) mView.findViewById(R.id.draw_train_card_button);
        mDrawDestCardButton = (Button) mView.findViewById(R.id.draw_dest_card_button);
        mClaimRouteButton = (Button) mView.findViewById(R.id.claim_route_button);
//        mMagicButton = (Button) mView.findViewById(R.id.magic_button);
        summaryButton = (Button) mView.findViewById(R.id.summary_button);

        mDrawTrainCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawTrainCardFragment fragment = new DrawTrainCardFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_view, mDrawTrainCardFragment).commit();
                mDrawerLayout.openDrawer(Gravity.START);
                MapPresenter.getInstance().signalOpenDrawer(MapPresenter.TRAIN);
                //Toast.makeText(getActivity(), "Popout DrawTrainCardDrawer", Toast.LENGTH_SHORT).show();
            }
        });

        mDrawDestCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawDestCardFragment fragment = new DrawDestCardFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_view, mDrawDestCardFragment).commit();
                mDrawerLayout.openDrawer(Gravity.START);
                MapPresenter.getInstance().signalOpenDrawer(MapPresenter.DESTINATION);
                //Toast.makeText(getActivity(), "Popout DrawDestCardDrawer", Toast.LENGTH_SHORT).show();
            }
        });


        mClaimRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClaimRouteFragment fragment = new ClaimRouteFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_view, mClaimRouteFragment).commit();
                mDrawerLayout.openDrawer(Gravity.START);
                MapPresenter.getInstance().signalOpenDrawer(MapPresenter.ROUTE);
                //Toast.makeText(getActivity(), "Popout claimRoute", Toast.LENGTH_SHORT).show();
            }
        });

//        mMagicButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                magicButtonGuts.start();
//            }
//        });

        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GameSummaryFragment summaryFragment = new GameSummaryFragment();
                FragmentTransaction summaryFragmentTransaction = getChildFragmentManager().beginTransaction();
                summaryFragmentTransaction.replace(R.id.summary_view, summaryFragment).commit();
                mDrawerLayout.openDrawer(Gravity.END);
//                Toast.makeText(getActivity(), "Popout Summary Card", Toast.LENGTH_SHORT).show();
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                MapPresenter.getInstance().signalClosedDrawer();
//                Toast.makeText(getContext(), "Drawer closed.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
    }

    /**
     * Disables action buttons for everyone but the currentPlayer
     */
    private void disableButtonForEveryoneButCurrPlayer(){
        if(this.playerTurn >= 0 && mPlayerUsername.equals(this.players[this.playerTurn].getUsername())){
            mDrawTrainCardButton.setEnabled(true);
            mDrawDestCardButton.setEnabled(true);
            mClaimRouteButton.setEnabled(true);
        } else {
            mDrawTrainCardButton.setEnabled(false);
            mDrawDestCardButton.setEnabled(false);
            mClaimRouteButton.setEnabled(false);
        }
    }



    @Override
    public void update() {
        Log.i(TAG, "Map Fragment Update Called.");
        routeDrawing.invalidate();
//        disableButtonForEveryoneButCurrPlayer();
    }



    @Override
    public void showErrorMessage(String errorMessage) {

    }

    public void updateLastTurn() {
        Toast.makeText(getContext(), "Last round!", Toast.LENGTH_LONG).show();
    }

    public void updatePlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
        //Toast.makeText(getContext(), "player turn updated to " + playerTurn, Toast.LENGTH_SHORT).show();

        Log.i(TAG, "player turn updated to " + playerTurn);

        //closes player's drawer when their turn starts
        if (players[playerTurn].getUsername().equals(ModelFacade.getInstance().getUsername())){
            Toast.makeText(getContext(), "It's your turn!", Toast.LENGTH_LONG).show();
            mDrawerLayout.closeDrawer(Gravity.START);
        }
        else{
            Toast.makeText(getContext(), "It is " + players[playerTurn].getUsername() + "'s turn.", Toast.LENGTH_LONG).show();
            mDrawerLayout.closeDrawer(Gravity.START);
        }

        //show active player
        if (playerTurn >= 0) {
            playerNameBorder.getPaint().setColor(players[playerTurn].getPlayerColor());

            //reset player name backgrounds
            for (int i = 0; i < players.length; i++) {
                playerTextViews[i].setBackgroundColor(players[i].getPlayerColor());
            }
            //give active player a border instead of fill
            playerTextViews[playerTurn].setBackground(playerNameBorder);

            update();
        }
    }

    public void updatePlayersForRoutes(Player[] players){
        this.players = players;
        update();
    }

    public DrawTrainCardFragment getDrawTrainCardFragment() {
        return mDrawTrainCardFragment;
    }

    public DrawDestCardFragment getDrawDestCardFragment() {
        return mDrawDestCardFragment;
    }

    public ClaimRouteFragment getClaimRouteFragment() {
        return mClaimRouteFragment;
    }


    private class MapDraw extends View{
        Paint paint;
        public MapDraw(Context context){
            super(context);

            paint = new Paint();
            paint.setStrokeWidth(6);
        }

        public void onDraw(Canvas canvas){

            for(int i = 0; i<players.length; i++){
                Player p = players[i];
                paint.setColor(p.getPlayerColor());
                ArrayList<Route> routes = p.getClaimedRoutes();
                for(Route route: routes) {
                    City startCity = findCityByName(route.getCity1());
                    City endCity = findCityByName(route.getCity2());
                    float startX = (startCity.getxCoordinate() + route.getxOffset())*screenRatio;
                    float startY = (startCity.getyCoordinate() + route.getyOffset())*screenRatio;
                    float endX = (endCity.getxCoordinate() + route.getxOffset())*screenRatio;
                    float endY = (endCity.getyCoordinate() + route.getyOffset())*screenRatio;

                    canvas.drawLine(startX, startY, endX, endY, paint);
                }
            }
        }

        public City findCityByName(String name){
            for(City city:cities){
                if(city.getName().equalsIgnoreCase(name)){
                    return city;
                }
            }
            return null;
        }
    }
}

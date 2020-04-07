package ateam.tickettoride.View.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ateam.tickettoride.Presenter.PresenterFacade;
import ateam.tickettoride.R;
import ateam.tickettoride.View.IView;
import ateam.tickettoride.common.map.Route;


public class ClaimRouteFragment extends Fragment implements IView{
    private static final String TAG = "ClaimRouteFragment";
    private static final String NO_ROUTE = "No Route Selected";

    private ArrayList<Route> mUnclaimedRoutes;
    private TextView mSelectedRouteTextView;
    private Button mClaimSelectedRouteButton;
    private RecyclerView mRouteRecyclerView;
    private View mView;
    private RouteInfoAdapter mAdapter;
    private Route mSelectedRoute;

    public ClaimRouteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PresenterFacade.getInstance().setClaimRoutePresenterActivity(getActivity());

        View v = inflater.inflate(R.layout.fragment_claim_route, container, false);
        mView = v;
        mRouteRecyclerView = (RecyclerView)v.findViewById(R.id.claim_route_recycler_view);
        mRouteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUnclaimedRoutes = PresenterFacade.getInstance().getUnclaimedRoutes();

        mSelectedRouteTextView = (TextView) v.findViewById(R.id.selected_route_text);
        mSelectedRouteTextView.setText(NO_ROUTE);

        mClaimSelectedRouteButton = (Button) v.findViewById(R.id.claim_route_button_frag);
        checkIfRouteSelected();

        mClaimSelectedRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedRoute.getColor() == 0xff808080){
                    FragmentManager manager = getChildFragmentManager();
                    TrainCardColorPickerFragment dialog = TrainCardColorPickerFragment.newInstance(mSelectedRoute);
                    dialog.show(manager, "Color Picker");
                }
                else{
                    PresenterFacade.getInstance().claimRoute(mSelectedRoute, mSelectedRoute.getColor());
                }
            }
        });

        updateUI();

        return v;
    }

    private void checkIfRouteSelected(){
        if(mSelectedRoute == null){
            mClaimSelectedRouteButton.setEnabled(false);
        }
        else{
            mClaimSelectedRouteButton.setEnabled(true);
        }
    }

    private void displaySelectedRouteInfo(){
        mSelectedRouteTextView.setText(mSelectedRoute.toString());
    }

    public void updateUnclaimedRoutes(ArrayList<Route> unclaimedRoutes){
        mUnclaimedRoutes = unclaimedRoutes;
        update();
    }

    /**
     * Updates UI to adjust to user scrolling through claimable routes
     */
    public void updateUI() {
        mAdapter = new ClaimRouteFragment.RouteInfoAdapter(mUnclaimedRoutes);
        mRouteRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void update() {
        updateUI();
    }

    @Override
    public void showErrorMessage(String errorMessage) {

    }

    /**
     * RouteInfoHolder class to hold information for each unclaimedRoute and to bind it to the corresponding view holder
     */
    private class RouteInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mRouteInfoView;
        private Route mRoute;
        private int selected_position = RecyclerView.NO_POSITION;

        public RouteInfoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.claim_route_list_item, parent, false));
            itemView.setOnClickListener(this);

            mRouteInfoView = (TextView) itemView.findViewById(R.id.route_holder);
        }

        public void bind(Route route){
            mRoute = route;

            mRouteInfoView.setText(mRoute.toString());
        }

        @Override
        public void onClick(View view){
            mSelectedRoute = mRoute;
            checkIfRouteSelected();
            displaySelectedRouteInfo();
            // Toast.makeText(getContext(), mRoute.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private class RouteInfoAdapter extends RecyclerView.Adapter<RouteInfoHolder> {
        private List<Route> mRouteInfo;

        public RouteInfoAdapter(List<Route> routes) {
            mRouteInfo = routes;
        }

        @NonNull
        @Override
        public RouteInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());

            return new RouteInfoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RouteInfoHolder holder, int position) {
            holder.bind(mRouteInfo.get(position));
        }

        @Override
        public int getItemCount() {
            return mRouteInfo.size();
        }
    }
}

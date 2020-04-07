package ateam.tickettoride.common.requests;

import ateam.tickettoride.common.card.TrainCard;
import ateam.tickettoride.common.map.Route;

public class ClaimRouteRequest extends Request {
    private String authToken;
    private String gameID;
    private Route route;
    private TrainCard[] cardsUsedToClaimRoute;

    public ClaimRouteRequest(String authToken, String gameID, Route route, TrainCard[] cardsUsedToClaimRoute) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.route = route;
        this.cardsUsedToClaimRoute = cardsUsedToClaimRoute;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public TrainCard[] getCardsUsedToClaimRoute() {
        return cardsUsedToClaimRoute;
    }

    public void setCardsUsedToClaimRoute(TrainCard[] cardsUsedToClaimRoute) {
        this.cardsUsedToClaimRoute = cardsUsedToClaimRoute;
    }
}

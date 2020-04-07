package ateam.tickettoride.common.graph;

import java.io.Serializable;

import ateam.tickettoride.common.map.Route;

//Class representing a route edge in a map graph
public class RouteEdge implements Serializable {
    //The route
    private Route actualRoute;
    //Flag for if the edge has been visited
    private boolean isVisited;

    public RouteEdge(Route actualRoute, boolean isVisited) {
        this.actualRoute = actualRoute;
        this.isVisited = isVisited;
    }

    public Route getActualRoute() {
        return actualRoute;
    }

    public void setActualRoute(Route actualRoute) {
        this.actualRoute = actualRoute;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}

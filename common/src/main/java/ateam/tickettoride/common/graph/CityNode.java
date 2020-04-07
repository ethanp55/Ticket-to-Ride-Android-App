package ateam.tickettoride.common.graph;

import java.io.Serializable;
import java.util.ArrayList;

import ateam.tickettoride.common.map.Route;

//Class representing a city node in a map graph
public class CityNode implements Serializable {
    //List of routes that are connected to the node (based on a player's claimed routes)
    private ArrayList<Route> connectedRoutes;
    //Name of the city
    private String cityName;
    //Flag for if the node has been visited or not
    private boolean isVisited;

    public CityNode(ArrayList<Route> connectedRoutes, String cityName, boolean isVisited) {
        this.connectedRoutes = connectedRoutes;
        this.cityName = cityName;
        this.isVisited = isVisited;
    }

    public ArrayList<Route> getConnectedRoutes() {
        return connectedRoutes;
    }

    public void setConnectedRoutes(ArrayList<Route> connectedRoutes) {
        this.connectedRoutes = connectedRoutes;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}

package ateam.tickettoride.common.graph;

import java.io.Serializable;
import java.util.ArrayList;

import ateam.tickettoride.common.card.DestinationCard;
import ateam.tickettoride.common.map.Route;

//Class representing a graph of cities and routes based on a player's claimed routes
//Used to facilitate the calculation of completed destination cards
public class MapGraph implements Serializable {
    //List of city nodes a player has
    private ArrayList<CityNode> cityNodes;
    //List of route edges a player has
    private ArrayList<RouteEdge> routeEdges;

    public MapGraph(ArrayList<CityNode> cityNodes, ArrayList<RouteEdge> routeEdges) {
        this.cityNodes = cityNodes;
        this.routeEdges = routeEdges;
    }

    public int calculateLongestPath(CityNode node) {
        return calculateLongestPathRecursion(0, node);
    }

    private int calculateLongestPathRecursion(int currentLength, CityNode node) {
        int currentEndLength;
        int maxLength = currentLength;

        for (Route route : node.getConnectedRoutes()) {
            RouteEdge routeEdge = findRouteEdgeFromRoute(route);
            if (!routeEdge.isVisited()) {
                routeEdge.setVisited(true);

                if (!route.getCity1().equals(node.getCityName())) {
                    currentEndLength = calculateLongestPathRecursion(currentLength + route.getRouteLength(), findNode(route.getCity1()));
                }

                else {
                    currentEndLength = calculateLongestPathRecursion(currentLength + route.getRouteLength(), findNode(route.getCity2()));
                }

                routeEdge.setVisited(false);

                if (currentEndLength >= maxLength) {
                    maxLength = currentEndLength;
                }
            }
         }

         return maxLength;
    }

    private RouteEdge findRouteEdgeFromRoute(Route route) {
        for (RouteEdge routeEdge : routeEdges) {
            if (routeEdge.getActualRoute().equals(route)) {
                return routeEdge;
            }
        }

        return null;
    }

    //Method for seeing if a destination card is completed
    public void checkIfDestinationIsCompleted(DestinationCard card) {
        //Find the node corresponding to the first city in the destination card
        CityNode node = findNode(card.getCity1());

        //If the node isn't null (the player claimed a route going to that city),
        // run a dfs starting with the node to determine if the card is finished or not
        if (node != null) {
            depthFirstSearch(node, card);
            resetCityNodes();
        }
    }

    private void depthFirstSearch(CityNode node, DestinationCard card) {
        //Mark the node as visited
        node.setVisited(true);

        for (Route route : node.getConnectedRoutes()) {
            //The city node that is adjacent to the current node (connected by the route)
            CityNode connectedNode;

            //Find the city node and make sure its not the same as the current node
            if (!route.getCity1().equals(node.getCityName())) {
                connectedNode = findNode(route.getCity1());
            }

            else {
                connectedNode = findNode(route.getCity2());
            }

            //If the connected node is the second city in the destination card, mark the card as complete and return (since
            // we started at the first city in the card, the second city is what we need to check for)
            if (connectedNode.getCityName().equals(card.getCity2())) {
                card.setCompleted(true);

                return;
            }

            //If the card isn't completed, continue with the depth first search
            //Check to see if the connected node hasn't been visited
            else if (!connectedNode.isVisited()) {
                depthFirstSearch(connectedNode, card);
            }
        }
    }

    //Helper method for resetting the visited flag for all of the city nodes in the graph
    private void resetCityNodes() {
        for (CityNode node : cityNodes) {
            node.setVisited(false);
        }
    }

    //Helper method for finding a city node based on the name of the city
    public CityNode findNode(String cityName) {
        for (CityNode node : cityNodes) {
            if (node.getCityName().equals(cityName)) {
                return node;
            }
        }

        //Return null if the node isn't in the map graph (the player hasn't claimed a route to that city)
        return null;
    }

    public ArrayList<CityNode> getCityNodes() {
        return cityNodes;
    }

    public void addCityNode(CityNode node){
        cityNodes.add(node);
    }

    public void addRouteEdge(RouteEdge edge){
        routeEdges.add(edge);
    }

    public void setCityNodes(ArrayList<CityNode> cityNodes) {
        this.cityNodes = cityNodes;
    }

    public ArrayList<RouteEdge> getRouteEdges() {
        return routeEdges;
    }

    public void setRouteEdges(ArrayList<RouteEdge> routeEdges) {
        this.routeEdges = routeEdges;
    }
}

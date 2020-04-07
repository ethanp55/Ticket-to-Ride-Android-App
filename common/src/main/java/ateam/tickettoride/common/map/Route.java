package ateam.tickettoride.common.map;

import java.io.Serializable;

import ateam.tickettoride.common.TrainColor;

public class Route implements Serializable {
    //The two cities the route goes between
    private String city1;
    private String city2;
    //Length of the route
    private int routeLength;
    //The amount of points the route is worth
    private int pointValue;
    //Color of train cards needed to claim the route
    private int color;
    //Offset value (if there are two routes between the same city -> for drawing/GUI purposes)
    private int xOffset;
    private int yOffset;
    //Username of the player who claims the route -> initialized to null until a player claims it
    private String usernameOfOwner;


    public Route(String city1, String city2, int routeLength, int color, int xOffset, int yOffset) {
        this.city1 = city1;
        this.city2 = city2;
        this.routeLength = routeLength;
        this.color = color;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.pointValue = calculatePointValue(routeLength);
        this.usernameOfOwner = null;
    }

    //Calculates the number of points a route is worth based on its length
    private int calculatePointValue(int routeLength) {
        if (routeLength == 1) {
            return 1;
        }

        else if (routeLength == 2) {
            return 2;
        }

        else if (routeLength == 3) {
            return 4;
        }

        else if (routeLength == 4) {
            return 7;
        }

        else if (routeLength == 5) {
            return 10;
        }

        else {
            return 15;
        }
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public int getRouteLength() {
        return routeLength;
    }

    public void setRouteLength(int routeLength) {
        this.routeLength = routeLength;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getxOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public String getUsernameOfOwner() {
        return usernameOfOwner;
    }

    public void setUsernameOfOwner(String usernameOfOwner) {
        this.usernameOfOwner = usernameOfOwner;
    }

    public String toString(){
        return city1 + " to " + city2 + " - " + routeLength + " " + TrainColor.getStringColor(color);
    }

    public String toStringCity(){
        return city1 + " to " + city2 + ".";
    }

    public boolean equals(Object o){
        if(!(o instanceof Route)){
            return false;
        }

        Route oRoute = (Route)o;

        if(this.city1.equals(oRoute.getCity1()) &&
                this.city2.equals(oRoute.getCity2()) &&
                this.color == oRoute.getColor() &&
                this.routeLength == oRoute.getRouteLength() &&
                this.pointValue == oRoute.getPointValue() &&
                this.xOffset == oRoute.getxOffset() &&
                this.yOffset == oRoute.getyOffset()){
            return true;
        }
        else {
            return false;
        }
    }
}

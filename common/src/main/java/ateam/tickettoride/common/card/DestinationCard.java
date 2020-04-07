package ateam.tickettoride.common.card;

import java.io.Serializable;

public class DestinationCard implements Serializable {
    //The two cities the destination card goes between
    private String city1;
    private String city2;
    //Number of points the card is worth
    private int pointValue;
    //An indicator used to determine if the destination card has been completed
    private boolean completed;

    public DestinationCard(String city1, String city2, int pointValue) {
        this.city1 = city1;
        this.city2 = city2;
        this.pointValue = pointValue;
        this.completed = false;
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

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof DestinationCard)){
            return false;
        }

        DestinationCard oCard = (DestinationCard)o;

        if(city1.equals(oCard.getCity1()) &&
                city2.equals(oCard.getCity2()) &&
                pointValue == oCard.getPointValue()){
            return true;
        }

        return false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Route: " + city1 + " to " + city2 + "\n");
        sb.append("Point Value = " + pointValue);
        return sb.toString();
    }

    public String toStringDest(){
        StringBuilder sb = new StringBuilder();

        sb.append(city1 + " to " + city2);
        return sb.toString();
    }


}

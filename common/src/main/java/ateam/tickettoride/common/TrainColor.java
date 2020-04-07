package ateam.tickettoride.common;

public class TrainColor {
    public static String getStringColor(int color){
        if (color == 0xff808080) {
            return "any";
        } else if (color == 0xffffffff) {
            return "white";
        } else if (color == 0xff0000ff) {
            return "blue";
        } else if (color == 0xff000000) {
            return "black";
        } else if (color == 0xffff0000) {
            return "red";
        } else if (color == 0xffffff00) {
            return "yellow";
        } else if (color == 0xff008000) {
            return "green";
        } else if (color == 0xff800080) {
            return "purple";
        } else {
            // Orange
            return "orange";
        }
    }
}

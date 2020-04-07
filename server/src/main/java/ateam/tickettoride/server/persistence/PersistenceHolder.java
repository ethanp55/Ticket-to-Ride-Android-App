package ateam.tickettoride.server.persistence;

import persistenceInterfaces.IFactory;

public class PersistenceHolder {
    private static IFactory factory = null;
    private static int numCommandsBetweenCheckpoints = 0;

    public static void setFactory(IFactory newFactory){
        factory = newFactory;
    }

    public static IFactory getFactory(){
        return factory;
    }

    public static void setNumCommandsBetweenCheckpoints(int num){
        numCommandsBetweenCheckpoints = num;
    }

    public static int getNumCommandsBetweenCheckpoints(){
        return numCommandsBetweenCheckpoints;
    }
}

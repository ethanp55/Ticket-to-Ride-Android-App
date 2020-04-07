package ateam.tickettoride.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.common.model.GameInfo;

/**
 * A singleton class which holds JoinedGame's information
 */
public class JoinedGameContainer extends Observable {
    private static JoinedGameContainer instance = null;
    private GameInfo joinedGameInfo;

    public static JoinedGameContainer getInstance(){
        if(instance == null){
            instance = new JoinedGameContainer();
        }
        return instance;
    }

    public void setJoinedGameInfo(GameInfo gameInfo){
        joinedGameInfo = gameInfo;
    }

    public GameInfo getJoinedGameInfo(){
        return joinedGameInfo;
    }

    /**
     * Updates the player names in the joined game and notifies any observers of the change
     * @param playerNames the new list of player names
     */
    public void setPlayerNames(List<String> playerNames){
        List<String> currentNames = Arrays.asList(joinedGameInfo.getPlayerNames());
        if(currentNames.hashCode() != playerNames.hashCode()){
            joinedGameInfo.setPlayerNames((String[])playerNames.toArray());
            setChanged();
            notifyObservers();
        }
    }

    public String getHost(){
        return joinedGameInfo.getHost();
    }

    public String[] getPlayerList(){
        return joinedGameInfo.getPlayerNames();
    }

    public int getMaxNumPlayers(){
        return joinedGameInfo.getMaxNumPlayers();
    }

    public int getCurrNumPlayers(){
        return joinedGameInfo.getNumPlayers();
    }

    public String getGameId(){
        return joinedGameInfo.getGameID();
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        super.deleteObserver(o);
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }

    @Override
    public void notifyObservers(Object arg) {
        super.notifyObservers(arg);
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }

    @Override
    protected synchronized void clearChanged() {
        super.clearChanged();
    }

    @Override
    public synchronized boolean hasChanged() {
        return super.hasChanged();
    }

    @Override
    public synchronized int countObservers() {
        return super.countObservers();
    }
}

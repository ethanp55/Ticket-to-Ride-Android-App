package ateam.tickettoride.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.common.model.GameInfo;

/**
 * A singleton class which holds GameBrowser object that holds the GameInfo objects to be displayed in the GameBrowser
 */
public class GameBrowserContainer extends Observable {
    private static GameBrowserContainer instance;
    private GameBrowser browser;

    public static GameBrowserContainer getInstance(){
        if(instance == null){
            instance = new GameBrowserContainer();
        }
        return instance;
    }

    private GameBrowserContainer(){
        browser = new GameBrowser();
    }

    /**
     * Sets the list of gameInfo objects in the games GameBrowser and notifies any observers of the change
     * @param games The list of games to be added to the browser
     */
    public void setGames(List<GameInfo> games){
        if(games.hashCode() != this.browser.getGames().hashCode()) {
            this.browser.setGames(games);
            setChanged();
            notifyObservers();
        }
    }

    public List<GameInfo> getGames() {
        return browser.getGames();
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

package ateam.tickettoride.server.ClientProxy;

import java.util.HashMap;

public class GameProxyContainer {
    //The GameProxyContainer holds a Map of GameProxies with gameID's as keys
    private static final GameProxyContainer ourInstance = new GameProxyContainer();
    private HashMap<String, GameProxy> proxyMap;

    public static GameProxyContainer getInstance() {
        return ourInstance;
    }

    private GameProxyContainer() {
        proxyMap = new HashMap<>();
    }

    public void insertProxy(String gameID, GameProxy proxy) {
        proxyMap.put(gameID, proxy);
    }

    public GameProxy getProxy(String gameID) {
        return proxyMap.get(gameID);
    }
}

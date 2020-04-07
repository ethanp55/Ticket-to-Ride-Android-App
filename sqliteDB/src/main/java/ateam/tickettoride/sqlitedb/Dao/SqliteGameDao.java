package ateam.tickettoride.sqlitedb.Dao;

import java.awt.Cursor;

import ateam.tickettoride.sqlitedb.Database;
import persistenceInterfaces.IGameDao;

public class SqliteGameDao implements IGameDao {
    private static SqliteGameDao instance;
    private Database db = Database.getInstance();

    public static SqliteGameDao getInstance() {
        if(instance == null) {
            instance = new SqliteGameDao();
        }
        return instance;
    }

    @Override
    public void addGame(String gameID, String serializedGame, String serializedGameProxy) {
        try {
            db.openConnection();
            db.addGame(gameID, serializedGame, serializedGameProxy);
            db.closeConnection(true);
        }
        catch(Exception ex) {
            System.out.println("Failed to add game");
        }
    }

    @Override
    public String[][] getGames() {
        String[][] retGames = null;
        try {
            db.openConnection();
            retGames = db.getGames();
            db.closeConnection(true);
        }
        catch(Exception ex) {
            System.out.println("Failed to get games");
        }
        return retGames;
    }

    @Override
    public void replace(String gameID, String serializedGame, String serializedGameProxy) {
        try {
            db.openConnection();
            db.removeGame(gameID);
            db.addGame(gameID, serializedGame, serializedGameProxy);
            db.closeConnection(true);
        }
        catch(Exception ex) {
            System.out.println("Failed to replace game");
        }
    }

    @Override
    public void clear() {
        try {
            db.openConnection();
            db.dropGame();
            db.createTables();
            db.closeConnection(true);
        }
        catch(Exception ex) {
            System.out.println("Failed to delete Game");
        }
    }
}

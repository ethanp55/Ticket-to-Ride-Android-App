package ateam.tickettoride.sqlitedb.Dao;

import ateam.tickettoride.sqlitedb.Database;
import persistenceInterfaces.IAuthTokenDao;

public class SqliteAuthTokenDao implements IAuthTokenDao {
    private static SqliteAuthTokenDao instance;
    private Database db = Database.getInstance();

    public static SqliteAuthTokenDao getInstance() {
        if(instance == null) {
            instance = new SqliteAuthTokenDao();
        }
        return instance;
    }

    @Override
    public void addToken(String username, String authToken) {
        try {
            db.openConnection();
            db.addAuthToken(authToken, username);
            db.closeConnection(true);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to add authToken");
        }
    }

    @Override
    public String getUsername(String authToken) {
        String retUsername = null;
        try {
            db.openConnection();
            retUsername = db.getUsernameAuthToken(authToken);
            db.closeConnection(true);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to get username");
        }
        return retUsername;
    }

    @Override
    public String getAuthToken(String username) {
        String retAuthToken = null;
        try {
            db.openConnection();
            retAuthToken = db.getUsernameAuthToken(username);
            db.closeConnection(true);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to get authToken");
        }
        return retAuthToken;
    }

    @Override
    public void clear() {
        try {
            db.openConnection();
            db.dropAuthToken();
            db.createTables();
            db.closeConnection(true);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to clear AuthToken");
        }
    }
}

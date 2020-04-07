package ateam.tickettoride.sqlitedb.Dao;

import ateam.tickettoride.sqlitedb.Database;
import persistenceInterfaces.IUserDao;

public class SqliteUserDao implements IUserDao {
    private static SqliteUserDao instance;
    private Database db = Database.getInstance();

    public static SqliteUserDao getInstance() {
        if(instance == null) {
            instance = new SqliteUserDao();
        }
        return instance;
    }

    @Override
    public boolean addUser(String username, String password) {
        try {
            db.openConnection();
            db.addUser(username, password);
            db.closeConnection(true);
            return true;
        }
        catch(Exception ex) {
            System.out.println("Failed to add user");
            return false;
        }
    }

    @Override
    public String getPassword(String username) {
        String retPass = null;
        try {
            db.openConnection();
            retPass = db.getPassword(username);
            db.closeConnection(true);
        }
        catch(Exception ex) {
            System.out.println("Failed to get password");
        }
        return retPass;
    }

    @Override
    public void clear() {
        try {
            db.openConnection();
            db.dropUser();
            db.createTables();
            db.closeConnection(true);
        }
        catch(Exception ex) {
            System.out.println("Failed to delete User");
        }
    }

    @Override
    public boolean checkPassword(String username, String password) {
        try {
            db.openConnection();
            String retPass = db.getPassword(username);
            db.closeConnection(true);
            if(retPass.equals(password)) {
                return true;
            }
            return false;
        }
        catch(Exception ex) {
            System.out.println("Failed to get password");
            return false;
        }
    }

    @Override
    public boolean checkUsername(String username) {
        try {
            db.openConnection();
            boolean found = db.getUsername(username);
            db.closeConnection(true);
            if(found) {
                return true;
            }
            return false;
        }
        catch(Exception ex) {
            System.out.println("Failed to get password");
            return false;
        }
    }
}

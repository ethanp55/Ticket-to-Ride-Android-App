package ateam.tickettoride.sqlitedb.Dao;

import ateam.tickettoride.sqlitedb.Database;
import persistenceInterfaces.ICommandDao;

public class SqliteCommandDao implements ICommandDao {
    private static SqliteCommandDao instance;
    private Database db = Database.getInstance();

    public static SqliteCommandDao getInstance() {
        if(instance == null) {
            instance = new SqliteCommandDao();
        }
        return instance;
    }

    @Override
    public String getCommands(String gameID) {
        String retCommandList = null;
        try {
          db.openConnection();
          retCommandList = db.getCommandList(gameID);
          db.closeConnection(true);
        }
        catch(Exception ex) {
            System.out.println("Failed to get commandList");
        }
        return retCommandList;
    }

    @Override
    public void replace(String gameID, String serializedCommands) {
        try {
            db.openConnection();
            db.removeCommandList(gameID);
            db.addCommandList(gameID, serializedCommands);
            db.closeConnection(true);
        }
        catch(Exception ex) {
            System.out.println("Failed to replace commandList");
        }
    }

    @Override
    public void clear() {
        try {
            db.openConnection();
            db.dropCommand();
            db.createTables();
            db.closeConnection(true);
        }
        catch(Exception ex) {
            System.out.println("Failed to delete Command");
        }
    }
}

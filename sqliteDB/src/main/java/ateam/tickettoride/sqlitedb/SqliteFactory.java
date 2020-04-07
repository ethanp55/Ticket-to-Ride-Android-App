package ateam.tickettoride.sqlitedb;

import ateam.tickettoride.sqlitedb.Dao.SqliteAuthTokenDao;
import ateam.tickettoride.sqlitedb.Dao.SqliteCommandDao;
import ateam.tickettoride.sqlitedb.Dao.SqliteGameDao;
import ateam.tickettoride.sqlitedb.Dao.SqliteUserDao;
import persistenceInterfaces.IAuthTokenDao;
import persistenceInterfaces.ICommandDao;
import persistenceInterfaces.IFactory;
import persistenceInterfaces.IGameDao;
import persistenceInterfaces.IUserDao;

public class SqliteFactory implements IFactory {
    private static SqliteFactory instance;
    private static Database SQLiteDatabase;

    public static SqliteFactory getInstance() {
        if(instance == null) {
            instance = new SqliteFactory();
        }
        return instance;
    }

    @Override
    public void initializePersistence() {

    }

    @Override
    public IUserDao getUserDao() {
        return SqliteUserDao.getInstance();
    }

    @Override
    public IAuthTokenDao getAuthTokenDao() {
        return SqliteAuthTokenDao.getInstance();
    }

    @Override
    public IGameDao getGameDao() {
        return SqliteGameDao.getInstance();
    }

    @Override
    public ICommandDao getCommandDao() {
        return SqliteCommandDao.getInstance();
    }
}

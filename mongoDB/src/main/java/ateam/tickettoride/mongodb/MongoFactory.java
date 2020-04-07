package ateam.tickettoride.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import ateam.tickettoride.mongodb.Dao.MongoAuthTokenDao;
import ateam.tickettoride.mongodb.Dao.MongoCommandDao;
import ateam.tickettoride.mongodb.Dao.MongoGameDao;
import ateam.tickettoride.mongodb.Dao.MongoUserDao;
import persistenceInterfaces.IAuthTokenDao;
import persistenceInterfaces.ICommandDao;
import persistenceInterfaces.IFactory;
import persistenceInterfaces.IGameDao;
import persistenceInterfaces.IUserDao;

// Good to know information
// Good Quick Start MongoDB Command Guide - http://mongodb.github.io/mongo-java-driver/3.9/driver/getting-started/quick-start/
// Downloading the Server - https://www.mongodb.com/download-center/community
// Setting up the Mongo Server - https://docs.mongodb.com/guides/server/install/

// Where to download the .jar files for MongoBD
// Core jar - https://oss.sonatype.org/content/repositories/releases/org/mongodb/mongodb-driver-core/3.9.1/
// Mongo driver jar - https://oss.sonatype.org/content/repositories/releases/org/mongodb/mongodb-driver/3.9.1/
// BSON jar - https://oss.sonatype.org/content/repositories/releases/org/mongodb/bson/3.9.1/

/**
 * A MongoDB Factory that can be used to interact with the 'ateam' Mongo database
 */
public class MongoFactory implements IFactory {
    private static MongoFactory instance;
    private static MongoDatabase sMongoDatabase;
    private static final String DB_NAME = "ateam";

    public static MongoFactory getInstance(){
        if(instance == null){
            instance = new MongoFactory();
        }
        return instance;
    }

    /**
     * Creates a MongoClient and initializes (or retrieves) database
     */
    @Override
    public void initializePersistence() {
        MongoClient mongoClient = MongoClients.create();
        sMongoDatabase = mongoClient.getDatabase(DB_NAME);
    }

    @Override
    public IUserDao getUserDao() {
        return MongoUserDao.getInstance();
    }

    @Override
    public IAuthTokenDao getAuthTokenDao() {
        return MongoAuthTokenDao.getInstance();
    }

    @Override
    public IGameDao getGameDao() {
        return MongoGameDao.getInstance();
    }

    @Override
    public ICommandDao getCommandDao() {
        return MongoCommandDao.getInstance();
    }

    public MongoDatabase getDB(){
        if(sMongoDatabase == null){
            initializePersistence();
        }
        return sMongoDatabase;
    }
}

package ateam.tickettoride.mongodb.Dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import ateam.tickettoride.mongodb.MongoFactory;
import persistenceInterfaces.IUserDao;

/**
 * A database access object to access the 'user' collection in the 'ateam' Mongo database
 */
public class MongoUserDao implements IUserDao {
    private static MongoUserDao instance;
    private static final String COLLECTION_NAME = "user";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    public static MongoUserDao getInstance(){
        if(instance == null){
            instance = new MongoUserDao();
        }
        return instance;
    }

    /**
     * Adds a user to the User Collection in the MongoDB
     * @param username Username of the user to add
     * @param password Password of the user to add
     * @return True if adding was successful, false otherwise
     */
    @Override
    public boolean addUser(String username, String password) {
        if(!checkUsername(username)){
            MongoDatabase db = MongoFactory.getInstance().getDB();
            MongoCollection<Document> coll =  db.getCollection(COLLECTION_NAME);

            coll.insertOne(toDocument(username, password));
            return true;
        }
        else{
            return false;
        }

    }

    /**
     * Queries the MongoDB database for the indicated user and returns their password
     * @param username Username of the password desired
     * @return password associated with this user, null if user could not be found
     */
    @Override
    public String getPassword(String username) {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> coll =  db.getCollection(COLLECTION_NAME);

        Document query = coll.find(eq(USERNAME_KEY, username)).first();
        if(query != null){
            return query.getString(PASSWORD_KEY);
        }
        else{
            return null;
        }
    }

    /**
     * Clears user collection in the Mongo database
     */
    @Override
    public void clear() {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> coll =  db.getCollection(COLLECTION_NAME);
        DeleteResult deleteMessage = coll.deleteMany(new Document());
        System.out.println("MongoDB user collection cleared. " + deleteMessage.getDeletedCount() + " document(s) deleted.");
    }

    /**
     * Checks if the username and password combination given is correct
     * @param username username to check against password
     * @param password password to check against username
     * @return true if username and password are correct, false otherwise
     */
    @Override
    public boolean checkPassword(String username, String password) {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> users =  db.getCollection(COLLECTION_NAME);

        Document query = users.find(eq(USERNAME_KEY, username)).first();
        if(query != null && password != null){
            return password.equals(query.getString(PASSWORD_KEY));
        }
        else{
            return false;
        }
    }

    @Override
    public boolean checkUsername(String username) {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> users =  db.getCollection(COLLECTION_NAME);

        Document query = users.find(eq(USERNAME_KEY, username)).first();

        if(query != null){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Converts username and password to Document form in preparation to be
     * added to the User collection of the MongoDB
     * @param username Username to be added
     * @param password Password to be added
     * @return A Document with the username and password inside
     */
    private Document toDocument(String username, String password){
        return new Document(USERNAME_KEY, username).append(PASSWORD_KEY, password);
    }
}

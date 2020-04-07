package ateam.tickettoride.mongodb.Dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import ateam.tickettoride.mongodb.MongoFactory;
import persistenceInterfaces.IAuthTokenDao;

/**
 * A database access object to access the 'auth' collection in the 'ateam' Mongo database
 */
public class MongoAuthTokenDao implements IAuthTokenDao {
    private static MongoAuthTokenDao instance;
    private static final String COLLECTION_NAME = "auth";
    private static final String USERNAME_KEY = "username";
    private static final String AUTH_TOKEN_KEY = "authToken";

    public static MongoAuthTokenDao getInstance(){
        if(instance == null){
            instance = new MongoAuthTokenDao();
        }
        return instance;
    }

    /**
     * Adds username, authtoken pair to auth collection in MongoDB database
     * @param username username to add to DB
     * @param authToken authToken to add to DB
     */
    @Override
    public void addToken(String username, String authToken) {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> coll =  db.getCollection(COLLECTION_NAME);

        coll.insertOne(toDocument(username, authToken));
    }

    /**
     * Gets the username from an authToken
     * @param authToken authToken for which a username is desired
     * @return the username associated with the authToken, null if no
     * username is found
     */
    @Override
    public String getUsername(String authToken) {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> users =  db.getCollection(COLLECTION_NAME);

        Document query = users.find(eq(AUTH_TOKEN_KEY, authToken)).first();
        if(query != null){
            return query.getString(USERNAME_KEY);
        }
        else{
            return null;
        }
    }

    /**
     * Gets the authtoken from a username
     * @param username username for which a authToken is desired
     * @return the authToken associated with the username, null if no
     * authToken is found
     */
    @Override
    public String getAuthToken(String username) {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> users =  db.getCollection(COLLECTION_NAME);

        Document query = users.find(eq(USERNAME_KEY, username)).first();
        if(query != null){
            return query.getString(AUTH_TOKEN_KEY);
        }
        else{
            return null;
        }
    }

    /**
     * Clears the auth collection in the Mongo database
     */
    @Override
    public void clear() {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> users =  db.getCollection(COLLECTION_NAME);
        DeleteResult deleteMessage = users.deleteMany(new Document());
        System.out.println("MongoDB authToken collection cleared. " + deleteMessage.getDeletedCount() + " document(s) deleted.");
    }

    /**
     * Transforms username and authToken into a document that can be added to the collection
     * @param username username to convert
     * @param authToken authToken to convert
     * @return a Document with the username and authToken inside
     */
    private Document toDocument(String username, String authToken){
        return new Document(USERNAME_KEY, username).append(AUTH_TOKEN_KEY, authToken);
    }
}

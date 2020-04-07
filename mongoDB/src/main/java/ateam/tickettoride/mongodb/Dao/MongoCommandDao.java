package ateam.tickettoride.mongodb.Dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import org.bson.Document;
import org.bson.conversions.Bson;

import ateam.tickettoride.mongodb.MongoFactory;
import persistenceInterfaces.ICommandDao;

/**
 * A database access object to access the 'command' collection in the 'ateam' Mongo database
 */
public class MongoCommandDao implements ICommandDao {
    private static MongoCommandDao instance;
    private static final String COLLECTION_NAME = "command";
    private static final String GAME_ID = "gameID";
    private static final String COMMANDS = "commands";

    public static MongoCommandDao getInstance(){
        if(instance == null){
            instance = new MongoCommandDao();
        }
        return instance;
    }

    /**
     * Gets all the stored commands for a particular game
     * @param gameID the GameID whose commands are desired
     * @return the serialized commands associated with the gameID
     */
    @Override
    public String getCommands(String gameID) {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> users =  db.getCollection(COLLECTION_NAME);

        Document query = users.find(eq(GAME_ID, gameID)).first();
        if(query != null){
            return query.getString(COMMANDS);
        }
        else{
            return null;
        }
    }

    /**
     * Replaces the commands associated with the gameID with the serializedCommands,
     * creates new entry if there is not a document matching the gameID
     * @param gameID the gameID whose commands are to be replaced
     * @param serializedCommands the commands to replace the old commands
     */
    @Override
    public void replace(String gameID, String serializedCommands) {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> coll =  db.getCollection(COLLECTION_NAME);

        if(gameID == null){
            System.out.println("GameID null, MongoCommandDao replace method");
        }
        else if(getCommands(gameID) == null){
            coll.insertOne(toDocument(gameID, serializedCommands));
        }
        else {
            Bson filter = eq(GAME_ID, gameID);
            Bson query = combine(set(COMMANDS, serializedCommands));

            coll.updateOne(filter, query);
        }

    }

    /**
     * Clears the command collection in the Mongo database
     */
    @Override
    public void clear() {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> coll =  db.getCollection(COLLECTION_NAME);
        DeleteResult deleteMessage = coll.deleteMany(new Document());
        System.out.println("MongoDB command collection cleared. " + deleteMessage.getDeletedCount() + " document(s) deleted.");
    }

    /**
     * Converts the gameID and serialized commands to Document form so they can be put into the database
     * @param gameID the gameID to be converted
     * @param serializedCommands the serialized commands to be converted
     * @return a Document with the gameID and serialized commands inside
     */
    private Document toDocument(String gameID, String serializedCommands){
        return new Document(GAME_ID, gameID).append(COMMANDS, serializedCommands);
    }
}

package ateam.tickettoride.mongodb.Dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import org.bson.Document;
import org.bson.conversions.Bson;

import ateam.tickettoride.mongodb.MongoFactory;
import persistenceInterfaces.IGameDao;

/**
 * A database access object to access the 'game' collection in the 'ateam' Mongo database
 */
public class MongoGameDao implements IGameDao {
    private static MongoGameDao instance;
    private static final String COLLECTION_NAME = "game";
    private static final String GAME_ID = "gameID";
    private static final String GAME = "game";
    private static final String GAME_PROXY = "game_proxy";


    public static MongoGameDao getInstance(){
        if(instance == null){
            instance = new MongoGameDao();
        }
        return instance;
    }

    /**
     * Adds a game to the game collection in the Mongo database
     * @param gameID the game ID of the game to add
     * @param serializedGame the serialized game information for the game to add
     * @param serializedGameProxy the serialized game proxy for the game to add
     */
    @Override
    public void addGame(String gameID, String serializedGame, String serializedGameProxy) {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> coll =  db.getCollection(COLLECTION_NAME);

        coll.insertOne(toDocument(gameID, serializedGame, serializedGameProxy));
    }

    /**
     * Returns the game and gameproxy information for all the games in the game collection
     * @return a two dimensional array of strings, column 1 is the serialized game, column
     * 2 is the serialized game proxy. Each row is a different game. Returns an empty array
     * if there is no game information in the collection
     */
    @Override
    public String[][] getGames() {
        String[][] game_info;
        int index = 0;

        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> coll =  db.getCollection(COLLECTION_NAME);

        game_info = new String[(int)coll.countDocuments()][2];

        MongoCursor<Document> cursor = coll.find().iterator();

        while (cursor.hasNext()) {
            Document doc = cursor.next();
            game_info[index][0] = doc.getString(GAME);
            game_info[index][1] = doc.getString(GAME_PROXY);
            index+=1;
        }
        return game_info;
    }

    /**
     * Replaces the serialized game and serialized proxy for the specified game (found from the gameID)
     * @param gameID the gameID to replace
     * @param serializedGame the serialized game information to replace the old game
     * @param serializedGameProxy the serialized game proxy to replace the old proxy
     */
    @Override
    public void replace(String gameID, String serializedGame, String serializedGameProxy) {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> coll =  db.getCollection(COLLECTION_NAME);
        Bson filter = eq(GAME_ID, gameID);
        Bson query = combine(set(GAME, serializedGame), set(GAME_PROXY, serializedGameProxy));

        coll.updateOne(filter, query);
    }

    /**
     * Clears the game collection in the Mongo database
     */
    @Override
    public void clear() {
        MongoDatabase db = MongoFactory.getInstance().getDB();
        MongoCollection<Document> coll =  db.getCollection(COLLECTION_NAME);
        DeleteResult deleteMessage = coll.deleteMany(new Document());
        System.out.println("MongoDB game collection cleared. " + deleteMessage.getDeletedCount() + " document(s) deleted.");
    }

    /**
     * Converts the game information in a Document so it can be stored in the Mongo database
     * @param gameID the gameID to convert
     * @param serializedGame the serialized game information to convert
     * @param serializedGameProxy the serialized game proxy to convert
     * @return a Document with the gameID, serializedGame, and serializedGameProxy in it
     */
    private Document toDocument(String gameID, String serializedGame, String serializedGameProxy){
        return new Document(GAME_ID, gameID).append(GAME, serializedGame).append(GAME_PROXY, serializedGameProxy);
    }
}

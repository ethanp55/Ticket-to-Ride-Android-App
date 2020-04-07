package ateam.tickettoride.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class for converting to and from JSON.
 */

public class Jsonifier {
    private static Gson gson = null;

    /**
     * Initializes the Gson object for the Jsonifier class.
     */
    public static void init(){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    /**
     * Converts a string of JSON to an object.
     * @param json  The String of JSON to be deserialized.
     * @param classOfT  The class of T.
     * @param <T>   The type of the desired object.
     * @return  An object of type T from the string. Returns null if something is awry.
     */
    public static <T> T fromJson(String json, Class<T> classOfT){
        if(gson == null){
            init();
        }
        return gson.fromJson(json, classOfT);
    }

    /**
     * Converts the desired object to a string of JSON.
     * @param src   The source object to be serialized.
     * @return  The String of JSON representing the object.
     */
    public static String toJson(Object src){
        if(gson == null){
            init();
        }
        return gson.toJson(src);
    }
}

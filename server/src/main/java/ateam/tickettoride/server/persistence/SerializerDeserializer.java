package ateam.tickettoride.server.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class SerializerDeserializer {
//    public static String serializeObject(Object object) {
//        String serializedObject = null;
//
//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//
//            objectOutputStream.writeObject(object);
//
//            objectOutputStream.close();
//
//            serializedObject = byteArrayOutputStream.toString();
//        }
//
//        catch (IOException ex) {
//            System.out.println("Cannot serialize the object.");
//        }
//
//        return serializedObject;
//    }
//
//    public static Object deserializeObject(String serializedObject) {
//        Object object = null;
//        System.out.println("Deserial1" + serializedObject);
//        System.out.println("Deserial2" + serializedObject.getBytes());
//        try {
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject.getBytes());
//            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//
//            object = objectInputStream.readObject();
//        }
//
//        catch (IOException ex) {
//            System.out.println("Cannot deserialize the object.");
//        }
//
//        catch (ClassNotFoundException ex) {
//            System.out.println("Cannot find class for deserialization");
//        }
//
//        return object;
//    }


    /** Read the object from Base64 string. */
    public static Object deserializeObject( String s ) {
        Object o = new Object();
        try {
            byte [] data = Base64.getDecoder().decode(s);
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(data));
            o  = ois.readObject();
            ois.close();
        }

        catch (IOException ex) {
            System.out.println("Cannot deserialize the object.");
        }

        catch (ClassNotFoundException ex) {
            System.out.println("Cannot find class for deserialization");
        }

        return o;
    }

    /** Write the object to a Base64 string. */
    public static String serializeObject( Serializable o ){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( o );
            oos.close();
        }
        catch (IOException ex) {
            System.out.println("Cannot deserialize the object.");
        }

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}

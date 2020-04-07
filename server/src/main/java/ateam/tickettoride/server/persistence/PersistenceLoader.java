package ateam.tickettoride.server.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Scanner;

import persistenceInterfaces.IFactory;

public class PersistenceLoader {
    private static final String CONFIG_FILE_LOC = "./jar_config.txt";

    /**
     * Static method for loading the persistence subsystem. Takes an alias for the
     * persistence subsystem to use, as well as the number of commands to use
     * between checkpoints of total game saves.
     * @param alias The name of the subsystem to use (the first string in the configuration file).
     * @param numCheckpointCommands The number of commands to use between checkpoints.
     * @return
     */
    public static boolean loadPersistence(String alias, int numCheckpointCommands){
        Scanner scanner = null;

        try {
            scanner = new Scanner(new File(CONFIG_FILE_LOC));
        }

        catch(FileNotFoundException e){
            System.out.println("Configuration file \"" + CONFIG_FILE_LOC + "\" is missing.");

            return false;
        }

        while (scanner.hasNext()){
            //grab the entire line
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);

            String foundAlias = lineScanner.next();

            //if it's the right alias
            if(foundAlias.equalsIgnoreCase(alias)){
                String factoryClassPath = lineScanner.next();

                ArrayList<String> jarPaths = new ArrayList<String>();

                while(lineScanner.hasNext()){
                    //grab all the .jars to load
                    jarPaths.add(lineScanner.next());
                }

                ArrayList<URL> urls = new ArrayList<URL>();

                //load up URL list
                for(String path: jarPaths){
                    File file = new File(path);

                    try {
                        urls.add(file.toURI().toURL());
                    }

                    catch(MalformedURLException e){
                        System.out.println("Malformed jar filepath URL in configuration file for Alias \""
                                + alias + "\": \"" + path + "\"");

                        return false;
                    }
                }

                System.out.println("urls has length " + urls.size());

                URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]));

                Class klass = null;

                try {
                    klass = Class.forName(factoryClassPath, true, loader);
                }

                catch(ClassNotFoundException e){
                    System.out.println("Could not find class \"" + factoryClassPath + "\"");

                    return false;
                }

                try {
                    Constructor constructor = klass.getDeclaredConstructor(null);
                    Method method = klass.getDeclaredMethod("getInstance");

                    if(method == null){
                        System.out.println("The method is null");
                    }

                    IFactory factory = (IFactory)method.invoke(null);

                    if(factory == null){
                        System.out.println("No factory returned from getInstance method.");
                        return false;
                    }

                    else{
                        PersistenceHolder.setFactory(factory);
                        PersistenceHolder.setNumCommandsBetweenCheckpoints(numCheckpointCommands);
                        factory.initializePersistence();
                        System.out.println("Persistence initialized.");

                        return true;
                    }
                }

                catch(NoSuchMethodException e){
                    System.out.println("Could not find a \"getInstance\" method for the class " + factoryClassPath);

                    return false;
                }

                catch(Exception e){
                    System.out.println("Could not get factory for persistence provider \"" + alias + "\"");
                    e.printStackTrace();

                    return false;
                }
            }
        }

        System.out.println("Could not find registration for the subsystem \"" + alias + "\" in the configuration file: "
                + CONFIG_FILE_LOC);

        return false;
    }
}

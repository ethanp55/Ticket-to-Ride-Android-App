package ateam.tickettoride.server.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import ateam.tickettoride.server.handler.CommandHandler;
import ateam.tickettoride.server.persistence.PersistenceHolder;
import ateam.tickettoride.server.persistence.PersistenceLoader;
import ateam.tickettoride.server.service.ServerFacade;


public class ServerCommunicator {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    public static void main(String[] args) {
        try {
            Class.forName("ateam.tickettoride.server.service.ServerFacade");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        //grab alias
        String persistenceAlias = args[0];
        //grab number of commands between checkpoints
        String numCommandsString = args[1];
        int numCommands = 0;

        try{
            numCommands = Integer.parseInt(numCommandsString);

            if(numCommands < 0){
                System.out.println("0 commands will be used between full saves.");
            }
        }

        catch(NumberFormatException e){
            System.out.println(numCommandsString + " is not an integer. 0 commands will be used between full saves.");
        }

        boolean loaded = PersistenceLoader.loadPersistence(persistenceAlias, numCommands);

        if(!loaded){
            System.out.println("Failed to load persistence subsystem. Exiting.");
            return;
        }

        //if there is another argument grab it to check for wipe
        if (args.length > 2){
            String lastString = args[2];

            if (lastString.equalsIgnoreCase("wipe")){
                System.out.println("Wiping persistent data...");

                //Have the DAOs clear out all their data
                PersistenceHolder.getFactory().getUserDao().clear();
                PersistenceHolder.getFactory().getAuthTokenDao().clear();
                PersistenceHolder.getFactory().getCommandDao().clear();
                PersistenceHolder.getFactory().getGameDao().clear();
            }
        }

        //Sync all the game data from the games and commands stored in the database (if needed)
        ServerFacade.getInstance().syncGameData();

        new ServerCommunicator().run("8080");
    }

    //Runs the server
    private void run(String portNumber) {
        System.out.println("Initializing HTTP Server");

        //Create the http server using the port number that was passed in
        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        //Create all of the contexts for the different html requests
        System.out.println("Creating contexts");

        server.createContext("/", new CommandHandler());

        System.out.println("Starting server");

        server.start();

        System.out.println("Server started");
    }
}
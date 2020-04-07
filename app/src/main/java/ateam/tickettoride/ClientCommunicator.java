package ateam.tickettoride;



import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import ateam.tickettoride.common.Command;
import ateam.tickettoride.common.Jsonifier;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;

/**
 * Class for network communications with the server.
 */
public class ClientCommunicator {
    private static ClientCommunicator singleCC;

    private String host;
    private int port;
    private int timeout = 6000;

    private ClientCommunicator(){
        port = 8080;
        host = "10.24.210.224";
    }

    public static ClientCommunicator getInstance(){
        if(singleCC == null){
            singleCC = new ClientCommunicator();
        }
        return singleCC;
    }

    /**
     * Method for HttpURLConnections that connects to the server.
     * Serializes the Command as the request body. classOfResponse is the type
     * of expected Response if everything works.
     * @param command The command to be send to the server.
     * @param classOfResponse The class of the response to return if successful
     * @return A Response from the server if it connected, or an ErrorResponse
     */
    // was public <T> Response connect(Command command, Class<T> classOfResponse) in 240 code
    //if this doesn't work, replace the line with the one above, and it should
    public Response connect(Command command, Class classOfResponse){
        String json = Jsonifier.toJson(command);
        try{
            //make the URL
            URL url = new URL("http://" + host + ":" + port + "/");
            //connect and set timout limit
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(timeout);

            //always post method because we always send a command
            conn.setRequestMethod("POST");
            //we want to write something
            conn.setDoOutput(true);

            //write the request body
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(json);
            osw.close();

            //the response code
            int code = conn.getResponseCode();
            try{
                //get Scanner for the response body
                Scanner scanner;
                //set up the scanner with the appropriate stream
                if(code == HttpURLConnection.HTTP_OK){
                    scanner = new Scanner(conn.getInputStream());
                }
                else{
                    scanner = new Scanner(conn.getErrorStream());
                }

                //scan in the response
                String responseBody = "";
                while(scanner.hasNextLine()){
                    String added = scanner.nextLine();
                    responseBody += added + "\n";
                }

                if(code == HttpURLConnection.HTTP_OK){
                    //if it's all good, give back what they ask for

                    return (Response)Jsonifier.fromJson(responseBody, classOfResponse);
                }
                else{//if something broke, send an error response
                    return Jsonifier.fromJson(responseBody, ErrorResponse.class);
                }
            }catch(IOException e){
                return new ErrorResponse("Something broke while hearing back from the server.");
            }

        }catch(MalformedURLException e){
            return new ErrorResponse("Malformed URL Exception: \n" + e.getMessage());
        }catch(IOException e){
            e.printStackTrace();
            return new ErrorResponse("Unable to connect with the server.");
        }
    }

    public void setIPAddress(String IPAddress){
        host = IPAddress;
    }
}

package ateam.tickettoride.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import ateam.tickettoride.common.Command;
import ateam.tickettoride.common.Jsonifier;
import ateam.tickettoride.common.responses.ErrorResponse;
import ateam.tickettoride.common.responses.Response;


public class CommandHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            //Decode the request body from JSON
            InputStream stream = exchange.getRequestBody();
            String jsonString = readString(stream);
            Command command = Jsonifier.fromJson(jsonString, Command.class);

            //Perform the command
            Object o = command.execute();
            Response response = (Response)o;

            //If there was an error while running the command, indicate so
            if (response.getClass().equals(ErrorResponse.class)) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

            //If there were no errors, indicate so
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }

            //Output the result
            OutputStream respBody = exchange.getResponseBody();

            String jsonResult = Jsonifier.toJson(response);
            writeString(jsonResult, respBody);

            respBody.close();
        }

        catch (IOException e) {
            System.out.println("Http error");
        }
    }

    //Helper function for writing the result of the service
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    //Helper function for reading a string from an input stream
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}

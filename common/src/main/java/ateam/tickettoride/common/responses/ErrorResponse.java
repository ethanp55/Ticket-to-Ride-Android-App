package ateam.tickettoride.common.responses;

/**
 * Class representing a response to a failed request.
 */

public class ErrorResponse extends Response {
    //The message for the client.
    private String message;

    public ErrorResponse(String theMessage){
        message = theMessage;
    }

    public String getMessage() {
        return message;
    }
}

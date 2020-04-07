package ateam.tickettoride.common.requests;

/**
 * Class representing a request to register.
 */

public class RegisterRequest extends Request {
    private String username;
    private String password;

    public RegisterRequest(String name, String pass){
        username = name;
        password = pass;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}

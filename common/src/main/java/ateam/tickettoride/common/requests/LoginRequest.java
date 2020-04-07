package ateam.tickettoride.common.requests;

/**
 * Class representing a request to login.
 */

public class LoginRequest extends Request {
    private String username;
    private String password;

    public LoginRequest(String name, String pass){
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

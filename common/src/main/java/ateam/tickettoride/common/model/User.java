package ateam.tickettoride.common.model;

/**
 * Class representing a user.
 */

public class User {
    //The user's username.
    private String username;
    //The user's password.
    private String password;
    //The user's authorization token.
    private String authToken;

    public User(){

    }

    public User(String name, String pass){
        username = name;
        password = pass;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

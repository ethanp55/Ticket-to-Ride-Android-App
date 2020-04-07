package ateam.tickettoride.Model;

import java.util.Observable;
import java.util.Observer;

import ateam.tickettoride.common.model.User;

/**
 * A singleton class to hold the logged in user's information, most commonly used for authentication purposes.
 */
public class UserInfoContainer {
    private static UserInfoContainer instance ;
    private User user;

    public static UserInfoContainer getInstance(){
        if(instance == null){
            instance = new UserInfoContainer();
        }
        return instance;
    }

    private UserInfoContainer() {
        user = new User();
    }

//    public User getUser() {
//        return user;
//    }

//    public void setUser(User user) {
//        this.user = user;
//
//        int hashes = user.getUsername().hashCode() + password.hashCode() + authToken.hashCode();
//
//        username = user.getUsername();
//        password = user.getPassword();
//        authToken = user.getAuthToken();
//
//        int newHashes = username.hashCode() + password.hashCode() + authToken.hashCode();
//
//        //if anything actually changed
//        if(hashes != newHashes){
//            notifyObservers();
//        }
//    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }

    public String getAuthToken() {
        return user.getAuthToken();
    }

    public void setAuthToken(String authToken) {
        user.setAuthToken(authToken);
    }
}

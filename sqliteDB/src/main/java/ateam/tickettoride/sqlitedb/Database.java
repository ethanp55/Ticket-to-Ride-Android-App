package ateam.tickettoride.sqlitedb;

import java.util.ArrayList;
import java.sql.*;
import java.lang.*;



//The Database class is the backbone of the Server.
//It creates, stores and acts on tables of data using methods called by DAOs
//The actual database itself is a static entity, so each instance of a Database object acts on the same set of data.
public class Database {
    //Creates the JDBC Driver
    private static Database instance;

    public static Database getInstance() {
        if(instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            openConnection();
            createTables();
            closeConnection(true);

            System.out.println("OK");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Connection conn;


    public void openConnection() throws Exception {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:ticket-to-ride.db";

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new Exception("openConnection failed", e);
        }
    }

    public void closeConnection(boolean commit) throws Exception {
        try {
            if (commit) {
                conn.commit();
            }
            else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        }
        catch (SQLException e) {
            throw new Exception("closeConnection failed", e);
        }
    }

    public void createTables() throws Exception {
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AuthToken (authTokenID TEXT NOT NULL PRIMARY KEY, username TEXT NOT NULL)");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS User (username TEXT NOT NULL PRIMARY KEY, password TEXT NOT NULL)");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Game (gameID TEXT NOT NULL PRIMARY KEY, game TEXT NOT NULL, gameProxy TEXT NOT NULL)");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Command (gameID TEXT NOT NULL PRIMARY KEY, commandList TEXT NOT NULL)");
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }
        catch (SQLException e) {
            throw new Exception("createTables failed", e);
        }
    }

    //The following are functions that are called by DAOs to operate on the tables
    //It is useful to think of each DAO as the table itself in the database
    //A DAO can add, query, delete and drop using the methods below

    public void addUser(String username, String password) throws Exception {
        String sql = "INSERT INTO User(username,password) VALUES(?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
    }

    public void removeUser(String username) throws Exception {
        String sql = "DELETE FROM User WHERE username = ?";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
    }

    //Returns null if username not found
    public String getPassword(String username) throws Exception {
        String sql = "SELECT password FROM User WHERE username = ?";

        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                String retPass = rs.getString(1);
                return retPass;
            }
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
        return null;
    }

    //Returns null if username not found
    public boolean getUsername(String username) throws Exception {
        String sql = "SELECT username FROM User WHERE username = ?";

        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return true;
            }
            return false;
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
    }

    public void dropUser() throws Exception {
        String sql = "DROP TABLE IF EXISTS User";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            return;
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
    }

    public void addGame(String gameID, String game, String gameProxy) throws Exception {
        String sql = "INSERT INTO Game(gameID,game,gameProxy) VALUES(?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gameID);
            pstmt.setString(2,game);
            pstmt.setString(3,gameProxy);
            pstmt.executeUpdate();
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
    }

    public void removeGame(String gameID) throws Exception {
        String sql = "DELETE FROM Game WHERE gameID = ?";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gameID);
            pstmt.executeUpdate();
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
    }

    //Returns a single game and game proxy corresponding to gameID
    public String[][] getGames() throws Exception{
        String sql = "SELECT game,gameProxy FROM Game";
        ArrayList<String[]> retArray = new ArrayList<>();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                String[] array = new String[2];
                array[0] = rs.getString(1);
                array[1] = rs.getString(2);
                retArray.add(array);
            }
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
        System.out.println("retArraySize: " + retArray.size());
        String[][] array1 = new String[retArray.size()][2];
        for(int i = 0; i < retArray.size(); i++) {
            array1[i][0] = retArray.get(i)[0];
            array1[i][1] = retArray.get(i)[1];

        }
        return array1;
    }

    public void dropGame() throws Exception {
        String sql = "DROP TABLE IF EXISTS Game";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            return;
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
    }

    public void addCommandList(String gameID, String commandList) throws Exception {
        String sql = "INSERT INTO Command(gameID,commandList) VALUES(?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gameID);
            pstmt.setString(2, commandList);
            pstmt.executeUpdate();
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
        return;
    }

    public void removeCommandList(String gameID) throws Exception {
        String sql = "DELETE FROM Command WHERE gameID = ?";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gameID);
            pstmt.executeUpdate();
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
    }

    public String getCommandList(String gameID) throws Exception {
        String sql = "SELECT commandList FROM Command WHERE gameID = ?";
        String retCommandList = null;

        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gameID);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                retCommandList = rs.getString(1);
                }
        }
        catch(SQLException ex) {
            closeConnection(false);
            throw ex;
        }
        return retCommandList;
    }

    //Drops Event table from database
    public void dropCommand() throws Exception {
        String sql = "DROP TABLE IF EXISTS Command";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            return;
        }
        catch(SQLException ex) {
            closeConnection(false);
            throw ex;
        }
    }

    public void addAuthToken(String authTokenID, String username) throws Exception {
        String sql = "INSERT INTO AuthToken(authTokenID,username) VALUES(?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, authTokenID);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
        catch(SQLException ex) {
            closeConnection(false);
            throw ex;
        }
        return;
    }

    public String getAuthToken(String username) throws Exception {
        String sql = "SELECT authTokenID FROM AuthToken WHERE username = ?";
        String retAuthToken = null;
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                retAuthToken = rs.getString(1);
            }
        }
        catch(SQLException ex) {
            closeConnection(false);
            throw ex;
        }
        return retAuthToken;
    }

    public String getUsernameAuthToken(String inToken) throws Exception {
        String sql = "SELECT username FROM AuthToken WHERE authTokenID = ?";
        String retUsername = null;
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, inToken);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                retUsername = rs.getString(1);
            }
            return retUsername;
        }
        catch(SQLException ex) {
            closeConnection(false);
            throw ex;
        }
    }

    public void dropAuthToken() throws Exception {
        String sql = "DROP TABLE IF EXISTS AuthToken";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            return;
        }
        catch(Exception ex) {
            closeConnection(false);
            throw new Exception();
        }
    }
}

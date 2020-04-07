package ateam.tickettoride.common.model;

/**
 * Class for representing basic information about a game to be displayed
 * in the Game Browser.
 */

public class GameInfo {
    //The current number of players in the game/lobby.
    private int numPlayers;
    //The maximum number of players allowed in the game.
    private int maxNumPlayers;
    //The unique ID for this game.
    private String gameID;
    //The name of the game.
    private String gameName;
    //The list of usernames of the players, with the host in index 0.
    private String[] playerNames;

    public GameInfo(String id, String name, int maxplayers){
        playerNames = new String[maxplayers];
        maxNumPlayers = maxplayers;
        gameID = id;
        gameName = name;
        numPlayers = 0;
    }

    /**
     * Attempts to add a player name to the list of usernames.
     * @param username The username of the player to add.
     * @return  True if the player was added, false otherwise.
     */
    public boolean addPlayerName(String username){
        System.out.println(playerNames.length);
        for(int i = 0; i<playerNames.length; i++){
            System.out.println(playerNames[i]);
            if(playerNames[i] == null){
                playerNames[i] = username;
                numPlayers++;
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to remove a player from the game information based on their username.
     * @param username  The name of the player to remove.
     * @return  True if the username is found and removed, false otherwise.
     */
    public boolean removePlayer(String username){
        for(int i = 0; i<playerNames.length; i++){
            if(playerNames[i].compareTo(username) == 0){
                //found the player, remove them
                playerNames[i] = null;
                numPlayers--;
                return true;
            }
        }
        //could not find the player
        return false;
    }

    public String getGameID() {
        return gameID;
    }

    public String getHost(){
        return playerNames[0];
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getMaxNumPlayers() {
        return maxNumPlayers;
    }

    public void setMaxNumPlayers(int maxNumPlayers) {
        this.maxNumPlayers = maxNumPlayers;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
        numPlayers = 0;
        for(int i = 0; i < playerNames.length; i++){
            if(playerNames[i] != null){
                numPlayers++;
            }
        }
    }
}

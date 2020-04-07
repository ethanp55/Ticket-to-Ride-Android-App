package persistenceInterfaces;

public interface IGameDao {
    public void addGame(String gameID, String serializedGame, String serializedGameProxy);
    public String[][] getGames();
    public void replace(String gameID, String serializedGame, String serializedGameProxy);
    public void clear();
}

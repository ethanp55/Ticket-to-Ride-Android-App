package persistenceInterfaces;

public interface ICommandDao {
    public String getCommands(String gameID);
    public void replace(String gameID, String serializedCommands);
    public void clear();
}

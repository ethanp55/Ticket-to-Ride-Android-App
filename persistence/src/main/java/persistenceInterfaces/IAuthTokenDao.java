package persistenceInterfaces;

public interface IAuthTokenDao {
    public void addToken(String username, String authToken);
    public String getUsername(String authToken);
    public String getAuthToken(String username);
    public void clear();
}

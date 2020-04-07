package persistenceInterfaces;

public interface IUserDao {
    public boolean addUser(String username, String password);
    public String getPassword(String username);
    public void clear();
    public boolean checkPassword(String username, String password);
    public boolean checkUsername(String username);
}

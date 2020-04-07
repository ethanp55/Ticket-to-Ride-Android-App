package persistenceInterfaces;

public interface IFactory {
    public void initializePersistence();
    public IUserDao getUserDao();
    public IAuthTokenDao getAuthTokenDao();
    public IGameDao getGameDao();
    public ICommandDao getCommandDao();
}

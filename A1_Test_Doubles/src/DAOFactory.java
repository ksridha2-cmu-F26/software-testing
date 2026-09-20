public class DAOFactory {
    private static final DAOFactory INSTANCE = new DAOFactory();
    private static IAccountDAO accountDAOImplementation = new AccountDAO("AccountDatabase");

    public static DAOFactory getInstance() {
        return INSTANCE;
    }

    public IAccountDAO getAccountDAO() {
        return accountDAOImplementation;
    }

    public IAccountDAO getAccountDAOFake() {
        return new AccountDAOFake();
    }

    public void setAccountDAOMock(IAccountDAO mockDAO) {
        accountDAOImplementation = mockDAO;
    }

    public IAccountDAO getAccountDAOMock() {
        return accountDAOImplementation;
    }

    public static boolean isMock(IAccountDAO dao) {
        return dao != null && !(dao instanceof AccountDAOFake) && !(dao instanceof AccountDAO);
    }
}

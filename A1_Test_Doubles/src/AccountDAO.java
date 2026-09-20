import java.util.HashSet;
import java.util.Set;

public class AccountDAO implements IAccountDAO {
    private static boolean connected;

    public AccountDAO(String dataBase) {
        connected = false;
    }

    public boolean isConnectedToDB() {
        return connected;
    }

    @Override
    public void save(Account member) {
    }

    @Override
    public Account findByUserName(String userName) {
        return null;
    }

    @Override
    public void delete(Account member) {
    }

    @Override
    public void update(Account member) {
    }

    @Override
    public Set<Account> findAll() {
        return new HashSet<Account>();
    }
}

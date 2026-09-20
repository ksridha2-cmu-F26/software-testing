import java.util.Set;

public interface IAccountDAO {
    void save(Account member);
    Account findByUserName(String userName);
    void delete(Account member);
    void update(Account member);
    Set<Account> findAll();
}

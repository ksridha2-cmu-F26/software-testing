import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AccountDAOFake implements IAccountDAO {
    private final Map<String, Account> records = new HashMap<String, Account>();

    public boolean isFullFake() {
        return true;
    }

    @Override
    public void save(Account member) {
        if (member != null) {
            records.put(member.getUserName(), member.clone());
        }
    }

    @Override
    public Account findByUserName(String userName) {
        Account member = records.get(userName);
        return member == null ? null : member.clone();
    }

    @Override
    public Set<Account> findAll() {
        Set<Account> result = new HashSet<Account>();
        for (Account member : records.values()) {
            result.add(member.clone());
        }
        return result;
    }

    @Override
    public void delete(Account member) {
        if (member != null) {
            records.remove(member.getUserName());
        }
    }

    @Override
    public void update(Account member) {
        save(member);
    }
}

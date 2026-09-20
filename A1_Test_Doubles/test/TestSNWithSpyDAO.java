import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class TestSNWithSpyDAO extends TestSNAbstractGeneric {
    @Override
    protected IAccountDAO createDAO() {
        return spy(new AccountDAOFake());
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void persistsNewAccount() throws Exception {
        Account newMember = sn.join("Gloria");
        verify(accountDAO).save(newMember);
    }

    @Test
    public void persistsFriendRequest() throws Exception {
        sn.login(m2);
        sn.sendFriendshipTo(m3.getUserName());
        verify(accountDAO, atLeastOnce()).update(any(Account.class));
    }

    @Test
    public void persistsAcceptance() throws Exception {
        sn.login(m2);
        sn.sendFriendshipTo(m3.getUserName());
        sn.login(m3);
        sn.acceptFriendshipFrom(m2.getUserName());
        verify(accountDAO, atLeastOnce()).update(any(Account.class));
    }

    @Test
    public void persistsRejection() throws Exception {
        sn.login(m2);
        sn.sendFriendshipTo(m3.getUserName());
        sn.login(m3);
        sn.rejectFriendshipFrom(m2.getUserName());
        verify(accountDAO, atLeastOnce()).update(any(Account.class));
    }

    @Test
    public void persistsBlocking() throws Exception {
        sn.login(m2);
        sn.block(m3.getUserName());
        verify(accountDAO, atLeastOnce()).update(any(Account.class));
    }

    @Test
    public void persistsLeaving() throws Exception {
        sn.login(m2);
        sn.leave();
        verify(accountDAO).delete(any(Account.class));
    }
}

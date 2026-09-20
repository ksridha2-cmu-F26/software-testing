import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class TestSNWithMockDAO extends TestSNAbstractGeneric {
    private AccountDAOFake backingStore;

    @Override
    protected IAccountDAO createDAO() {
        backingStore = new AccountDAOFake();
        IAccountDAO mockDAO = mock(IAccountDAO.class);
        doAnswer(invocation -> { backingStore.save(invocation.getArgument(0)); return null; }).when(mockDAO).save(any(Account.class));
        doAnswer(invocation -> { backingStore.update(invocation.getArgument(0)); return null; }).when(mockDAO).update(any(Account.class));
        doAnswer(invocation -> { backingStore.delete(invocation.getArgument(0)); return null; }).when(mockDAO).delete(any(Account.class));
        when(mockDAO.findByUserName(anyString())).thenAnswer(invocation -> backingStore.findByUserName(invocation.getArgument(0)));
        when(mockDAO.findAll()).thenAnswer(invocation -> backingStore.findAll());
        return mockDAO;
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
    public void persistsFriendRequestForBothAccounts() throws Exception {
        sn.login(m2);
        sn.sendFriendshipTo(m3.getUserName());
        verify(accountDAO, times(2)).update(any(Account.class));
    }

    @Test
    public void persistsAcceptanceForBothAccounts() throws Exception {
        sn.login(m2);
        sn.sendFriendshipTo(m3.getUserName());
        sn.login(m3);
        sn.acceptFriendshipFrom(m2.getUserName());
        verify(accountDAO, times(4)).update(any(Account.class));
    }

    @Test
    public void persistsRejectionForBothAccounts() throws Exception {
        sn.login(m2);
        sn.sendFriendshipTo(m3.getUserName());
        sn.login(m3);
        sn.rejectFriendshipFrom(m2.getUserName());
        verify(accountDAO, times(4)).update(any(Account.class));
    }

    @Test
    public void persistsBlockingChanges() throws Exception {
        sn.login(m2);
        sn.block(m3.getUserName());
        verify(accountDAO, times(2)).update(any(Account.class));
    }

    @Test
    public void persistsLeavingChangesAndDeletesAccount() throws Exception {
        sn.login(m2);
        sn.leave();
        verify(accountDAO).delete(any(Account.class));
    }
}

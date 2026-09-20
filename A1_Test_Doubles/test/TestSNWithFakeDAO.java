import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

public class TestSNWithFakeDAO extends TestSNAbstractGeneric {
    @Override
    protected IAccountDAO createDAO() {
        return new AccountDAOFake();
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test(expected = UserExistsException.class)
    public void persistedAccountCannotBeChangedThroughReturnedReference() throws Exception {
        Account surya = sn.join("Surya");
        Account stored = accountDAO.findByUserName("Surya");
        assertNotSame(surya, stored);
        surya.setUserName("Somebody Else");
        sn.join("Surya");
    }
}

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DAOTest {
    @Test
    public void realDAOIsDisconnectedUntilImplemented() {
        AccountDAO dao = new AccountDAO("AccountDatabase");
        assertFalse(dao.isConnectedToDB());
        assertTrue(dao.findAll().isEmpty());
        dao.save(null);
        dao.update(null);
        dao.delete(null);
    }

    @Test
    public void factoryCreatesAFullFake() {
        IAccountDAO dao = DAOFactory.getInstance().getAccountDAOFake();
        assertTrue(dao instanceof AccountDAOFake);
        assertTrue(((AccountDAOFake) dao).isFullFake());
        assertFalse(DAOFactory.isMock(dao));
    }
}
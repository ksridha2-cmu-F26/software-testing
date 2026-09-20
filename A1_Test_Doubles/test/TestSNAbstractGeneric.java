import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public abstract class TestSNAbstractGeneric {
    protected IAccountDAO accountDAO;
    protected SocialNetwork sn;
    protected Account m1, m2, m3, m4, m5;

    protected IAccountDAO createDAO() {
        return new AccountDAOFake();
    }

    @Before
    public void setUp() throws Exception {
        accountDAO = createDAO();
        sn = new SocialNetwork(accountDAO);
        m1 = sn.join("John");
        m2 = sn.join("Hakan");
        m3 = sn.join("Serra");
        m4 = sn.join("Dean");
        m5 = sn.join("Hasan");
    }

    @Test
    public void canJoinSocialNetwork() throws Exception {
        assertEquals("Gloria", sn.join("Gloria").getUserName());
    }

    @Test(expected = NoUserLoggedInException.class)
    public void mustLoginBeforeUsingSocialNetwork() throws Exception {
        sn.hasMember("Jane");
    }

    @Test
    public void canLoginAndFindYourself() throws Exception {
        sn.login(m1);
        assertTrue(sn.hasMember(m1.getUserName()));
    }

    @Test
    public void canListMembers() throws Exception {
        sn.login(m1);
        assertTrue(sn.listMembers().contains(m2.getUserName()));
        assertTrue(sn.listMembers().contains(m3.getUserName()));
    }

    @Test
    public void sendingFriendRequestCreatesPendingRequestAndResponse() throws Exception {
        sn.login(m2);
        sn.sendFriendshipTo(m3.getUserName());
        m2 = sn.login(m2);
        m3 = sn.login(m3);
        assertTrue(m3.getIncomingRequests().contains(m2.getUserName()));
        assertTrue(m2.getOutgoingRequests().contains(m3.getUserName()));
    }

    @Test
    public void acceptingFriendRequestCreatesFriendship() throws Exception {
        sn.login(m2);
        sn.sendFriendshipTo(m3.getUserName());
        m3 = sn.login(m3);
        sn.acceptFriendshipFrom(m2.getUserName());
        m2 = sn.login(m2);
        assertTrue(m3.getFriends().contains(m2.getUserName()));
        assertTrue(m2.getFriends().contains(m3.getUserName()));
    }

    @Test
    public void rejectingFriendRequestClearsPendingRequestAndResponse() throws Exception {
        sn.login(m2);
        sn.sendFriendshipTo(m3.getUserName());
        sn.login(m3);
        sn.rejectFriendshipFrom(m2.getUserName());
        assertFalse(m3.getIncomingRequests().contains(m2.getUserName()));
        assertFalse(sn.login(m2).getOutgoingRequests().contains(m3.getUserName()));
    }

    @Test(expected = UserNotFoundException.class)
    public void cannotSendFriendRequestToNonExistingMember() throws Exception {
        sn.login(m2);
        sn.sendFriendshipTo("Anonymous");
    }

    @Test(expected = UserExistsException.class)
    public void cannotJoinSocialNetworkAgain() throws Exception {
        sn.join("Hakan");
    }

    @Test
    public void blockingMakesUserInvisible() throws Exception {
        sn.login(m2);
        sn.block(m3.getUserName());
        sn.login(m3);
        assertFalse(sn.hasMember(m2.getUserName()));
        assertFalse(sn.listMembers().contains(m2.getUserName()));
    }

    @Test
    public void recommendMembersReturnsSharedFriends() throws Exception {
        sn.login(m1);
        sn.sendFriendshipTo(m2.getUserName());
        sn.sendFriendshipTo(m3.getUserName());
        sn.login(m2);
        sn.acceptFriendshipFrom(m1.getUserName());
        sn.sendFriendshipTo(m4.getUserName());
        sn.login(m3);
        sn.acceptFriendshipFrom(m1.getUserName());
        sn.sendFriendshipTo(m4.getUserName());
        sn.login(m4);
        sn.acceptFriendshipFrom(m2.getUserName());
        sn.acceptFriendshipFrom(m3.getUserName());
        sn.login(m1);
        Set<String> recommendations = sn.recommendFriends();
        assertEquals(new HashSet<String>(java.util.Collections.singleton(m4.getUserName())), recommendations);
    }

    @Test
    public void canLeaveSocialNetwork() throws Exception {
        sn.login(m2);
        sn.leave();
        sn.login(m3);
        assertFalse(sn.hasMember(m2.getUserName()));
    }

    @Test
    public void canUnblockMember() throws Exception {
        sn.login(m2);
        sn.block(m3.getUserName());
        sn.unblock(m3.getUserName());
        sn.login(m3);
        assertTrue(sn.hasMember(m2.getUserName()));
    }

    @Test
    public void canLogout() throws Exception {
        sn.login(m1);
        sn.logout();
        try {
            sn.listMembers();
        } catch (NoUserLoggedInException expected) {
            return;
        }
        throw new AssertionError("logout must clear the current user");
    }

    @Test
    public void accountEqualityAndCloningWork() {
        assertEquals(m2, m2.clone());
        assertFalse(m2.equals(m3));
        Account copy = m2.clone();
        m2.setUserName("Changed");
        assertEquals("Hakan", copy.getUserName());
    }
}

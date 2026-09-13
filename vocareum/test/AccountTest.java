
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AccountTest {

	Account me, her, another;

	@Before
	public void setUp() {
		me = new Account("Hakan");
		her = new Account("Serra");
		another = new Account("Cecile");
	}

	private void sendFriendRequest(Account from, Account to) {
		to.requestFriendship(from);
	}

	@Test
	public void requestFriendship_whenNotFriends_addsIncomingRequest() {
		sendFriendRequest(her, me);
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}

	@Test
	public void getIncomingRequests_whenNewAccount_returnsEmpty() {
		assertTrue(me.getIncomingRequests().isEmpty());
	}

	@Test
	public void requestFriendship_whenMultipleSenders_recordsAllIncomingRequests() {
		sendFriendRequest(her, me);
		sendFriendRequest(another, me);
		assertEquals(2, me.getIncomingRequests().size());
	}

	@Test
	public void requestFriendship_whenDuplicateRequest_keepsSingleIncomingRequest() {
		sendFriendRequest(her, me);
		sendFriendRequest(her, me);
		assertEquals(1, me.getIncomingRequests().size());
	}

	@Test
	public void friendshipAccepted_whenSingleRequest_addsFriendOnBothSides() {
		sendFriendRequest(her, me);
		her.friendshipAccepted(me);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
	}

	@Test
	public void friendshipRejected_whenIncomingRequestExists_clearsPendingRequests() {
		sendFriendRequest(her, me);
		her.friendshipRejected(me);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
		assertFalse(her.getOutgoingRequests().contains(me.getUserName()));
	}

	@Test
	public void cancelFriendship_whenFriends_removesFriendFromBothAccounts() {
		sendFriendRequest(her, me);
		her.friendshipAccepted(me);
		her.cancelFriendship(me);
		assertFalse(me.hasFriend(her.getUserName()));
		assertFalse(her.hasFriend(me.getUserName()));
	}

	@Test
	public void requestFriendship_whenAutoAcceptEnabled_createsFriendshipImmediately() {
		me.autoAcceptFriendships();
		sendFriendRequest(her, me);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
	}

	@Test
	public void cancelAutoAcceptFriendships_whenEnabled_stopsAutoAcceptance() {
		me.autoAcceptFriendships();
		me.cancelAutoAcceptFriendships();
		sendFriendRequest(her, me);
		assertFalse(me.hasFriend(her.getUserName()));
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}

	@Test
	public void block_whenMemberBlocked_recordsBlockedMember() {
		me.block("Serra");
		assertTrue(me.hasBlocked("Serra"));
	}

	@Test
	public void unblock_whenMemberWasBlocked_removesBlockedMember() {
		me.block("Serra");
		me.unblock("Serra");
		assertFalse(me.hasBlocked("Serra"));
	}

}

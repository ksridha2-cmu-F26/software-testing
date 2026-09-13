package edu.cmu;

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

	private void acceptFriendshipBetween(Account requester, Account accepter) {
		requester.friendshipAccepted(accepter);
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
	public void requestFriendship_whenSenderNotFriend_recordsOutgoingRequest() {
		sendFriendRequest(me, her);
		assertTrue(me.getOutgoingRequests().contains(her.getUserName()));
	}

	@Test
	public void friendshipAccepted_whenRequestWasSent_clearsOutgoingRequest() {
		sendFriendRequest(me, her);
		acceptFriendshipBetween(me, her);
		assertFalse(me.getOutgoingRequests().contains(her.getUserName()));
	}

	@Test
	public void friendshipAccepted_whenIncomingRequestExists_clearsIncomingRequest() {
		sendFriendRequest(her, me);
		acceptFriendshipBetween(her, me);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
	}

	@Test
	public void friendshipAccepted_whenIncomingRequestExists_clearsSenderOutgoingRequest() {
		sendFriendRequest(her, me);
		acceptFriendshipBetween(her, me);
		assertFalse(her.getOutgoingRequests().contains(me.getUserName()));
	}

	@Test
	public void friendshipAccepted_whenSingleRequest_addsFriendOnBothSides() {
		sendFriendRequest(her, me);
		acceptFriendshipBetween(her, me);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
	}

	@Test
	public void requestFriendship_whenAlreadyFriends_doesNotAddRequest() {
		sendFriendRequest(her, me);
		acceptFriendshipBetween(her, me);
		sendFriendRequest(her, me);
		assertTrue(me.getIncomingRequests().isEmpty());
		assertTrue(her.getIncomingRequests().isEmpty());
	}

	@Test
	public void friendshipRejected_whenIncomingRequestExists_clearsIncomingRequest() {
		sendFriendRequest(her, me);
		her.friendshipRejected(me);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
	}

	@Test
	public void friendshipRejected_whenIncomingRequestExists_clearsSenderOutgoingRequest() {
		sendFriendRequest(her, me);
		her.friendshipRejected(me);
		assertFalse(her.getOutgoingRequests().contains(me.getUserName()));
	}

	@Test
	public void friendshipRejected_whenIncomingRequestExists_doesNotCreateFriendship() {
		sendFriendRequest(her, me);
		her.friendshipRejected(me);
		assertFalse(me.hasFriend(her.getUserName()));
	}

	@Test
	public void cancelFriendship_whenFriends_removesFriendFromBothAccounts() {
		sendFriendRequest(her, me);
		acceptFriendshipBetween(her, me);
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
	public void requestFriendship_whenAutoAcceptEnabled_doesNotQueueIncomingRequest() {
		me.autoAcceptFriendships();
		sendFriendRequest(her, me);
		assertTrue(me.getIncomingRequests().isEmpty());
	}

	@Test
	public void requestFriendship_whenAutoAcceptEnabledAfterPending_keepsExistingPendingRequest() {
		sendFriendRequest(her, me);
		me.autoAcceptFriendships();
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
		assertFalse(me.hasFriend(her.getUserName()));
	}

}

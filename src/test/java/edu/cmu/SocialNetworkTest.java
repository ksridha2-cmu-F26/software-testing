package edu.cmu;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;


public class SocialNetworkTest {

	private static class Members {
		final SocialNetwork network;
		final Account me;
		final Account her;
		final Account another;

		Members(SocialNetwork network, Account me, Account her, Account another) {
			this.network = network;
			this.me = me;
			this.her = her;
			this.another = another;
		}
	}

	private Members createThreeMemberNetwork() {
		SocialNetwork network = new SocialNetwork();
		Account me = network.join("Hakan");
		Account her = network.join("Cecile");
		Account another = network.join("John");
		return new Members(network, me, her, another);
	}

	private Members createTwoMemberNetwork() {
		SocialNetwork network = new SocialNetwork();
		Account me = network.join("Hakan");
		Account her = network.join("Cecile");
		return new Members(network, me, her, null);
	}

	private void sendFriendRequest(SocialNetwork network, Account from, Account to) {
		network.sendFriendshipTo(to.getUserName(), from);
	}

	private void makeFriends(SocialNetwork network, Account requester, Account accepter) {
		sendFriendRequest(network, requester, accepter);
		network.acceptFriendshipFrom(requester.getUserName(), accepter);
	}

	@Test
	public void join_whenNewUser_returnsAccountWithUserName() {
		SocialNetwork network = new SocialNetwork();
		Account me = network.join("Hakan");
		assertEquals("Hakan", me.getUserName());
	}

	@Test
	public void listMembers_whenOneMemberJoins_containsMember() {
		SocialNetwork network = new SocialNetwork();
		network.join("Hakan");
		Collection<String> members = network.listMembers();
		assertEquals(1, members.size());
		assertTrue(members.contains("Hakan"));
	}

	@Test
	public void listMembers_whenTwoMembersJoin_listsBothMembers() {
		SocialNetwork network = new SocialNetwork();
		network.join("Hakan");
		network.join("Cecile");
		Collection<String> members = network.listMembers();
		assertEquals(2, members.size());
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}

	@Test
	public void sendFriendshipTo_whenNotFriends_addsIncomingRequest() {
		Members members = createTwoMemberNetwork();
		sendFriendRequest(members.network, members.me, members.her);
		assertTrue(members.her.getIncomingRequests().contains("Hakan"));
	}

	@Test
	public void acceptFriendshipFrom_whenPendingRequest_createsBidirectionalFriendship() {
		Members members = createTwoMemberNetwork();
		sendFriendRequest(members.network, members.me, members.her);
		members.network.acceptFriendshipFrom("Hakan", members.her);
		assertTrue(members.me.hasFriend("Cecile"));
		assertTrue(members.her.hasFriend("Hakan"));
	}

	@Test
	public void acceptFriendshipFrom_whenPendingRequest_clearsSenderOutgoingRequest() {
		Members members = createTwoMemberNetwork();
		sendFriendRequest(members.network, members.her, members.me);
		members.network.acceptFriendshipFrom("Cecile", members.me);
		assertFalse(members.her.getOutgoingRequests().contains("Hakan"));
	}

	@Test
	public void acceptAllFriendshipsTo_whenMultiplePending_clearsIncomingRequests() {
		Members members = createThreeMemberNetwork();
		sendFriendRequest(members.network, members.her, members.me);
		sendFriendRequest(members.network, members.another, members.me);
		members.network.acceptAllFriendshipsTo(members.me);
		assertTrue(members.me.getIncomingRequests().isEmpty());
	}

	@Test
	public void acceptAllFriendshipsTo_whenMultiplePending_createsAllFriendships() {
		Members members = createThreeMemberNetwork();
		sendFriendRequest(members.network, members.her, members.me);
		sendFriendRequest(members.network, members.another, members.me);
		members.network.acceptAllFriendshipsTo(members.me);
		assertTrue(members.me.hasFriend("Cecile"));
		assertTrue(members.me.hasFriend("John"));
	}

	@Test
	public void rejectFriendshipFrom_whenPendingRequest_clearsIncomingRequest() {
		Members members = createTwoMemberNetwork();
		sendFriendRequest(members.network, members.her, members.me);
		members.network.rejectFriendshipFrom("Cecile", members.me);
		assertFalse(members.me.getIncomingRequests().contains("Cecile"));
	}

	@Test
	public void rejectFriendshipFrom_whenPendingRequest_clearsSenderOutgoingRequest() {
		Members members = createTwoMemberNetwork();
		sendFriendRequest(members.network, members.her, members.me);
		members.network.rejectFriendshipFrom("Cecile", members.me);
		assertFalse(members.her.getOutgoingRequests().contains("Hakan"));
	}

	@Test
	public void rejectAllFriendshipsTo_whenMultiplePending_clearsAllIncomingRequests() {
		Members members = createThreeMemberNetwork();
		sendFriendRequest(members.network, members.her, members.me);
		sendFriendRequest(members.network, members.another, members.me);
		members.network.rejectAllFriendshipsTo(members.me);
		assertTrue(members.me.getIncomingRequests().isEmpty());
	}

	@Test
	public void rejectAllFriendshipsTo_whenMultiplePending_doesNotCreateFriendships() {
		Members members = createThreeMemberNetwork();
		sendFriendRequest(members.network, members.her, members.me);
		sendFriendRequest(members.network, members.another, members.me);
		members.network.rejectAllFriendshipsTo(members.me);
		assertFalse(members.me.hasFriend("Cecile"));
		assertFalse(members.me.hasFriend("John"));
	}

	@Test
	public void autoAcceptFriendshipsTo_whenNewRequestArrives_createsFriendshipImmediately() {
		Members members = createTwoMemberNetwork();
		members.network.autoAcceptFriendshipsTo(members.me);
		sendFriendRequest(members.network, members.her, members.me);
		assertTrue(members.me.hasFriend("Cecile"));
		assertTrue(members.her.hasFriend("Hakan"));
	}

	@Test
	public void autoAcceptFriendshipsTo_whenNewRequestArrives_doesNotQueueIncomingRequest() {
		Members members = createTwoMemberNetwork();
		members.network.autoAcceptFriendshipsTo(members.me);
		sendFriendRequest(members.network, members.her, members.me);
		assertTrue(members.me.getIncomingRequests().isEmpty());
	}

	@Test
	public void sendFriendshipCancellationTo_whenFriends_removesFriendshipFromBothAccounts() {
		Members members = createTwoMemberNetwork();
		makeFriends(members.network, members.me, members.her);
		members.network.sendFriendshipCancellationTo("Cecile", members.me);
		assertFalse(members.me.hasFriend("Cecile"));
		assertFalse(members.her.hasFriend("Hakan"));
	}

	@Test
	public void leave_whenMemberLeaves_excludesMemberFromListMembers() {
		Members members = createThreeMemberNetwork();
		members.network.leave(members.me);
		Collection<String> memberNames = members.network.listMembers();
		assertEquals(2, memberNames.size());
		assertFalse(memberNames.contains("Hakan"));
	}

	@Test
	public void leave_whenMemberWasFriend_clearsFriendshipOnOtherAccount() {
		Members members = createTwoMemberNetwork();
		makeFriends(members.network, members.me, members.her);
		members.network.leave(members.me);
		assertFalse(members.her.hasFriend("Hakan"));
	}

	@Test
	public void leave_whenMemberHadPendingIncoming_clearsPendingOnOtherAccount() {
		Members members = createTwoMemberNetwork();
		sendFriendRequest(members.network, members.her, members.me);
		members.network.leave(members.me);
		assertFalse(members.her.getOutgoingRequests().contains("Hakan"));
	}

}

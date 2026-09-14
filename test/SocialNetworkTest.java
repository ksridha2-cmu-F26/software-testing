
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SocialNetworkTest {

	SocialNetwork sn;
	Account me, her, another;

	// these are some example tests: you can merge them with your own tests from A0 
    
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test 
	public void canJoinSocialNetwork() throws Exception {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		assertEquals("Hakan", me.getUserName());
	}
	
	@Test 
	public void canListSingleMemberOfSocialNetworkAfterOnePersonJoiningAndSizeOfNetworkEqualsOne() throws Exception {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		sn.login(me);
		Set<String> members = sn.listMembers();
		assertEquals(1, members.size());
		assertTrue(members.contains("Hakan"));
	}
	
	@Test 
	public void twoPeopleCanJoinSocialNetworkAndSizeOfNetworkEqualsTwo() throws Exception {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		sn.join("Cecile");
		sn.login(me);
		Set<String> members = sn.listMembers();
		assertEquals(2, members.size());
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}
	
	@Test 
	public void sendAndAcceptFriendRequestToBecomeFriends() throws Exception {
		// test sending friend request
	    sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}
	
	@Test
	public void login_whenValidAccount_returnsLoggedInAccount() throws Exception {
		sn = new SocialNetwork();
		Account account = sn.join("Hakan");
		Account loggedIn = sn.login(account);
		assertNotNull(loggedIn);
		assertEquals("Hakan", loggedIn.getUserName());
	}

	@Test
	public void login_whenNullAccount_returnsNull() throws Exception {
		sn = new SocialNetwork();
		Account loggedIn = sn.login(null);
		assertNull(loggedIn);
	}

	@Test
	public void login_whenAccountNotInNetwork_returnsNull() throws Exception {
		sn = new SocialNetwork();
		Account account = new Account("Hakan");
		Account loggedIn = sn.login(account);
		assertNull(loggedIn);
	}

	@Test
	public void login_whenSwitchingAccounts_returnsNewAccount() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		
		// Login as Hakan
		Account first = sn.login(me);
		assertNotNull(first);
		assertEquals("Hakan", first.getUserName());
		
		// Switch to Cecile without logging out
		Account second = sn.login(her);
		assertNotNull(second);
		assertEquals("Cecile", second.getUserName());
	}

	@Test
	public void login_whenSameAccountTwice_returnsAccount() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		
		// Login first time
		Account first = sn.login(me);
		assertNotNull(first);
		
		// Login same account again
		Account second = sn.login(me);
		assertNotNull(second);
		assertEquals("Hakan", second.getUserName());
	}

	// ----- T3: hasMember -----

	@Test
	public void hasMember_whenMemberJoined_returnsTrue() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.join("Cecile");
		sn.login(me);
		assertTrue(sn.hasMember("Cecile"));
	}

	@Test
	public void hasMember_whenLoggedInMember_returnsTrue() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void hasMember_whenNeverJoined_returnsFalse() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertFalse(sn.hasMember("Ghost"));
	}

	@Test
	public void hasMember_whenNullUserName_returnsFalse() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertFalse(sn.hasMember(null));
	}

	// ----- T5: block -----

	@Test
	public void sendFriendshipTo_whenLoggedIn_addsIncomingRequest() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		assertTrue(her.getIncomingRequests().contains("Hakan"));
	}

	@Test
	public void hasMember_whenBlockedMemberLooksUpBlocker_returnsFalse() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.login(her);
		assertFalse(sn.hasMember("Hakan"));
	}

	@Test
	public void listMembers_whenBlockedMemberLoggedIn_excludesBlocker() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.login(her);
		assertFalse(sn.listMembers().contains("Hakan"));
		assertTrue(sn.listMembers().contains("Cecile"));
	}

	@Test
	public void hasMember_whenBlockerLooksUpBlockedMember_returnsTrue() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		assertTrue(sn.hasMember("Cecile"));
	}

	@Test
	public void hasMember_whenThirdMemberLooksUpBlocker_returnsTrue() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.join("Cecile");
		another = sn.join("Serra");
		sn.login(me);
		sn.block("Cecile");
		sn.login(another);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void sendFriendshipTo_whenBlockedMemberToBlocker_doesNotAddRequest() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		assertFalse(me.getIncomingRequests().contains("Cecile"));
	}

	@Test
	public void acceptAllFriendships_whenMultipleIncoming_becomesFriendsWithAll() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Cecile");
		Account hakan = sn.join("Hakan");
		Account serra = sn.join("Serra");

		sn.login(hakan);
		sn.sendFriendshipTo("Cecile");
		sn.login(serra);
		sn.sendFriendshipTo("Cecile");
		sn.login(me);
		sn.acceptAllFriendships();

		assertTrue(me.hasFriend("Hakan"));
		assertTrue(me.hasFriend("Serra"));
		assertTrue(me.getIncomingRequests().isEmpty());
	}

	@Test
	public void rejectAllFriendships_whenMultipleIncoming_rejectsAllRequests() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Cecile");
		Account hakan = sn.join("Hakan");
		Account serra = sn.join("Serra");

		sn.login(hakan);
		sn.sendFriendshipTo("Cecile");
		sn.login(serra);
		sn.sendFriendshipTo("Cecile");
		sn.login(me);
		sn.rejectAllFriendships();

		assertFalse(me.hasFriend("Hakan"));
		assertFalse(me.hasFriend("Serra"));
		assertTrue(me.getIncomingRequests().isEmpty());
		assertTrue(hakan.getOutgoingRequests().isEmpty());
		assertTrue(serra.getOutgoingRequests().isEmpty());
	}

	// ----- T6: unblock -----

	@Test
	public void hasMember_whenUnblockedMemberLooksUpBlocker_returnsTrue() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.unblock("Cecile");
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void listMembers_whenUnblockedMemberLoggedIn_includesBlocker() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.unblock("Cecile");
		sn.login(her);
		assertTrue(sn.listMembers().contains("Hakan"));
	}

	@Test
	public void sendFriendshipTo_whenUnblockedMemberToBlocker_addsIncomingRequest() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.unblock("Cecile");
		sn.login(her);
		sn.sendFriendshipTo("Hakan");
		assertTrue(me.getIncomingRequests().contains("Cecile"));
	}

	@Test
	public void unblock_whenOneMemberUnblocked_leavesOtherBlocksInPlace() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		another = sn.join("Serra");
		sn.login(me);
		sn.block("Cecile");
		sn.block("Serra");
		sn.unblock("Cecile");
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
		sn.login(another);
		assertFalse(sn.hasMember("Hakan"));
	}

	@Test
	public void unblock_whenMemberNeverBlocked_changesNothing() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.unblock("Cecile");
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void unblock_whenNullUserName_doesNothing() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		sn.unblock(null);
		assertTrue(sn.hasMember("Hakan"));
	}

	// ----- T3: hasMember, further cases -----

	/*
	@Test
	public void hasMember_whenEmptyNetwork_returnsFalse() throws Exception {
		sn = new SocialNetwork();
		assertFalse(sn.hasMember("Hakan"));
	}
	*/

	@Test
	public void hasMember_whenEmptyUserName_returnsFalse() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertFalse(sn.hasMember(""));
	}

	@Test
	public void hasMember_whenCaseMismatch_returnsFalse() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertTrue(sn.hasMember("Hakan"));
		assertFalse(sn.hasMember("hakan"));
		assertFalse(sn.hasMember("HAKAN"));
	}

	@Test
	public void hasMember_whenMemberJoinsAfterLogin_returnsTrue() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertFalse(sn.hasMember("Cecile"));
		sn.join("Cecile");
		assertTrue(sn.hasMember("Cecile"));
	}

	@Test
	public void hasMember_whenMemberHasLeft_returnsFalse() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		assertTrue(sn.hasMember("Cecile"));
		sn.leave(her);
		assertFalse(sn.hasMember("Cecile"));
	}

	@Test
	public void hasMember_whenLargerNetwork_returnsTrueForAllMembers() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.join("Cecile");
		sn.join("Serra");
		sn.login(me);
		assertTrue(sn.hasMember("Hakan"));
		assertTrue(sn.hasMember("Cecile"));
		assertTrue(sn.hasMember("Serra"));
		assertFalse(sn.hasMember("Ghost"));
	}

	// ----- T5: block, further cases -----

	@Test
	public void block_whenSameMemberTwice_isIdempotent() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.block("Cecile");
		sn.login(her);
		assertFalse(sn.hasMember("Hakan"));
	}

	@Test
	public void block_whenNullUserName_doesNothing() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block(null);
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void block_whenNotAMember_doesNothing() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Ghost");
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void hasMember_whenThirdMemberLooksUpBlockedMember_returnsTrue() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.join("Cecile");
		another = sn.join("Serra");
		sn.login(me);
		sn.block("Cecile");
		sn.login(another);
		assertTrue(sn.hasMember("Cecile"));
		assertTrue(sn.listMembers().contains("Cecile"));
	}

	@Test
	public void listMembers_whenBlockedMemberLoggedIn_excludesBlockerOnly() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		another = sn.join("Serra");
		sn.login(me);
		sn.block("Cecile");
		sn.login(her);
		Set<String> visible = sn.listMembers();
		assertFalse(visible.contains("Hakan"));
		assertTrue(visible.contains("Cecile"));
		assertTrue(visible.contains("Serra"));
	}

	@Test
	public void hasMember_whenBlockSelf_returnsTrue() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		sn.block("Hakan");
		assertTrue(sn.hasMember("Hakan"));
		assertTrue(sn.listMembers().contains("Hakan"));
	}

	@Test
	public void block_whenMutualBlock_hidesEachOther() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.login(her);
		sn.block("Hakan");
		assertFalse(sn.hasMember("Hakan"));
		sn.login(me);
		assertFalse(sn.hasMember("Cecile"));
	}

	// ----- T6: unblock, further cases -----

	@Test
	public void unblock_whenSameMemberTwice_isIdempotent() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.unblock("Cecile");
		sn.unblock("Cecile");
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void unblock_whenNotAMember_doesNothing() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.unblock("Ghost");
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void block_whenAfterUnblock_hidesBlockerAgain() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.unblock("Cecile");
		sn.block("Cecile");
		sn.login(her);
		assertFalse(sn.hasMember("Hakan"));
	}

	@Test
	public void unblock_whenOnlyOneMemberUnblocks_leavesOtherBlockInPlace() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		another = sn.join("Serra");
		sn.login(me);
		sn.block("Serra");
		sn.login(her);
		sn.block("Serra");
		sn.unblock("Serra");
		sn.login(another);
		assertTrue(sn.hasMember("Cecile"));
		assertFalse(sn.hasMember("Hakan"));
	}

	@Test
	public void cancelAutoAcceptFriendships_whenEnabled_stopsFutureAutoAcceptance() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		another = sn.join("Serra");
		sn.login(me);
		sn.autoAcceptFriendships();
		sn.sendFriendshipTo("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertEquals(0, me.getIncomingRequests().size());
		sn.cancelAutoAcceptFriendships();
		sn.sendFriendshipTo("Hakan", another);
		assertFalse(me.hasFriend("Serra"));
		assertEquals(1, me.getIncomingRequests().size());
		assertTrue(me.getIncomingRequests().contains("Serra"));
	}

	@Test
	public void cancelAutoAcceptFriendships_whenEnabled_requiresExplicitAcceptance() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.autoAcceptFriendships();
		sn.cancelAutoAcceptFriendships();
		sn.sendFriendshipTo("Hakan", her);
		assertTrue(me.getIncomingRequests().contains("Cecile"));
		sn.acceptFriendshipFrom("Cecile", me);
		assertTrue(me.hasFriend("Cecile"));
		assertEquals(0, me.getIncomingRequests().size());
	}

	@Test
	public void cancelAutoAcceptFriendships_whenNotEnabled_doesNothing() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		sn.cancelAutoAcceptFriendships();
		her = sn.join("Cecile");
		sn.sendFriendshipTo("Hakan", her);
		assertFalse(me.hasFriend("Cecile"));
		assertEquals(1, me.getIncomingRequests().size());
	}

	@Test
	public void autoAcceptFriendships_whenToggledMultipleTimes_togglesBehavior() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		another = sn.join("Serra");
		sn.login(me);
		sn.autoAcceptFriendships();
		sn.sendFriendshipTo("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		sn.cancelAutoAcceptFriendships();
		sn.sendFriendshipTo("Hakan", another);
		assertFalse(me.hasFriend("Serra"));
		assertEquals(1, me.getIncomingRequests().size());
	}

}

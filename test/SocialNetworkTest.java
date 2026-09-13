
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
	public void canLoginAfterJoining() throws Exception {
		sn = new SocialNetwork();
		Account account = sn.join("Hakan");
		Account loggedIn = sn.login(account);
		assertNotNull(loggedIn);
		assertEquals("Hakan", loggedIn.getUserName());
	}

	@Test
	public void loginReturnsNullForNullAccount() throws Exception {
		sn = new SocialNetwork();
		Account loggedIn = sn.login(null);
		assertNull(loggedIn);
	}

	@Test
	public void cannotLoginWithAccountNotInNetwork() throws Exception {
		sn = new SocialNetwork();
		Account account = new Account("Hakan");
		Account loggedIn = sn.login(account);
		assertNull(loggedIn);
	}

	@Test
	public void canSwitchAccountsWithoutLoggingOut() throws Exception {
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
	public void loginMultipleTimes() throws Exception {
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
	public void hasMemberIsTrueForAMemberWhoJoined() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.join("Cecile");
		sn.login(me);
		assertTrue(sn.hasMember("Cecile"));
	}

	@Test
	public void hasMemberIsTrueForTheLoggedInMemberHerself() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void hasMemberIsFalseForSomeoneWhoNeverJoined() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertFalse(sn.hasMember("Ghost"));
	}

	@Test
	public void hasMemberIsFalseForNullUserName() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertFalse(sn.hasMember(null));
	}

	// ----- T5: block -----

	@Test
	public void loggedInMemberCanSendAFriendRequestWithTheNewApi() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		assertTrue(her.getIncomingRequests().contains("Hakan"));
	}

	@Test
	public void aBlockedMemberCannotSeeTheBlocker() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		sn.login(her);
		assertFalse(sn.hasMember("Hakan"));
	}

	@Test
	public void aBlockedMemberDoesNotSeeTheBlockerInTheMemberList() throws Exception {
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
	public void theBlockerCanStillSeeTheBlockedMember() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.join("Cecile");
		sn.login(me);
		sn.block("Cecile");
		assertTrue(sn.hasMember("Cecile"));
	}

	@Test
	public void blockingDoesNotAffectOtherMembers() throws Exception {
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
	public void aBlockedMemberCannotSendAFriendRequestToTheBlocker() throws Exception {
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
	public void aMemberShouldBeAbleToAcceptAllFriendRequestsAtOnce() throws Exception {
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
	public void aMemberShouldBeAbleToRejectAllFriendRequestsAtOnce() throws Exception {
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

	// ----- T7: recommendFriends -----
	private void makeFriends(Account a, Account b) {
		a.requestFriendship(b);
		b.friendshipAccepted(a);
	}

	@Test
	public void recommendsAMemberWhoIsFriendsWithTwoOfMyFriends() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account bob = sn.join("Bob");
		Account carol = sn.join("Carol");
		makeFriends(me, alice);
		makeFriends(me, bob);
		makeFriends(carol, alice);
		makeFriends(carol, bob);
		sn.login(me);
		assertTrue(sn.recommendFriends().contains("Carol"));
	}

	@Test
	public void doesNotRecommendAMemberWithOnlyOneFriendInCommon() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account bob = sn.join("Bob");
		Account dave = sn.join("Dave");
		makeFriends(me, alice);
		makeFriends(me, bob);
		makeFriends(dave, alice);
		sn.login(me);
		assertFalse(sn.recommendFriends().contains("Dave"));
	}

	@Test
	public void doesNotRecommendMembersWhoAreAlreadyMyFriends() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account bob = sn.join("Bob");
		makeFriends(me, alice);
		makeFriends(me, bob);
		makeFriends(alice, bob);
		sn.login(me);
		assertFalse(sn.recommendFriends().contains("Alice"));
		assertFalse(sn.recommendFriends().contains("Bob"));
	}

	@Test
	public void doesNotRecommendTheLoggedInMemberHerself() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account bob = sn.join("Bob");
		makeFriends(me, alice);
		makeFriends(me, bob);
		makeFriends(alice, bob);
		sn.login(me);
		assertFalse(sn.recommendFriends().contains("Hakan"));
	}

	@Test
	public void doesNotRecommendAMemberBlockedByTheLoggedInUser() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account bob = sn.join("Bob");
		Account carol = sn.join("Carol");
		makeFriends(me, alice);
		makeFriends(me, bob);
		makeFriends(carol, alice);
		makeFriends(carol, bob);
		sn.login(me);
		sn.block("Carol");
		assertFalse(sn.recommendFriends().contains("Carol"));
	}

	@Test
	public void recommendsNobodyWhenTheLoggedInMemberHasNoFriends() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.join("Alice");
		sn.login(me);
		assertTrue(sn.recommendFriends().isEmpty());
	}

	// ----- T6: unblock -----

	@Test
	public void unblockingMakesTheBlockerVisibleAgain() throws Exception {
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
	public void anUnblockedMemberSeesTheBlockerInTheMemberListAgain() throws Exception {
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
	public void anUnblockedMemberCanBefriendTheBlockerAgain() throws Exception {
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
	public void unblockingOneMemberLeavesOtherBlocksInPlace() throws Exception {
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
	public void unblockingAMemberWhoWasNeverBlockedChangesNothing() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.unblock("Cecile");
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void unblockingANullUserNameIsHarmless() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		sn.unblock(null);
		assertTrue(sn.hasMember("Hakan"));
	}

	// ----- T3: hasMember, further cases -----

	/*
	@Test
	public void hasMemberIsFalseForEveryNameOnAnEmptyNetwork() throws Exception {
		sn = new SocialNetwork();
		assertFalse(sn.hasMember("Hakan"));
	}
	*/

	@Test
	public void hasMemberIsFalseForAnEmptyUserName() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertFalse(sn.hasMember(""));
	}

	@Test
	public void hasMemberIsCaseSensitive() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertTrue(sn.hasMember("Hakan"));
		assertFalse(sn.hasMember("hakan"));
		assertFalse(sn.hasMember("HAKAN"));
	}

	@Test
	public void hasMemberSeesSomebodyWhoJoinsAfterLogin() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertFalse(sn.hasMember("Cecile"));
		sn.join("Cecile");
		assertTrue(sn.hasMember("Cecile"));
	}

	@Test
	public void hasMemberIsFalseOnceThatMemberHasLeft() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		assertTrue(sn.hasMember("Cecile"));
		sn.leave(her);
		assertFalse(sn.hasMember("Cecile"));
	}

	@Test
	public void hasMemberIsTrueForEveryMemberOfALargerNetwork() throws Exception {
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
	public void blockingTheSameMemberTwiceIsIdempotent() throws Exception {
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
	public void blockingANullUserNameIsHarmless() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block(null);
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void blockingSomeoneWhoIsNotAMemberIsHarmless() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.block("Ghost");
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void aBlockedMemberIsStillVisibleToEveryoneElse() throws Exception {
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
	public void blockingHidesTheBlockerFromTheBlockedMemberOnly() throws Exception {
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
	public void blockingYourselfDoesNotMakeYouInvisibleToYourself() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		sn.block("Hakan");
		assertTrue(sn.hasMember("Hakan"));
		assertTrue(sn.listMembers().contains("Hakan"));
	}

	@Test
	public void twoMembersCanBlockEachOther() throws Exception {
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
	public void unblockingTwiceIsHarmless() throws Exception {
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
	public void unblockingSomeoneWhoIsNotAMemberIsHarmless() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.unblock("Ghost");
		sn.login(her);
		assertTrue(sn.hasMember("Hakan"));
	}

	@Test
	public void aMemberCanBeBlockedAgainAfterBeingUnblocked() throws Exception {
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
	public void unblockingOnlyAffectsTheMemberWhoIssuedTheBlock() throws Exception {
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

	// ----- T7: recommendFriends, further cases -----

	@Test
	public void recommendFriendsNeverReturnsNull() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
		assertNotNull(sn.recommendFriends());
	}

	@Test
	public void recommendsSeveralCandidatesAtOnce() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account bob = sn.join("Bob");
		Account carol = sn.join("Carol");
		Account dan = sn.join("Dan");
		makeFriends(me, alice);
		makeFriends(me, bob);
		makeFriends(carol, alice);
		makeFriends(carol, bob);
		makeFriends(dan, alice);
		makeFriends(dan, bob);
		sn.login(me);
		Set<String> recommended = sn.recommendFriends();
		assertTrue(recommended.contains("Carol"));
		assertTrue(recommended.contains("Dan"));
		assertEquals(2, recommended.size());
	}

	@Test
	public void recommendsAMemberSharingThreeFriends() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account bob = sn.join("Bob");
		Account eve = sn.join("Eve");
		Account carol = sn.join("Carol");
		makeFriends(me, alice);
		makeFriends(me, bob);
		makeFriends(me, eve);
		makeFriends(carol, alice);
		makeFriends(carol, bob);
		makeFriends(carol, eve);
		sn.login(me);
		assertTrue(sn.recommendFriends().contains("Carol"));
	}

	@Test
	public void doesNotRecommendAMemberWhoBlockedTheLoggedInUser() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account bob = sn.join("Bob");
		Account carol = sn.join("Carol");
		makeFriends(me, alice);
		makeFriends(me, bob);
		makeFriends(carol, alice);
		makeFriends(carol, bob);
		sn.login(carol);
		sn.block("Hakan");
		sn.login(me);
		assertFalse(sn.recommendFriends().contains("Carol"));
	}

	@Test
	public void aRecommendedMemberIsNoLongerRecommendedOnceBefriended() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account bob = sn.join("Bob");
		Account carol = sn.join("Carol");
		makeFriends(me, alice);
		makeFriends(me, bob);
		makeFriends(carol, alice);
		makeFriends(carol, bob);
		sn.login(me);
		assertTrue(sn.recommendFriends().contains("Carol"));
		makeFriends(me, carol);
		assertFalse(sn.recommendFriends().contains("Carol"));
	}

	@Test
	public void recommendsNobodyWhenFriendsHaveNoOtherFriends() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account bob = sn.join("Bob");
		makeFriends(me, alice);
		makeFriends(me, bob);
		sn.login(me);
		assertTrue(sn.recommendFriends().isEmpty());
	}

	@Test
	public void aMemberWithASingleFriendGetsNoRecommendations() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		Account alice = sn.join("Alice");
		Account carol = sn.join("Carol");
		makeFriends(me, alice);
		makeFriends(alice, carol);
		sn.login(me);
		assertTrue(sn.recommendFriends().isEmpty());
	}

	@Test
	public void cancelAutoAcceptFriendshipsStopsFutureAutoAcceptance() throws Exception {
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
	public void cancelAutoAcceptFriendshipsRequiresExplicitAcceptanceAfter() throws Exception {
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
	public void cancelAutoAcceptFriendshipsWhenNotEnabled() throws Exception {
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
	public void multipleToggleBetweenAutoAcceptAndCancel() throws Exception {
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

	// ----- T8: blocking terminates friendships -----

	@Test
	public void blockingTerminatesExistingFriendship() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		makeFriends(me, her);
		sn.login(me);
		sn.block("Cecile");
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));
	}

	@Test
	public void blockingTerminatesPendingIncomingFriendRequest() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		her.requestFriendship(me);
		sn.login(me);
		sn.block("Cecile");
		assertFalse(me.getIncomingRequests().contains("Cecile"));
		assertFalse(her.getOutgoingRequests().contains("Hakan"));
	}

	@Test
	public void blockingTerminatesPendingOutgoingFriendRequest() throws Exception {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		me.requestFriendship(her);
		sn.login(me);
		sn.block("Cecile");
		assertFalse(her.getIncomingRequests().contains("Hakan"));
		assertFalse(me.getOutgoingRequests().contains("Cecile"));
	}

	// ----- T9: NoUserLoggedInException -----

	@Test(expected = NoUserLoggedInException.class)
	public void listMembersThrowsWhenNotLoggedIn() throws Exception {
		sn = new SocialNetwork();
		sn.join("Hakan");
		sn.listMembers();
	}

	@Test(expected = NoUserLoggedInException.class)
	public void hasMemberThrowsWhenNotLoggedIn() throws Exception {
		sn = new SocialNetwork();
		sn.join("Hakan");
		sn.hasMember("Hakan");
	}

	@Test(expected = NoUserLoggedInException.class)
	public void sendFriendshipToThrowsWhenNotLoggedIn() throws Exception {
		sn = new SocialNetwork();
		sn.join("Hakan");
		sn.join("Cecile");
		sn.sendFriendshipTo("Cecile");
	}

	@Test(expected = NoUserLoggedInException.class)
	public void blockThrowsWhenNotLoggedIn() throws Exception {
		sn = new SocialNetwork();
		sn.join("Hakan");
		sn.block("Ghost");
	}

	@Test(expected = NoUserLoggedInException.class)
	public void acceptAllFriendshipsThrowsWhenNotLoggedIn() throws Exception {
		sn = new SocialNetwork();
		sn.acceptAllFriendships();
	}

	@Test(expected = NoUserLoggedInException.class)
	public void leaveThrowsWhenNotLoggedIn() throws Exception {
		sn = new SocialNetwork();
		sn.leave();
	}

}

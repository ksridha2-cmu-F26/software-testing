package edu.cmu;

import java.util.HashSet;
import java.util.Collection;
import java.util.Set;

public class SocialNetwork {
	
	private Collection<Account> accounts = new HashSet<Account>();

	// join SN with a new user name
	public Account join(String userName) {
		Account newAccount = new Account(userName);
		accounts.add(newAccount);
		return newAccount;
	}

	// find a member by user name 
	private Account findAccountForUserName(String userName) {
		// find account with user name userName
		// not accessible to outside because that would give a user full access to another member's account
		for (Account each : accounts) {
			if (each.getUserName().equals(userName)) 
					return each;
		}
		return null;
	}
	
	// list user names of all members
	public Collection<String> listMembers() {
		Collection<String> members = new HashSet<String>();
		for (Account each : accounts) {
			members.add(each.getUserName());
		}
		return members;
	}
	
	// from my account, send a friend request to user with userName from my account
	public void sendFriendshipTo(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.requestFriendship(me);
	}

	// from my account, accept a pending friend request from another user with userName
	public void acceptFriendshipFrom(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipAccepted(me);
	}

	// accept all pending friend requests sent to me
	public void acceptAllFriendshipsTo(Account me) {
		Set<String> pendingRequests = new HashSet<String>(me.getIncomingRequests());
		for (String userName : pendingRequests) {
			acceptFriendshipFrom(userName, me);
		}
	}

	// from my account, reject a pending friend request from another user with userName
	public void rejectFriendshipFrom(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipRejected(me);
	}

	// reject all pending friend requests sent to me
	public void rejectAllFriendshipsTo(Account me) {
		Set<String> pendingRequests = new HashSet<String>(me.getIncomingRequests());
		for (String userName : pendingRequests) {
			rejectFriendshipFrom(userName, me);
		}
	}

	// automatically accept all new friend requests sent to me
	public void autoAcceptFriendshipsTo(Account me) {
		me.autoAcceptFriendships();
	}

	// from my account, unfriend another user with userName
	public void sendFriendshipCancellationTo(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.cancelFriendship(me);
	}

	// remove me from the social network
	public void leave(Account me) {
		for (Account each : accounts) {
			if (each != me) {
				each.getFriends().remove(me.getUserName());
				each.getIncomingRequests().remove(me.getUserName());
				each.getOutgoingRequests().remove(me.getUserName());
			}
		}
		accounts.remove(me);
	}

}

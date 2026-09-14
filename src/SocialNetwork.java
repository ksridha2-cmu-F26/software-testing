import java.util.HashSet;
import java.util.Set;

public class SocialNetwork implements ISocialNetwork {

	private Set<Account> accounts = new HashSet<Account>();
	private Account loggedInUser = null;

	private boolean isLoggedIn() {
		return loggedInUser != null;
	}

	private Account findAccountForUserName(String userName) {
		for (Account each : accounts) {
			if (each.getUserName().equals(userName)) {
				return each;
			}
		}
		return null;
	}

	private boolean isVisibleToLoggedInUser(Account account) {
		if (account == null) {
			return false;
		}
		if (loggedInUser == null) {
			return true;
		}
		if (account == loggedInUser) {
			return true;
		}
		return !account.hasBlocked(loggedInUser.getUserName());
	}

	@Override
	public Account join(String userName) {
		if (userName == null || userName.isEmpty()) {
			return null;
		}
		if (findAccountForUserName(userName) != null) {
			return null;
		}
		Account newAccount = new Account(userName);
		accounts.add(newAccount);
		return newAccount;
	}

	@Override
	public Account login(Account me) {
		if (me == null) {
			return null;
		}
		if (accounts.contains(me)) {
			loggedInUser = me;
			return me;
		}
		return null;
	}

	@Override
	public Set<String> listMembers() {
		Set<String> members = new HashSet<String>();
		if (!isLoggedIn()) {
			return members;
		}
		for (Account each : accounts) {
			if (isVisibleToLoggedInUser(each)) {
				members.add(each.getUserName());
			}
		}
		return members;
	}

	@Override
	public boolean hasMember(String userName) {
		if (!isLoggedIn() || userName == null || userName.isEmpty()) {
			return false;
		}
		return isVisibleToLoggedInUser(findAccountForUserName(userName));
	}

	@Override
	public void sendFriendshipTo(String userName) {
		if (!isLoggedIn() || userName == null) {
			return;
		}
		Account target = findAccountForUserName(userName);
		if (!isVisibleToLoggedInUser(target)) {
			return;
		}
		target.requestFriendship(loggedInUser);
	}

	@Override
	public void block(String userName) {
		if (!isLoggedIn() || userName == null) {
			return;
		}
		loggedInUser.block(userName);
	}

	@Override
	public void unblock(String userName) {
		if (!isLoggedIn() || userName == null) {
			return;
		}
		loggedInUser.unblock(userName);
	}

	@Override
	public void sendFriendshipCancellationTo(String userName) {
		if (!isLoggedIn() || userName == null) {
			return;
		}
		Account friend = findAccountForUserName(userName);
		if (friend != null && loggedInUser.hasFriend(userName)) {
			friend.cancelFriendship(loggedInUser);
		}
	}

	@Override
	public void acceptFriendshipFrom(String userName) {
		if (!isLoggedIn() || userName == null) {
			return;
		}
		Account requester = findAccountForUserName(userName);
		if (requester != null && isVisibleToLoggedInUser(requester)) {
			requester.friendshipAccepted(loggedInUser);
		}
	}

	@Override
	public void acceptAllFriendships() {
		if (!isLoggedIn()) {
			return;
		}
		for (String requesterName : new HashSet<String>(loggedInUser.getIncomingRequests())) {
			acceptFriendshipFrom(requesterName);
		}
	}

	@Override
	public void rejectFriendshipFrom(String userName) {
		if (!isLoggedIn() || userName == null) {
			return;
		}
		Account requester = findAccountForUserName(userName);
		if (requester != null) {
			requester.friendshipRejected(loggedInUser);
		}
	}

	@Override
	public void rejectAllFriendships() {
		if (!isLoggedIn()) {
			return;
		}
		for (String requesterName : new HashSet<String>(loggedInUser.getIncomingRequests())) {
			rejectFriendshipFrom(requesterName);
		}
	}

	@Override
	public void autoAcceptFriendships() {
		if (!isLoggedIn()) {
			return;
		}
		loggedInUser.autoAcceptFriendships();
	}

	@Override
	public void cancelAutoAcceptFriendships() {
		if (!isLoggedIn()) {
			return;
		}
		loggedInUser.cancelAutoAcceptFriendships();
	}

	@Override
	public Set<String> recommendFriends() {
		return new HashSet<String>();
	}

	@Override
	public void leave() {
	}

	public void sendFriendshipTo(String userName, Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		sendFriendshipTo(userName);
		loggedInUser = previous;
	}

	public void acceptFriendshipFrom(String userName, Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		acceptFriendshipFrom(userName);
		loggedInUser = previous;
	}

	public void acceptAllFriendshipsTo(Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		acceptAllFriendships();
		loggedInUser = previous;
	}

	public void rejectFriendshipFrom(String userName, Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		rejectFriendshipFrom(userName);
		loggedInUser = previous;
	}

	public void rejectAllFriendshipsTo(Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		rejectAllFriendships();
		loggedInUser = previous;
	}

	public void autoAcceptFriendshipsTo(Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		autoAcceptFriendships();
		loggedInUser = previous;
	}

	public void sendFriendshipCancellationTo(String userName, Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		sendFriendshipCancellationTo(userName);
		loggedInUser = previous;
	}

	public void leave(Account me) {
		Account previous = loggedInUser;
		if (accounts.contains(me)) {
			for (String friendName : new HashSet<String>(me.getFriends())) {
				Account friend = findAccountForUserName(friendName);
				if (friend != null) {
					friend.cancelFriendship(me);
				}
			}
			for (Account account : accounts) {
				account.getIncomingRequests().remove(me.getUserName());
				account.getOutgoingRequests().remove(me.getUserName());
			}
			accounts.remove(me);
			if (loggedInUser == me) {
				loggedInUser = null;
			}
		}
		loggedInUser = previous == me ? null : previous;
	}

}

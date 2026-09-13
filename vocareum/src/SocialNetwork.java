
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SocialNetwork implements ISocialNetwork {

	private Set<Account> accounts = new HashSet<Account>();
	private Account loggedInUser = null;

	private void requireLoggedIn() throws NoUserLoggedInException {
		if (loggedInUser == null) {
			throw new NoUserLoggedInException();
		}
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

	private void terminateRelationshipsBetween(Account first, Account second) {
		if (first == null || second == null) {
			return;
		}
		if (first.hasFriend(second.getUserName())) {
			second.cancelFriendship(first);
		}
		first.getIncomingRequests().remove(second.getUserName());
		first.getOutgoingRequests().remove(second.getUserName());
		second.getIncomingRequests().remove(first.getUserName());
		second.getOutgoingRequests().remove(first.getUserName());
	}

	public Account getLoggedInUser() {
		return loggedInUser;
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
	public Set<String> listMembers() throws NoUserLoggedInException {
		requireLoggedIn();
		Set<String> members = new HashSet<String>();
		for (Account each : accounts) {
			if (isVisibleToLoggedInUser(each)) {
				members.add(each.getUserName());
			}
		}
		return members;
	}

	@Override
	public boolean hasMember(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null || userName.isEmpty()) {
			return false;
		}
		return isVisibleToLoggedInUser(findAccountForUserName(userName));
	}

	@Override
	public void sendFriendshipTo(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null) {
			return;
		}
		Account target = findAccountForUserName(userName);
		if (!isVisibleToLoggedInUser(target)) {
			return;
		}
		target.requestFriendship(loggedInUser);
	}

	@Override
	public void block(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null) {
			return;
		}
		Account target = findAccountForUserName(userName);
		if (target != null) {
			terminateRelationshipsBetween(loggedInUser, target);
		}
		loggedInUser.block(userName);
	}

	@Override
	public void unblock(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null) {
			return;
		}
		loggedInUser.unblock(userName);
	}

	@Override
	public void sendFriendshipCancellationTo(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null) {
			return;
		}
		Account friend = findAccountForUserName(userName);
		if (friend != null && loggedInUser.hasFriend(userName)) {
			friend.cancelFriendship(loggedInUser);
		}
	}

	@Override
	public void acceptFriendshipFrom(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null) {
			return;
		}
		Account requester = findAccountForUserName(userName);
		if (requester != null && isVisibleToLoggedInUser(requester)) {
			requester.friendshipAccepted(loggedInUser);
		}
	}

	@Override
	public void acceptAllFriendships() throws NoUserLoggedInException {
		requireLoggedIn();
		for (String requesterName : new HashSet<String>(loggedInUser.getIncomingRequests())) {
			acceptFriendshipFrom(requesterName);
		}
	}

	@Override
	public void rejectFriendshipFrom(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null) {
			return;
		}
		Account requester = findAccountForUserName(userName);
		if (requester != null) {
			requester.friendshipRejected(loggedInUser);
		}
	}

	@Override
	public void rejectAllFriendships() throws NoUserLoggedInException {
		requireLoggedIn();
		for (String requesterName : new HashSet<String>(loggedInUser.getIncomingRequests())) {
			rejectFriendshipFrom(requesterName);
		}
	}

	@Override
	public void autoAcceptFriendships() throws NoUserLoggedInException {
		requireLoggedIn();
		loggedInUser.autoAcceptFriendships();
	}

	@Override
	public void cancelAutoAcceptFriendships() throws NoUserLoggedInException {
		requireLoggedIn();
		loggedInUser.cancelAutoAcceptFriendships();
	}

	@Override
	public Set<String> recommendFriends() throws NoUserLoggedInException {
		requireLoggedIn();
		Set<String> recommendations = new HashSet<String>();
		Map<String, Integer> commonFriendCount = new HashMap<String, Integer>();

		for (String friendName : loggedInUser.getFriends()) {
			Account friend = findAccountForUserName(friendName);
			if (friend == null) {
				continue;
			}
			for (String candidate : friend.getFriends()) {
				if (candidate.equals(loggedInUser.getUserName())) {
					continue;
				}
				if (loggedInUser.hasFriend(candidate)) {
					continue;
				}
				Integer soFar = commonFriendCount.get(candidate);
				commonFriendCount.put(candidate, soFar == null ? 1 : soFar + 1);
			}
		}

		for (Map.Entry<String, Integer> each : commonFriendCount.entrySet()) {
			if (each.getValue() < 2) {
				continue;
			}
			if (loggedInUser.hasBlocked(each.getKey())) {
				continue;
			}
			if (!isVisibleToLoggedInUser(findAccountForUserName(each.getKey()))) {
				continue;
			}
			recommendations.add(each.getKey());
		}
		return recommendations;
	}

	@Override
	public void leave() throws NoUserLoggedInException {
		requireLoggedIn();
		Account departingUser = loggedInUser;
		for (String friendName : new HashSet<String>(departingUser.getFriends())) {
			Account friend = findAccountForUserName(friendName);
			if (friend != null) {
				friend.cancelFriendship(departingUser);
			}
		}
		for (Account account : accounts) {
			account.getIncomingRequests().remove(departingUser.getUserName());
			account.getOutgoingRequests().remove(departingUser.getUserName());
		}
		accounts.remove(departingUser);
		loggedInUser = null;
	}

	public void sendFriendshipTo(String userName, Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		try {
			sendFriendshipTo(userName);
		} catch (NoUserLoggedInException e) {
			// logged-in user was set above
		}
		loggedInUser = previous;
	}

	public void acceptFriendshipFrom(String userName, Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		try {
			acceptFriendshipFrom(userName);
		} catch (NoUserLoggedInException e) {
			// logged-in user was set above
		}
		loggedInUser = previous;
	}

	public void acceptAllFriendshipsTo(Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		try {
			acceptAllFriendships();
		} catch (NoUserLoggedInException e) {
			// logged-in user was set above
		}
		loggedInUser = previous;
	}

	public void rejectFriendshipFrom(String userName, Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		try {
			rejectFriendshipFrom(userName);
		} catch (NoUserLoggedInException e) {
			// logged-in user was set above
		}
		loggedInUser = previous;
	}

	public void rejectAllFriendshipsTo(Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		try {
			rejectAllFriendships();
		} catch (NoUserLoggedInException e) {
			// logged-in user was set above
		}
		loggedInUser = previous;
	}

	public void autoAcceptFriendshipsTo(Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		try {
			autoAcceptFriendships();
		} catch (NoUserLoggedInException e) {
			// logged-in user was set above
		}
		loggedInUser = previous;
	}

	public void sendFriendshipCancellationTo(String userName, Account me) {
		Account previous = loggedInUser;
		loggedInUser = me;
		try {
			sendFriendshipCancellationTo(userName);
		} catch (NoUserLoggedInException e) {
			// logged-in user was set above
		}
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

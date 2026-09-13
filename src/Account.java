import java.util.HashSet;
import java.util.Set;

public class Account {

	private String userName;
	private Set<String> incomingRequests = new HashSet<String>();
	private Set<String> outgoingRequests = new HashSet<String>();
	private Set<String> friends = new HashSet<String>();
	private Set<String> blockedMembers = new HashSet<String>();
	private boolean autoAcceptFriendships = false;

	public Account(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public Set<String> getIncomingRequests() {
		return incomingRequests;
	}

	public Set<String> getOutgoingRequests() {
		return outgoingRequests;
	}

	public Set<String> getFriends() {
		return friends;
	}

	public void requestFriendship(Account fromAccount) {
		if (fromAccount == null) {
			return;
		}
		if (!friends.contains(fromAccount.getUserName())) {
			incomingRequests.add(fromAccount.getUserName());
			fromAccount.outgoingRequests.add(this.getUserName());
			if (autoAcceptFriendships) {
				fromAccount.friendshipAccepted(this);
			}
		}
	}

	public boolean hasFriend(String userName) {
		return friends.contains(userName);
	}

	public void friendshipAccepted(Account toAccount) {
		if (toAccount == null || !toAccount.incomingRequests.contains(this.getUserName())) {
			return;
		}
		friends.add(toAccount.getUserName());
		toAccount.friends.add(this.getUserName());
		toAccount.incomingRequests.remove(this.getUserName());
		toAccount.outgoingRequests.remove(this.getUserName());
		outgoingRequests.remove(toAccount.getUserName());
	}

	public void friendshipRejected(Account toAccount) {
		if (toAccount == null) {
			return;
		}
		toAccount.incomingRequests.remove(this.getUserName());
		toAccount.outgoingRequests.remove(this.getUserName());
		outgoingRequests.remove(toAccount.getUserName());
	}

	public void cancelFriendship(Account otherAccount) {
		if (otherAccount == null) {
			return;
		}
		friends.remove(otherAccount.getUserName());
		otherAccount.friends.remove(this.getUserName());
	}

	public void autoAcceptFriendships() {
		autoAcceptFriendships = true;
	}

	public void cancelAutoAcceptFriendships() {
		autoAcceptFriendships = false;
	}

	public void block(String userName) {
		if (userName == null) {
			return;
		}
		blockedMembers.add(userName);
	}

	public void unblock(String userName) {
		blockedMembers.remove(userName);
	}

	public boolean hasBlocked(String userName) {
		return blockedMembers.contains(userName);
	}

}

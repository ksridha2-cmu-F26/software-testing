import java.util.Set;

public interface ISocialNetwork {

	Account join(String userName);

	Account login(Account me);

	Set<String> listMembers();

	boolean hasMember(String userName);

	void sendFriendshipTo(String userName);

	void block(String userName);

	void unblock(String userName);

	void sendFriendshipCancellationTo(String userName);

	void acceptFriendshipFrom(String userName);

	void acceptAllFriendships();

	void rejectFriendshipFrom(String userName);

	void rejectAllFriendships();

	void autoAcceptFriendships();

	void cancelAutoAcceptFriendships();

	Set<String> recommendFriends();

	void leave();

}

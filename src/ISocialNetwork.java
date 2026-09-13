import java.util.Set;

public interface ISocialNetwork {

	Account join(String userName);

	Account login(Account me);

	Set<String> listMembers() throws NoUserLoggedInException;

	boolean hasMember(String userName) throws NoUserLoggedInException;

	void sendFriendshipTo(String userName) throws NoUserLoggedInException;

	void block(String userName) throws NoUserLoggedInException;

	void unblock(String userName) throws NoUserLoggedInException;

	void sendFriendshipCancellationTo(String userName) throws NoUserLoggedInException;

	void acceptFriendshipFrom(String userName) throws NoUserLoggedInException;

	void acceptAllFriendships() throws NoUserLoggedInException;

	void rejectFriendshipFrom(String userName) throws NoUserLoggedInException;

	void rejectAllFriendships() throws NoUserLoggedInException;

	void autoAcceptFriendships() throws NoUserLoggedInException;

	void cancelAutoAcceptFriendships() throws NoUserLoggedInException;

	Set<String> recommendFriends() throws NoUserLoggedInException;

	void leave() throws NoUserLoggedInException;

}

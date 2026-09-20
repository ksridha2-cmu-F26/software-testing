import java.util.Set;

public interface ISocialNetwork {
    Account join(String userName) throws UserExistsException;
    Account login(Account me) throws UserNotFoundException;
    void logout();
    Set<String> listMembers() throws NoUserLoggedInException;
    boolean hasMember(String userName) throws NoUserLoggedInException;
    void sendFriendshipTo(String userName) throws UserNotFoundException, NoUserLoggedInException;
    void block(String userName) throws UserNotFoundException, NoUserLoggedInException;
    void unblock(String userName) throws UserNotFoundException, NoUserLoggedInException;
    void sendFriendshipCancellationTo(String userName) throws UserNotFoundException, NoUserLoggedInException;
    void acceptFriendshipFrom(String userName) throws UserNotFoundException, NoUserLoggedInException;
    void rejectFriendshipFrom(String userName) throws UserNotFoundException, NoUserLoggedInException;
    void autoAcceptFriendships() throws NoUserLoggedInException;
    void acceptAllFriendships() throws NoUserLoggedInException;
    void rejectAllFriendships() throws NoUserLoggedInException;
    void cancelAutoAcceptFriendships() throws NoUserLoggedInException;
    Set<String> recommendFriends() throws NoUserLoggedInException, UserNotFoundException;
    void leave() throws NoUserLoggedInException;
}

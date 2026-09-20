import java.util.HashSet;
import java.util.Set;

public class SocialNetwork implements ISocialNetwork {
    private final IAccountDAO accountDAO;
    private Account currentUser;

    public SocialNetwork() {
        this(DAOFactory.getInstance().getAccountDAO());
    }

    public SocialNetwork(IAccountDAO accountDAO) {
        if (accountDAO == null) {
            throw new IllegalArgumentException("accountDAO must not be null");
        }
        this.accountDAO = accountDAO;
    }

    private Account accountFor(String userName) {
        return accountDAO.findByUserName(userName);
    }

    private void requireLogin() throws NoUserLoggedInException {
        if (currentUser == null) {
            throw new NoUserLoggedInException();
        }
    }

    private Account requireMember(String userName) throws UserNotFoundException {
        Account member = accountFor(userName);
        if (member == null) {
            throw new UserNotFoundException(userName);
        }
        return member;
    }

    @Override
    public Account join(String userName) throws UserExistsException {
        if (userName == null || accountFor(userName) != null) {
            throw new UserExistsException(userName);
        }
        Account member = new MyAccount(userName);
        accountDAO.save(member);
        return member;
    }

    @Override
    public Account login(Account me) throws UserNotFoundException {
        if (me == null) {
            throw new UserNotFoundException("null");
        }
        Account member = accountFor(me.getUserName());
        if (member == null) {
            throw new UserNotFoundException(me.getUserName());
        }
        currentUser = member;
        return member;
    }

    @Override
    public void logout() {
        currentUser = null;
    }

    @Override
    public Set<String> listMembers() throws NoUserLoggedInException {
        requireLogin();
        Set<String> names = new HashSet<String>();
        for (Account member : accountDAO.findAll()) {
            if (!member.blockedMembers().contains(currentUser.getUserName())) {
                names.add(member.getUserName());
            }
        }
        return names;
    }

    @Override
    public boolean hasMember(String userName) throws NoUserLoggedInException {
        requireLogin();
        Account member = accountFor(userName);
        return member != null && !member.blockedMembers().contains(currentUser.getUserName());
    }

    @Override
    public void sendFriendshipTo(String userName) throws UserNotFoundException, NoUserLoggedInException {
        requireLogin();
        Account member = requireMember(userName);
        if (!member.blockedMembers().contains(currentUser.getUserName())) {
            member.requestFriendship(currentUser);
            accountDAO.update(member);
            accountDAO.update(currentUser);
        }
    }

    @Override
    public void block(String userName) throws UserNotFoundException, NoUserLoggedInException {
        requireLogin();
        Account member = requireMember(userName);
        currentUser.block(member);
        member.cancelFriendship(currentUser);
        member.getIncomingRequests().remove(currentUser.getUserName());
        member.getOutgoingRequests().remove(currentUser.getUserName());
        currentUser.getIncomingRequests().remove(member.getUserName());
        currentUser.getOutgoingRequests().remove(member.getUserName());
        accountDAO.update(currentUser);
        accountDAO.update(member);
    }

    @Override
    public void unblock(String userName) throws UserNotFoundException, NoUserLoggedInException {
        requireLogin();
        Account member = requireMember(userName);
        currentUser.unblock(member);
        accountDAO.update(currentUser);
    }

    @Override
    public void sendFriendshipCancellationTo(String userName) throws UserNotFoundException, NoUserLoggedInException {
        requireLogin();
        Account member = requireMember(userName);
        member.cancelFriendship(currentUser);
        accountDAO.update(member);
        accountDAO.update(currentUser);
    }

    @Override
    public void acceptFriendshipFrom(String userName) throws UserNotFoundException, NoUserLoggedInException {
        requireLogin();
        Account member = requireMember(userName);
        member.friendshipAccepted(currentUser);
        accountDAO.update(member);
        accountDAO.update(currentUser);
    }

    @Override
    public void rejectFriendshipFrom(String userName) throws UserNotFoundException, NoUserLoggedInException {
        requireLogin();
        Account member = requireMember(userName);
        member.friendshipRejected(currentUser);
        accountDAO.update(member);
        accountDAO.update(currentUser);
    }

    @Override
    public void autoAcceptFriendships() throws NoUserLoggedInException {
        requireLogin();
        currentUser.autoAcceptFriendships();
        accountDAO.update(currentUser);
    }

    @Override
    public void cancelAutoAcceptFriendships() throws NoUserLoggedInException {
        requireLogin();
        currentUser.cancelAutoAcceptFriendships();
        accountDAO.update(currentUser);
    }

    @Override
    public void acceptAllFriendships() throws NoUserLoggedInException {
        requireLogin();
        for (String name : new HashSet<String>(currentUser.getIncomingRequests())) {
            try {
                acceptFriendshipFrom(name);
            } catch (UserNotFoundException ignored) {
            }
        }
    }

    @Override
    public void rejectAllFriendships() throws NoUserLoggedInException {
        requireLogin();
        for (String name : new HashSet<String>(currentUser.getIncomingRequests())) {
            try {
                rejectFriendshipFrom(name);
            } catch (UserNotFoundException ignored) {
            }
        }
    }

    @Override
    public Set<String> recommendFriends() throws NoUserLoggedInException, UserNotFoundException {
        requireLogin();
        Set<String> recommendations = new HashSet<String>();
        Set<String> seen = new HashSet<String>();
        for (String friendName : currentUser.getFriends()) {
            Account friend = requireMember(friendName);
            for (String candidateName : friend.getFriends()) {
                if (seen.contains(candidateName)) {
                    Account candidate = requireMember(candidateName);
                    if (!candidateName.equals(currentUser.getUserName())
                            && !currentUser.getFriends().contains(candidateName)
                            && !currentUser.blockedMembers().contains(candidateName)
                            && !candidate.blockedMembers().contains(currentUser.getUserName())) {
                        recommendations.add(candidateName);
                    }
                } else {
                    seen.add(candidateName);
                }
            }
        }
        return recommendations;
    }

    @Override
    public void leave() throws NoUserLoggedInException {
        requireLogin();
        Account departing = currentUser;
        for (String friendName : new HashSet<String>(departing.getFriends())) {
            Account friend = accountDAO.findByUserName(friendName);
            if (friend != null) {
                friend.cancelFriendship(departing);
                accountDAO.update(friend);
            }
        }
        for (Account member : accountDAO.findAll()) {
            member.getIncomingRequests().remove(departing.getUserName());
            member.getOutgoingRequests().remove(departing.getUserName());
            accountDAO.update(member);
        }
        accountDAO.delete(departing);
        currentUser = null;
    }

    private static class MyAccount extends Account {
        private MyAccount(String userName) {
            setUserName(userName);
        }
    }
}

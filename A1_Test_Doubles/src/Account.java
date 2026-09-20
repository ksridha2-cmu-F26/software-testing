import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Account implements Cloneable {
    private String userName;
    private boolean autoAccept;
    private Set<String> incomingRequests = new HashSet<String>();
    private Set<String> outgoingRequests = new HashSet<String>();
    private Set<String> friends = new HashSet<String>();
    private Set<String> blocked = new HashSet<String>();

    protected Account() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> blockedMembers() {
        return blocked;
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

    public void block(Account member) {
        if (member != null) {
            blocked.add(member.getUserName());
        }
    }

    public void unblock(Account member) {
        if (member != null) {
            blocked.remove(member.getUserName());
        }
    }

    public void requestFriendship(Account fromMember) {
        if (fromMember == null || blocked.contains(fromMember.getUserName())) {
            return;
        }
        incomingRequests.add(fromMember.getUserName());
        fromMember.outgoingRequests.add(userName);
        if (autoAccept) {
            fromMember.friendshipAccepted(this);
        }
    }

    public void cancelFriendship(Account member) {
        if (member == null) {
            return;
        }
        friends.remove(member.getUserName());
        member.friends.remove(userName);
    }

    public boolean hasFriends() {
        return !friends.isEmpty();
    }

    public void friendshipAccepted(Account member) {
        if (member == null) {
            return;
        }
        friends.add(member.getUserName());
        outgoingRequests.remove(member.getUserName());
        member.friends.add(userName);
        member.incomingRequests.remove(userName);
    }

    public void friendshipRejected(Account member) {
        if (member == null) {
            return;
        }
        outgoingRequests.remove(member.getUserName());
        member.incomingRequests.remove(userName);
    }

    public boolean hasFriend(Account member) {
        return member != null && friends.contains(member.getUserName());
    }

    public void autoAcceptFriendships() {
        autoAccept = true;
    }

    public void cancelAutoAcceptFriendships() {
        autoAccept = false;
    }

    public boolean autoAccepts() {
        return autoAccept;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        return autoAccept == other.autoAccept
                && Objects.equals(userName, other.userName)
                && outgoingRequests.equals(other.outgoingRequests)
                && incomingRequests.equals(other.incomingRequests)
                && blocked.equals(other.blocked)
                && friends.equals(other.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userName);
    }

    @Override
    protected Account clone() {
        Account copy = new Account();
        copy.userName = userName;
        copy.autoAccept = autoAccept;
        copy.friends.addAll(friends);
        copy.incomingRequests.addAll(incomingRequests);
        copy.outgoingRequests.addAll(outgoingRequests);
        copy.blocked.addAll(blocked);
        return copy;
    }
}

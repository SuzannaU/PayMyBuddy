package oc.paymybuddy.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RelationId implements Serializable {

    private int invitingUserId;
    private int invitedUserId;

    public int getInvitingUserId() {
        return invitingUserId;
    }

    public void setInvitingUserId(int invitingUserId) {
        this.invitingUserId = invitingUserId;
    }

    public int getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(int invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        RelationId that = (RelationId) o;
        return invitingUserId == that.invitingUserId && invitedUserId == that.invitedUserId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(invitingUserId, invitedUserId);
    }
}

package oc.paymybuddy.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RelationId implements Serializable {

    private int user1Id;
    private int user2Id;

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        RelationId that = (RelationId) o;
        return user1Id == that.user1Id && user2Id == that.user2Id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1Id, user2Id);
    }
}

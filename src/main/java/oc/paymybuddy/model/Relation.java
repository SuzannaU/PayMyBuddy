package oc.paymybuddy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_user")
public class Relation {

    @EmbeddedId
    private RelationId id = new RelationId();

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @MapsId("invitingUserId")
    @JoinColumn(name = "user1_id")
    private User invitingUser;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @MapsId("invitedUserId")
    @JoinColumn(name = "user2_id")
    private User invitedUser;

    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        users.add(invitingUser);
        users.add(invitedUser);
        return users;
    }

    public RelationId getId() {
        return id;
    }

    public User getInvitingUser() {
        return invitingUser;
    }

    public void setInvitingUser(User invitingUser) {
        this.invitingUser = invitingUser;
    }

    public User getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }
}

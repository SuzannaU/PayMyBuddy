package oc.paymybuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_user")
public class Relation {

    @EmbeddedId
    private RelationId id;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @MapsId("invitingUserId")
    @JoinColumn(name = "user1_id")
    @JsonManagedReference("invitingUserReference")
    private User invitingUser;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @MapsId("invitedUserId")
    @JoinColumn(name = "user2_id")
    @JsonManagedReference("invitedUserReference")
    private User invitedUser;

    @JsonIgnore
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

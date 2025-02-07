package oc.paymybuddy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_user")
public class Relation {

    @EmbeddedId
    private RelationId id = new RelationId();

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @MapsId("user1Id")
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @MapsId("user2Id")
    @JoinColumn(name = "user2_id")
    private User user2;
}

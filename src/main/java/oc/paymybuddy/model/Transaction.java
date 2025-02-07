package oc.paymybuddy.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "transactions")
@DynamicUpdate
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int id;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH}
    )
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH}
    )
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String description;
    private double amount;

}

package oc.paymybuddy.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinColumn(name = "sender_id")
    @JsonManagedReference("sentTransactions")
    private User sender;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinColumn(name = "receiver_id")
    @JsonManagedReference("receivedTransactions")
    private User receiver;

    private String description;
    private double amount;
    private double fee;

    public int getId() {

        return id;
    }

    public User getSender() {

        return sender;
    }

    public void setSender(User sender) {

        this.sender = sender;
    }

    public User getReceiver() {

        return receiver;
    }

    public void setReceiver(User receiver) {

        this.receiver = receiver;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public double getAmount() {

        return amount;
    }

    public void setAmount(double amount) {

        this.amount = amount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}

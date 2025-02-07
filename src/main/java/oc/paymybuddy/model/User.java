package oc.paymybuddy.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "users")
@DynamicUpdate
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    private String username;
    private String password;
    private String email;
    private double balance;

    @OneToMany(
            mappedBy = "sender",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    List<Transaction> sentTransactions;

    @OneToMany(
            mappedBy = "receiver",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    List<Transaction> receivedTransactions;

    @OneToMany(
            mappedBy = "user1",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    List<Relation> initiatedRelations;

    @OneToMany(
            mappedBy = "user2",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    List<Relation> receivedRelations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getSentTransactions() {
        return sentTransactions;
    }

    public void setSentTransactions(List<Transaction> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }

    public void setReceivedTransactions(List<Transaction> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }

    public List<Relation> getInitiatedRelations() {
        return initiatedRelations;
    }

    public void setInitiatedRelations(List<Relation> initiatedRelations) {
        this.initiatedRelations = initiatedRelations;
    }

    public List<Relation> getReceivedRelations() {
        return receivedRelations;
    }

    public void setReceivedRelations(List<Relation> receivedRelations) {
        this.receivedRelations = receivedRelations;
    }
}

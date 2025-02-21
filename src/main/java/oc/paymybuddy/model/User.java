package oc.paymybuddy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"sentTransactions", "receivedTransactions", "invitingRelations", "invitedRelations"})
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
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    @JsonBackReference("sentTransactions")
    private List<Transaction> sentTransactions;


    @OneToMany(
            mappedBy = "receiver",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    @JsonBackReference("receivedTransactions")
    private List<Transaction> receivedTransactions;


    @OneToMany(
            mappedBy = "invitingUser",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonBackReference("invitingUserReference")
    private List<Relation> invitingRelations;


    @OneToMany(
            mappedBy = "invitedUser",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonBackReference("invitedUserReference")
    private List<Relation> invitedRelations;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference("userRoles")
    private List<UserRole> userRoles;

    @JsonIgnore
    public List<Relation> getRelations() {
        List<Relation> relations = new ArrayList<>();
        relations.addAll(invitedRelations);
        relations.addAll(invitingRelations);
        return relations;
    }

    @JsonIgnore
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(sentTransactions);
        transactions.addAll(receivedTransactions);
        return transactions;
    }

    public void addSentTransactions(Transaction t) {
        sentTransactions.add(t);
        t.setSender(this);
    }

    public void addReceivedTransactions(Transaction t) {
        receivedTransactions.add(t);
        t.setReceiver(this);
    }

    public void addInvitingRelation(Relation r) {
        invitingRelations.add(r);
        r.setInvitingUser(this);
    }

    public void addInvitedTransactions(Relation r) {
        invitedRelations.add(r);
        r.setInvitedUser(this);
    }

    public void removeInvitingRelation(Relation r) {
        invitingRelations.remove(r);
        r.setInvitingUser(null);
    }

    public void removeInvitedTransactions(Relation r) {
        invitedRelations.remove(r);
        r.setInvitedUser(null);
    }

    public int getId() {
        return id;
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

    public List<Relation> getInvitingRelations() {
        return invitingRelations;
    }

    public void setInvitingRelations(List<Relation> invitingRelations) {
        this.invitingRelations = invitingRelations;
    }

    public List<Relation> getInvitedRelations() {
        return invitedRelations;
    }

    public void setInvitedRelations(List<Relation> invitedRelations) {
        this.invitedRelations = invitedRelations;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}

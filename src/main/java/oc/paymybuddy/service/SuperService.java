package oc.paymybuddy.service;

import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Service
public class SuperService {

    private final RelationService relationService;
    private final TransactionService transactionService;
    private final UserService userService;

    public SuperService(RelationService relationService, TransactionService transactionService, UserService userService) {
        this.relationService = relationService;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    public Relation addRelation(User invitingUser, User invitedUser) {
        if (userService.isAnExistingUser(invitedUser.getUsername())) {
            return relationService.addRelation(invitingUser, invitedUser);
        }
        return null;
    }

    public Set<String> getRelationsUsernamesByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return relationService.getRelationsUsernamesByUser(user);
    }

    public Transaction transfer(User sender, User receiver, String description, double amount) {
        if (sender.getBalance() >= amount) {
            userService.updateBalances(sender, receiver, amount);
            return transactionService.addTransaction(sender, receiver, description, amount);
        }
        return null;
    }

    public List<Transaction> getTransactionsByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return transactionService.getTransactionsByUser(user);
    }


}

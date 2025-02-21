package oc.paymybuddy.service;

import oc.paymybuddy.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SuperService {
    private static final Logger logger = LoggerFactory.getLogger(SuperService.class);
    private final RelationService relationService;
    private final TransactionService transactionService;
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final RoleService roleService;

    public SuperService(RelationService relationService, TransactionService transactionService, UserService userService,
                        UserRoleService userRoleService, RoleService roleService) {
        this.relationService = relationService;
        this.transactionService = transactionService;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }

    public Relation addRelation(User invitingUser, User invitedUser) {
        if (userService.isAnExistingUsername(invitedUser.getUsername())) {
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

    public User registerUser(User user) {
        User registeredUser = userService.registerUser(user);
        userRoleService.assignRoleToUser(user, roleService.getRoleByRoleName(Roles.USER.name()));
        return registeredUser;
    }


}

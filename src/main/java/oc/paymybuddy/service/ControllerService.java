package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.UnsufficientFundsException;
import oc.paymybuddy.exceptions.UserNotFoundException;
import oc.paymybuddy.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ControllerService {
    private static final Logger logger = LoggerFactory.getLogger(ControllerService.class);
    private final RelationService relationService;
    private final TransactionService transactionService;
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final RoleService roleService;

    public ControllerService(RelationService relationService, TransactionService transactionService, UserService userService,
                             UserRoleService userRoleService, RoleService roleService) {
        this.relationService = relationService;
        this.transactionService = transactionService;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }

    public Relation addRelation(String invitingUsername, String invitedUserEmail) {

        if (userService.isAnExistingEmail(invitedUserEmail)) {
            User invitedUser = userService.getUserByEmail(invitedUserEmail);
            User invitingUser = userService.getUserByUsername(invitingUsername);
            return relationService.addRelation(invitingUser, invitedUser);
        }
        throw new UserNotFoundException();    // handle in upper layer
    }

    public Set<String> getRelationsUsernamesByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return relationService.getRelationsUsernamesByUser(user);
    }

    public List<Transaction> getSentTransactionsByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return transactionService.getSentTransactionsByUser(user);
    }

    public Transaction transfer(
            String senderUsername, String receiverUsername, String description, String stringAmount) {
        User sender = userService.getUserByUsername(senderUsername);
        User receiver = userService.getUserByUsername(receiverUsername);
        double amount = Double.parseDouble(stringAmount);
        if (sender.getBalance() >= amount) {
            userService.updateBalances(sender, receiver, amount);
            return transactionService.addTransaction(sender, receiver, description, amount);
        }
        throw new UnsufficientFundsException();    // handle in upper layer
    }

    public User getUserByUsername(String username) {

        return userService.getUserByUsername(username);
    }

    public User registerUser(User user) {
        logger.debug("ControllerService/registerUser method called");
        User registeredUser = userService.registerUser(user);
        userRoleService.assignRoleToUser(user, roleService.getRoleByRoleName(Roles.USER.name()));
        logger.debug("registerUser method called");
        return registeredUser;
    }

    public User updateUsername(String currentUsername, String newUsername) {
        logger.debug("ControllerService/updateUsername method called");
        return userService.updateUsername(currentUsername, newUsername);
    }

    public User updateEmail(String currentUsername, String newEmail) {
        logger.debug("ControllerService/updateEmail method called");
        return userService.updateEmail(currentUsername, newEmail);
    }

    public User updatePassword(String currentUsername, String newPassword) {
        logger.debug("ControllerService/updatePassword method called");
        return userService.updatePassword(currentUsername, newPassword);
    }
}

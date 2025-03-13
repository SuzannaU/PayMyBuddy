package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.UnsufficientFundsException;
import oc.paymybuddy.exceptions.UserNotFoundException;
import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.Roles;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ControllerService(RelationService relationService,
                             TransactionService transactionService,
                             UserService userService,
                             UserRoleService userRoleService,
                             RoleService roleService) {
        this.relationService = relationService;
        this.transactionService = transactionService;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }

    @Transactional(rollbackFor = Exception.class)
    public Relation addRelation(String invitingUsername, String invitedUserEmail) {

        if (userService.isAnExistingEmail(invitedUserEmail)) {
            User invitingUser = userService.getUserByUsername(invitingUsername);
            User invitedUser = userService.getUserByEmail(invitedUserEmail);
            return relationService.addRelation(invitingUser, invitedUser);
        }
        throw new UserNotFoundException();
    }

    @Transactional(rollbackFor = Exception.class)
    public Transaction transfer(
            String senderUsername,
            String receiverUsername,
            String description,
            String stringAmount) {

        User sender = userService.getUserByUsername(senderUsername);
        User receiver = userService.getUserByUsername(receiverUsername);
        double amount = Double.parseDouble(stringAmount);
        if (sender.getBalance() >= amount) {
            Transaction transaction = transactionService.addTransaction(sender, receiver, description, amount);
            userService.updateBalances(sender, receiver, amount);
            return transaction;
        }
        throw new UnsufficientFundsException();
    }

    public User getUserByUsername(String username) {

        return userService.getUserByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    public User registerUser(User user) {
        logger.debug("registerUser method called");
        User registeredUser = userService.registerUser(user);
        userRoleService.assignRoleToUser(user, roleService.getRoleByRoleName(Roles.USER.name()));
        return registeredUser;
    }

    @Transactional(rollbackFor = Exception.class)
    public User updateUsername(String currentUsername, String newUsername) {
        logger.debug("ControllerService/updateUsername method called");
        return userService.updateUsername(currentUsername, newUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public User updateEmail(String currentUsername, String newEmail) {
        logger.debug("ControllerService/updateEmail method called");
        return userService.updateEmail(currentUsername, newEmail);
    }

    @Transactional(rollbackFor = Exception.class)
    public User updatePassword(String currentUsername, String newPassword) {
        logger.debug("ControllerService/updatePassword method called");
        return userService.updatePassword(currentUsername, newPassword);
    }

    public Set<String> getRelationsUsernamesByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return relationService.getRelationsUsernamesByUser(user);
    }

    public List<Transaction> getSentTransactionsByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return transactionService.getSentTransactionsByUser(user);
    }
}

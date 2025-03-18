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

/**
 * This Service is called by the Controllers and its responsibility is to call Model Services
 * <p>
 * The methods that cause a change in the DB are all Transactional, with inner methods running in the same Transaction.
 * Rollbacks will be made in case of any Exception.
 */
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

    /**
     * Calls UserService to recover Users and RelationService to create the new relation
     *
     * @param invitingUsername the inviting username
     * @param invitedUserEmail the invited user email
     * @return the saved relation
     * @throws UserNotFoundException in case the invited user email doesn't exist
     */
    @Transactional(rollbackFor = Exception.class)
    public Relation addRelation(String invitingUsername, String invitedUserEmail) {

        if (userService.isAnExistingEmail(invitedUserEmail)) {
            User invitingUser = userService.getUserByUsername(invitingUsername);
            User invitedUser = userService.getUserByEmail(invitedUserEmail);
            return relationService.addRelation(invitingUser, invitedUser);
        }
        throw new UserNotFoundException();
    }

    /**
     * Performs the transfer by calling the addTransaction method and the updateBalances method
     * In V1, the fee will be collected here
     *
     * @param senderUsername   the sender username
     * @param receiverUsername the receiver username
     * @param description      the description
     * @param stringAmount     the amount as String
     * @return the saved transaction
     * @throws UnsufficientFundsException in case the sender doesn't have enough funds for the transfer
     */
    @Transactional(rollbackFor = Exception.class)
    public Transaction transfer(
            String senderUsername,
            String receiverUsername,
            String description,
            String stringAmount) {

        User sender = userService.getUserByUsername(senderUsername);
        User receiver = userService.getUserByUsername(receiverUsername);
        double netAmount = Double.parseDouble(stringAmount);
        double fee = transactionService.calculateFee(netAmount);
        if (sender.getBalance() >= netAmount + fee) {
            Transaction transaction = transactionService.addTransaction(sender, receiver, description, netAmount, fee);
            //collectFee(fee) method to be implemented in V1
            userService.updateBalances(sender, receiver, netAmount);
            return transaction;
        }
        throw new UnsufficientFundsException();
    }

    /**
     * Registers a new User by calling register method and assignRole method.
     *
     * @param user the new user
     * @return the saved user
     */
    @Transactional(rollbackFor = Exception.class)
    public User registerUser(User user) {
        User registeredUser = userService.registerUser(user);
        userRoleService.assignRoleToUser(user, roleService.getRoleByRoleName(Roles.USER.name()));
        return registeredUser;
    }

    /**
     * Calls service to update the username of a user.
     *
     * @param currentUsername the current username
     * @param newUsername     the new username
     * @return the saved user
     */
    @Transactional(rollbackFor = Exception.class)
    public User updateUsername(String currentUsername, String newUsername) {
        return userService.updateUsername(currentUsername, newUsername);
    }

    /**
     * Calls service to update the email of a user.
     *
     * @param currentUsername the current username
     * @param newEmail        the new email
     * @return the saved user
     */
    @Transactional(rollbackFor = Exception.class)
    public User updateEmail(String currentUsername, String newEmail) {
        return userService.updateEmail(currentUsername, newEmail);
    }

    /**
     * Calls service to update the password of a user.
     *
     * @param currentUsername the current username
     * @param newPassword     the new password
     * @return the saved user
     */
    @Transactional(rollbackFor = Exception.class)
    public User updatePassword(String currentUsername, String newPassword) {
        return userService.updatePassword(currentUsername, newPassword);
    }

    /**
     * Calls services to get relations usernames by username.
     * Used in TransactionController to recover relations of a user
     *
     * @param username the username
     * @return the relations usernames for that username
     */
    public Set<String> getRelationsUsernamesByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return relationService.getRelationsUsernamesByUser(user);
    }

    /**
     * Calls services to get sent transactions by username.
     * Used in TransactionController to recover the transfers made by a user
     *
     * @param username the username
     * @return transactions sent by that username
     */
    public List<Transaction> getSentTransactionsByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return transactionService.getSentTransactionsByUser(user);
    }

    /**
     * Calls service to get user by username.
     * Used in UserController to recover Users from Principal usernames.
     *
     * @param username the username
     * @return the user by username
     */
    public User getUserByUsername(String username) {

        return userService.getUserByUsername(username);
    }
}

package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.*;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Verifies User attributes are valid and if so, encodes password and calls repo to save the User
     *
     * @param user the User to be registered
     * @return the saved User
     * @throws ExistingUsernameException
     * @throws ExistingEmailException
     * @throws TooLongUsernameException
     * @throws TooLongEmailException
     * @throws TooLongPasswordException
     */
    public User registerUser(User user) {
        logger.debug("registerUser method called");
        if (isAnExistingUsername(user.getUsername())) {
            throw new ExistingUsernameException();
        } else if (isAnExistingEmail(user.getEmail())) {
            throw new ExistingEmailException();
        } else if (user.getUsername().length() > 45) {
            throw new TooLongUsernameException();
        } else if (user.getEmail().length() > 100) {
            throw new TooLongEmailException();
        } else if (user.getPassword().length() > 45) {
            throw new TooLongPasswordException();
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.info("Encoded Password: " + user.getPassword());
            return userRepo.save(user);
        }
    }

    /**
     * Verifies that the new Username is valid and if so, updates the User and calls repo to save it
     *
     * @param currentUsername
     * @param newUsername
     * @return the saved User
     * @throws ExistingUsernameException
     * @throws TooLongUsernameException  *
     */
    public User updateUsername(String currentUsername, String newUsername) {
        if (isAnExistingUsername(newUsername)) {
            throw new ExistingUsernameException();
        } else if (newUsername.length() > 45) {
            throw new TooLongUsernameException();
        } else {
            User user = getUserByUsername(currentUsername);
            user.setUsername(newUsername);
            logger.info("Username updated to: " + user.getUsername());
            return userRepo.save(user);
        }
    }

    /**
     * Verifies that the new email is valid and if so, updates the User and calls repo to save it.
     *
     * @param username
     * @param newEmail
     * @return the saved User
     * @throws ExistingEmailException
     * @throws TooLongEmailException
     */
    public User updateEmail(String username, String newEmail) {
        if (isAnExistingEmail(newEmail)) {
            throw new ExistingEmailException();
        } else if (newEmail.length() > 100) {
            throw new TooLongEmailException();
        } else {
            User user = getUserByUsername(username);
            user.setEmail(newEmail);
            logger.info("Email updated to: " + user.getEmail());
            return userRepo.save(user);
        }
    }

    /**
     * Verifies that the new password is valid and if so, encodes it, updates the User and calls repo to save it.
     *
     * @param username
     * @param newPassword
     * @return the saved User
     * @throws TooLongPasswordException
     */
    public User updatePassword(String username, String newPassword) {
        if (newPassword.length() > 45) {
            throw new TooLongPasswordException();
        } else {
            User user = getUserByUsername(username);
            user.setPassword(passwordEncoder.encode(newPassword));
            logger.info("Password updated");
            return userRepo.save(user);
        }
    }

    /**
     * When a Transfer happens, updates the balances of both Users and calls repo to save them
     *
     * @param sender
     * @param receiver
     * @param amount
     */
    public void updateBalances(User sender, User receiver, double amount) {
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        userRepo.save(sender);
        logger.info("Sender balance updated to: " + sender.getBalance());
        userRepo.save(receiver);
        logger.info("Receiver balance updated  to: " + receiver.getBalance());
    }

    /**
     * Calls repo to recover a User by username and checks if it exists
     *
     * @param username
     * @return the corresponding User if it exists
     * @throws UserNotFoundException
     */
    public User getUserByUsername(String username) {
        Optional<User> optUser = userRepo.findByUsername(username);
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * Calls repo to recover a User by email and checks if it exists
     *
     * @param email
     * @return the correspondind User if it exists
     * @throws UserNotFoundException
     */
    public User getUserByEmail(String email) {
        Optional<User> optUser = userRepo.findByEmail(email);
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * Checks if a username already exists by calling repo
     *
     * @param username
     * @return true if the username already exists
     */
    public boolean isAnExistingUsername(String username) {

        return userRepo.findByUsername(username).isPresent();
    }

    /**
     * Check if an email already exists by calling repo
     *
     * @param email
     * @return true if the email already exists
     */
    public boolean isAnExistingEmail(String email) {

        return userRepo.findByEmail(email).isPresent();
    }

}

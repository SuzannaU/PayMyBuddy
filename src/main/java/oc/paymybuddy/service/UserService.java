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
            logger.debug("Encoded Password: " + user.getPassword());
            return userRepo.save(user);
        }
    }

    public User updateUsername(String currentUsername, String newUsername) {
        if (isAnExistingUsername(newUsername)) {
            throw new ExistingUsernameException();
        } else if (newUsername.length() > 45) {
            throw new TooLongUsernameException();
        } else {
            User user = getUserByUsername(currentUsername);
            user.setUsername(newUsername);
            logger.debug("Username updated to: " + user.getUsername());
            return userRepo.save(user);
        }
    }

    public User updateEmail(String username, String newEmail) {
        if (isAnExistingEmail(newEmail)) {
            throw new ExistingEmailException();
        } else if (newEmail.length() > 100) {
            throw new TooLongEmailException();
        } else {
            User user = getUserByUsername(username);
            user.setEmail(newEmail);
            logger.debug("Email updated to: " + user.getEmail());
            return userRepo.save(user);
        }
    }

    public User updatePassword(String username, String newPassword){
        if (newPassword.length() > 45) {
            throw new TooLongPasswordException();
        } else {
            User user = getUserByUsername(username);
            user.setPassword(passwordEncoder.encode(newPassword));
            logger.debug("Password updated");
            return userRepo.save(user);
        }
    }

    public void updateBalances(User sender, User receiver, double amount) {
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        userRepo.save(sender);
        logger.debug("Sender balance updated");
        userRepo.save(receiver);
        logger.debug("Receiver balance updated");
    }

    public User getUserByUsername(String username) {
        Optional<User> optUser = userRepo.findByUsername(username);
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> optUser = userRepo.findByEmail(email);
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    public boolean isAnExistingUsername(String username) {

        return userRepo.findByUsername(username).isPresent();
    }

    public boolean isAnExistingEmail(String email) {

        return userRepo.findByEmail(email).isPresent();
    }

}

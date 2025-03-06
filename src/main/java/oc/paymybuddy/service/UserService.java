package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.ExistingEmailException;
import oc.paymybuddy.exceptions.ExistingUsernameException;
import oc.paymybuddy.exceptions.TooLongException;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
        logger.debug("UserService/registerUser method called");
        if (isAnExistingUsername(user.getUsername())) {
            throw new ExistingUsernameException();
        } else if (isAnExistingEmail(user.getEmail())) {
            throw new ExistingEmailException();
        } else if (user.getUsername().length() > 45
                || user.getEmail().length() > 100
                || user.getPassword().length() > 45) {
            throw new TooLongException();
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.debug("Encoded Password: " + user.getPassword());
            return userRepo.save(user);
        }
    }

    public User updateUsername(User user, String newUsername) {
        if (isAnExistingUsername(newUsername)) {
            throw new ExistingUsernameException();
        } else if (newUsername.length() > 45) {
            throw new TooLongException();
        } else {
            user.setUsername(newUsername);
            logger.debug("Username updated to: " + user.getUsername());
            return userRepo.save(user);
        }
    }

    public User updateEmail(User user, String newEmail) {
        if (isAnExistingEmail(newEmail)) {
            throw new ExistingEmailException();
        } else if (newEmail.length() > 100) {
            throw new TooLongException();
        } else {
            user.setEmail(newEmail);
            logger.debug("Email updated to: " + user.getEmail());
            return userRepo.save(user);
        }
    }

    public User updatePassword(User user, String newPassword){
        if (newPassword.length() > 45) {
            throw new TooLongException();
        } else {
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
            throw new UsernameNotFoundException(username);
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> optUser = userRepo.findByEmail(email);
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    public boolean isAnExistingUsername(String username) {
        return userRepo.findByUsername(username).isPresent();
    }

    public boolean isAnExistingEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    // dev utilities
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void motherlode(int userId, double newBalance) {
        User user = userRepo.findById(userId).get();
        user.setBalance(newBalance);
        userRepo.save(user);
    }
}

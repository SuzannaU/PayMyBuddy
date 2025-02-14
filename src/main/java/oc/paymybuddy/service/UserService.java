package oc.paymybuddy.service;

import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    public User registerUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User updateUsername(User user, String newUsername) {
        // handle if newUsername already exists
        user.setUsername(newUsername);
        return userRepo.save(user);
    }

    public User updateEmail(User user, String newEmail) {
        // handle if newEmail already exists
        user.setEmail(newEmail);
        return userRepo.save(user);
    }

    public User updatePassword(User user, String newPassword) {
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        return userRepo.save(user);
    }

    public void updateBalances(User sender, User receiver, double amount) {
        sender.setBalance(sender.getBalance()-amount);
        receiver.setBalance(receiver.getBalance()+amount);
        userRepo.save(sender);
        userRepo.save(receiver);
    }

    public User getUserByUsername(String username) {
        Optional<User> optUser = userRepo.findByUsername(username);
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public int getUserIdByUsername(String username) {
        Optional<User> optUser = userRepo.findByUsername(username);
        if (optUser.isPresent()) {
            return optUser.get().getId();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public boolean isAnExistingUser(String username) {
        return userRepo.findByUsername(username) != null;
    }


    // dev utilities
    public User getUserById(Integer id) {
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


}

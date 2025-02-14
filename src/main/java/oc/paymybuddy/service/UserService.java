package oc.paymybuddy.service;

import oc.paymybuddy.model.User;
import oc.paymybuddy.model.UserRole;
import oc.paymybuddy.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepo userRepo;
    private UserRoleService userRoleService;

    public UserService(UserRepo userRepo, UserRoleService userRoleService) {
        this.userRepo = userRepo;
        this.userRoleService = userRoleService;
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

    public User updateBalance(User user, double balance) {
        user.setBalance(balance);
        return userRepo.save(user);
    }

    public int getUserIdByUsername(String username) {
        return userRepo.findByUsername(username).getId();
    }





    // dev utilities
    public User getUserById(Integer id) {
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null);
    }
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }



}

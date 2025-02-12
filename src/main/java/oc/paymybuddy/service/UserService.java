package oc.paymybuddy.service;

import oc.paymybuddy.model.Role;
import oc.paymybuddy.model.User;
import oc.paymybuddy.model.UserRole;
import oc.paymybuddy.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private UserRoleService userRoleService;

    private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder(10);

    public User register(User user) {
        user.setPassword(bCryptEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User getUserById(Integer id) {
        return userRepo.findById(id).get();
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Set<String> getRoles(User user) {
        List<UserRole> userRoles = userRoleService.getAllUserRolesByUser(user);
        Set<String> roleNames =userRoles.stream()
                .map(ur -> ur.getRole())
                .map(r->r.getRoleName())
                .collect(Collectors.toSet());
        return roleNames;
    }


}

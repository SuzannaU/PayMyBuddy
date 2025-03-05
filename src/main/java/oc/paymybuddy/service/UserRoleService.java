package oc.paymybuddy.service;

import oc.paymybuddy.model.Role;
import oc.paymybuddy.model.User;
import oc.paymybuddy.model.UserRole;
import oc.paymybuddy.model.UserRoleId;
import oc.paymybuddy.repository.UserRoleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    private static final Logger logger = LoggerFactory.getLogger(UserRoleService.class);
    private final UserRoleRepo userRoleRepo;

    public UserRoleService(UserRoleRepo userRoleRepo) {
        this.userRoleRepo = userRoleRepo;
    }

    public List<UserRole> getAllUserRolesByUser(User user) {
        return userRoleRepo.findAllByUser(user);
    }

    public void assignRoleToUser(User user, Role role) {
        logger.debug("SuperService/assignRoleToUser method called");
        UserRole userRole = new UserRole(new UserRoleId(user.getId(),role.getRoleName()));
        userRole.setUser(user);
        userRole.setRole(role);
        logger.debug("role name {}, user name {}", userRole.getRole().getRoleName(), userRole.getUser().getUsername());
        userRoleRepo.save(userRole);
    }
}

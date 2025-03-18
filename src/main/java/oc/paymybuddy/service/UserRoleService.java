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

    /**
     * Calls repo to get all the UserRole of one User
     * Called in UserDetailsService to determine the roles of a User
     *
     * @param user the User
     * @return List of the UserRoles
     */
    public List<UserRole> getAllUserRolesByUser(User user) {

        return userRoleRepo.findAllByUser(user);
    }

    /**
     * Creates a new UserRole and calls repo to save it
     * Called in ControllerService when creating a new User
     *
     * @param user the new User
     * @param role the Role that will be assigned to it
     */
    public void assignRoleToUser(User user, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUserRoleId(new UserRoleId(user.getId(), role.getRoleName()));
        userRole.setUser(user);
        userRole.setRole(role);
        logger.info("role name {} has been assigned to username {}",
                userRole.getRole().getRoleName(),
                userRole.getUser().getUsername());
        userRoleRepo.save(userRole);
    }
}

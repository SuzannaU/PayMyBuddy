package oc.paymybuddy.service;

import oc.paymybuddy.model.Role;
import oc.paymybuddy.repository.RoleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    /**
     * Calls repo to get Role object corresponding to a String from Roles enum
     * Called in ControllerService to assign a role to a new user
     *
     * @param roleName is the String of the role name
     * @return the corresponding Role
     */
    public Role getRoleByRoleName(String roleName) {
        logger.debug("RoleService/getRoleByRoleName method called");
        return roleRepo.findByRoleName(roleName);
    }
}

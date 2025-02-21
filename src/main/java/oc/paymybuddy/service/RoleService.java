package oc.paymybuddy.service;

import oc.paymybuddy.model.Role;
import oc.paymybuddy.repository.RoleRepo;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role getRoleByRoleName(String roleName) {
        return roleRepo.findByRoleName(roleName);
    }
}

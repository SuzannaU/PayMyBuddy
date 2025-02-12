package oc.paymybuddy.service;

import oc.paymybuddy.model.Role;
import oc.paymybuddy.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public Role getRoleById(Integer id) {
        return roleRepo.findById(id).get();
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }
}

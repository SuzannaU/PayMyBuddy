package oc.paymybuddy.service;

import oc.paymybuddy.model.UserRole;
import oc.paymybuddy.model.UserRoleId;
import oc.paymybuddy.repository.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepo userRoleRepo;

    public UserRole getUserRoleById(UserRoleId id) {

        return userRoleRepo.findById(id).get();
    }

    public List<UserRole> getAllUserRoles() {
        return userRoleRepo.findAll();
    }
}

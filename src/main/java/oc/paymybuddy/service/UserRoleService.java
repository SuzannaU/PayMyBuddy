package oc.paymybuddy.service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import oc.paymybuddy.model.User;
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

    public List<UserRole> getAllUserRolesByUser(User user) {
        return userRoleRepo.findAllByUser(user);
    }
}

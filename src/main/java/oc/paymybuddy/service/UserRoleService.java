package oc.paymybuddy.service;

import oc.paymybuddy.model.User;
import oc.paymybuddy.model.UserRole;
import oc.paymybuddy.repository.UserRoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    private UserRoleRepo userRoleRepo;

    public UserRoleService(UserRoleRepo userRoleRepo) {
        this.userRoleRepo = userRoleRepo;
    }

    public List<UserRole> getAllUserRolesByUser(User user) {
        return userRoleRepo.findAllByUser(user);
    }
}

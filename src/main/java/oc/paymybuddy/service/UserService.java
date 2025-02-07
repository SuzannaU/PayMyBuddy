package oc.paymybuddy.service;

import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public void getUsers() {
        List<User> users = userRepo.findAll();
        for (User user : users) {
            System.out.println(user.getEmail());
        }
    }
}

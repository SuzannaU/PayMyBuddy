package oc.paymybuddy.service;

import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    private BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder(10);

    public User register(User user) {
        user.setPassword(bCryptEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public List<User> getUsers(){
        return userRepo.findAll();
    }


}

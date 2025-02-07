package oc.paymybuddy;

import oc.paymybuddy.repository.UserRepo;
import oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public void getUsers() {

         userService.getUsers();
    }
}

package oc.paymybuddy.controller;

import oc.paymybuddy.exceptions.ExistingEmailException;
import oc.paymybuddy.exceptions.ExistingUsernameException;
import oc.paymybuddy.exceptions.TooLongException;
import oc.paymybuddy.model.User;
import oc.paymybuddy.service.SuperService;
import oc.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final SuperService superService;

    public UserController(UserService userService, SuperService superService) {
        this.userService = userService;
        this.superService = superService;
    }

    @GetMapping("/user")
    public String getUser() {
        logger.info("getUser");
        return "User";
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        logger.debug("getUsers");
        return userService.getAllUsers();
    }

    @PostMapping("/user")
    public User registerUser(@RequestBody User user) {
        logger.info("registerUser");
        try {
            return superService.registerUser(user);
        } catch (ExistingUsernameException e) {
            logger.error("Username already exists");
            return user;
        } catch (ExistingEmailException e2) {
            logger.error("Email already exists");
            return user;
        } catch (TooLongException e3){
            logger.error("Username must be under 45 characters\n Email must be under 100 characters\n Password must be under 45 characters");
            return user;
        }
    }

}

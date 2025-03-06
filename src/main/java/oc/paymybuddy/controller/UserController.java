package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.model.User;
import oc.paymybuddy.service.ControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final ControllerService controllerService;

    public UserController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/profile")
    public String getProfile(HttpServletRequest request, Model model) {
        String currentUsername = request.getUserPrincipal().getName();
        User user = controllerService.getUserByUsername(currentUsername);
        model.addAttribute("currentUser", user);
        model.addAttribute("currentUrl", request.getRequestURI());
        return "profile";
    }

    @PostMapping("/update-user")
    public String getProfile(@ModelAttribute("user") User user) {
        logger.debug("POST profile");
        controllerService.updateUser(user);
        return "redirect:/profile";
    }

    @GetMapping("/register")
    public String getRegisterFrom(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user) {
        logger.debug("POST register");
        controllerService.registerUser(user);
        return "redirect:/login";
    }

//    @GetMapping("/user")
//    public String getUser() {
//        logger.info("getUser");
//        return "User";
//    }
//
//    @GetMapping("/users")
//    public List<User> getUsers() {
//        logger.debug("getUsers");
//        return userService.getAllUsers();
//    }
//
//    @PostMapping("/user")
//    public User registerUser(@RequestBody User user) {
//        logger.info("registerUser");
//        try {
//            return superService.registerUser(user);
//        } catch (ExistingUsernameException e) {
//            logger.error("Username already exists");
//            return user;
//        } catch (ExistingEmailException e2) {
//            logger.error("Email already exists");
//            return user;
//        } catch (TooLongException e3){
//            logger.error("Username must be under 45 characters\n Email must be under 100 characters\n Password must be under 45 characters");
//            return user;
//        }
//    }

}

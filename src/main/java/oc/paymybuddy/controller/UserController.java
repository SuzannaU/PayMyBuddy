package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import oc.paymybuddy.exceptions.*;
import oc.paymybuddy.model.User;
import oc.paymybuddy.service.ControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/register")
    public String getRegisterFrom(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result) {
        logger.debug("POST register");
        if (result.hasErrors()) {
            return "register";
        }
        try {
            controllerService.registerUser(user);
        } catch (ExistingUsernameException e) {
            logger.error("Username already exists");
            result.addError(new FieldError(
                    "user", "username", "Username already exists"));
            return "register";
        } catch (ExistingEmailException e) {
            logger.error("Email already exists");
            result.addError(new FieldError(
                    "user", "email", "Email already exists"));
            return "register";
        } catch (TooLongUsernameException e) {
            logger.error("username is too long");
            result.addError(new FieldError(
                    "user", "username", "Username must be under 45 characters"));
            return "register";
        } catch (TooLongEmailException e) {
            logger.error("email is too long");
            result.addError(new FieldError(
                    "user", "email", "Email must be under 100 characters"));
            return "register";
        } catch (TooLongPasswordException e) {
            logger.error("password is too long");
            result.addError(new FieldError(
                    "user", "password", "Password must be under 45 characters"));
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String getProfile(HttpServletRequest request, Model model) {
        String currentUsername = request.getUserPrincipal().getName();
        User user = controllerService.getUserByUsername(currentUsername);
        model.addAttribute("currentUser", user);
        model.addAttribute("currentUrl", request.getRequestURI());
        return "profile";
    }

    @PostMapping("/update-username")
    public String updateUsername(@RequestParam String username, Model model, HttpServletRequest request) {
        logger.debug("POST update-username");
        String currentUsername = request.getUserPrincipal().getName();
        try {
            controllerService.updateUsername(currentUsername, username);
        } catch (ExistingUsernameException e) {
            logger.error("{} already exists", username);
            User user = controllerService.getUserByUsername(currentUsername);
            model.addAttribute("currentUser", user);
            model.addAttribute("currentUrl", request.getRequestURI());
            model.addAttribute("usernameError", "Ce nom d'utilisateur existe déjà.");
            return "profile";
        } catch (TooLongUsernameException e2) {
            logger.error("username is too long");
            User user = controllerService.getUserByUsername(currentUsername);
            model.addAttribute("currentUser", user);
            model.addAttribute("currentUrl", request.getRequestURI());
            model.addAttribute("usernameError", "Le nom d'utilisateur doit faire moins de 45 charactères");
            return "profile";
        }
        return "redirect:/profile";
    }

    @PostMapping("/update-email")
    public String updateEmail(@RequestParam String email, Model model, HttpServletRequest request) {
        logger.debug("POST update-email");
        String currentUsername = request.getUserPrincipal().getName();
        try {
            controllerService.updateEmail(currentUsername, email);
        } catch (ExistingEmailException e) {
            logger.error("{} already exists", email);
            User user = controllerService.getUserByUsername(currentUsername);
            model.addAttribute("currentUser", user);
            model.addAttribute("currentUrl", request.getRequestURI());
            model.addAttribute("emailError", "Ce mail existe déjà.");
            return "profile";
        } catch (TooLongException e2) {
            logger.error("email is too long");
            User user = controllerService.getUserByUsername(currentUsername);
            model.addAttribute("currentUser", user);
            model.addAttribute("currentUrl", request.getRequestURI());
            model.addAttribute("emailError", "L'email doit faire moins de 100 charactères");
            return "profile";
        }
        return "redirect:/profile";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String password, Model model, HttpServletRequest request) {
        logger.debug("POST update-password");
        String currentUsername = request.getUserPrincipal().getName();
        try {
            controllerService.updatePassword(currentUsername, password);
        } catch (TooLongPasswordException e) {
            logger.error("password is too long");
            User user = controllerService.getUserByUsername(currentUsername);
            model.addAttribute("currentUser", user);
            model.addAttribute("currentUrl", request.getRequestURI());
            model.addAttribute("passwordError", "Le mot de passe doit faire moins de 45 charactères");
            return "profile";
        }
        return "redirect:/profile";
    }

}

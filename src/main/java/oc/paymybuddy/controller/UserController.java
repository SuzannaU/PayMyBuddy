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

/**
 * This Controller handles requests related to Users
 */
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

    /**
     * Returns register page, with model corresponding to User class
     *
     * @param model for a User object
     * @return register template
     */
    @GetMapping("/register")
    public String getRegister(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    /**
     * Calls ControllerService to initiate the creation of a new User
     * Catches Exceptions and reloads page with error messages
     *
     * @param user   as ModelAttribute, with Validation
     * @param result to add error message in case of Validation errors, or Exceptions
     * @return redirects to /login
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result) {
        logger.info("POST register");
        if (result.hasErrors()) {
            return "register";
        }
        try {
            controllerService.registerUser(user);
        } catch (ExistingUsernameException e) {
            logger.error("Username already exists");
            result.addError(new FieldError(
                    "user", "username", "Ce nom d'utilisateur existe déjà."));
            return "register";
        } catch (ExistingEmailException e) {
            logger.error("Email already exists");
            result.addError(new FieldError(
                    "user", "email", "Ce mail existe déjà."));
            return "register";
        } catch (TooLongUsernameException e) {
            logger.error("username is too long");
            result.addError(new FieldError(
                    "user", "username", "Le nom d'utilisateur doit faire moins de 45 caractères"));
            return "register";
        } catch (TooLongEmailException e) {
            logger.error("email is too long");
            result.addError(new FieldError(
                    "user", "email", "L'email doit faire moins de 100 caractères"));
            return "register";
        } catch (TooLongPasswordException e) {
            logger.error("password is too long");
            result.addError(new FieldError(
                    "user", "password", "Le mot de passe doit faire moins de 45 caractères"));
            return "register";
        }
        return "redirect:/login";
    }

    /**
     * Returns the profile page, with model to specify the current URL
     *
     * @param request used to retrieve the current URL as well as the Principal
     * @param model   used to inject the current URL (for active link in navigation)
     * @return profile template
     */
    @GetMapping("/profile")
    public String getProfile(HttpServletRequest request, Model model) {
        String currentUsername = request.getUserPrincipal().getName();
        User user = controllerService.getUserByUsername(currentUsername);
        model.addAttribute("currentUser", user);
        model.addAttribute("currentUrl", request.getRequestURI());
        return "profile";
    }

    /**
     * Calls ControllerService to initiate the update of the username
     * Catches Exceptions and reloads the page with Error messages
     *
     * @param username retrieved as RequestParam
     * @param request used to retrieve the current URL as well as the Principal
     * @param model   used to inject the current URL
     * @return redirect to /profile
     */
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
            model.addAttribute("usernameError", "Le nom d'utilisateur doit faire moins de 45 caractères");
            return "profile";
        }
        return "redirect:/profile";
    }

    /**
     * Calls ControllerService to initiate the update of the email
     * Catches Exceptions and reloads the page with Error messages
     *
     * @param email retrieved as RequestParam
     * @param request used to retrieve the current URL as well as the Principal
     * @param model   used to inject the current URL
     * @return redirect to /profile
     */
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
        } catch (TooLongEmailException e2) {
            logger.error("email is too long");
            User user = controllerService.getUserByUsername(currentUsername);
            model.addAttribute("currentUser", user);
            model.addAttribute("currentUrl", request.getRequestURI());
            model.addAttribute("emailError", "L'email doit faire moins de 100 caractères");
            return "profile";
        }
        return "redirect:/profile";
    }
    /**
     * Calls ControllerService to initiate the update of the password
     * Catches Exceptions and reloads the page with Error messages
     *
     * @param password retrieved as RequestParam
     * @param request used to retrieve the current URL as well as the Principal
     * @param model   used to inject the current URL
     * @return redirect to /profile
     */
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
            model.addAttribute("passwordError", "Le mot de passe doit faire moins de 45 caractères");
            return "profile";
        }
        return "redirect:/profile";
    }

}

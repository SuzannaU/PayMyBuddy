package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.model.User;
import oc.paymybuddy.service.SuperService;
import oc.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@org.springframework.stereotype.Controller
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private final UserService userService;
    private final SuperService superService;

    public Controller(UserService userService, SuperService superService) {
        this.userService = userService;
        this.superService = superService;
    }

    @GetMapping({"", "/"})
    public String getHome() {
        return "index";
    }

//    @GetMapping("/transfer")
//    public String getTransfer(HttpServletRequest request, Model model) {
//        model.addAttribute("currentUrl", request.getRequestURI());
//        return "transfer";
//    }

    @GetMapping("/profile")
    public String getProfile(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "profile";
    }

    @GetMapping("/add-relation")
    public String getAddRelation(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "add-relation";
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
    public String register(@ModelAttribute("user") User user) {
        logger.debug("POST register");
        superService.registerUser(user);
        return "login";
    }

    @GetMapping("/error")
    public String getLoginError() {
        return "login-error";
    }


//
//    private StringBuffer getUsernamePasswordLoginInfo(Principal principal) {
//        StringBuffer usernameInfo = new StringBuffer();
//
//        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) principal);
//        if (token.isAuthenticated()) {
//            org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) token.getPrincipal();
//            usernameInfo.append("Welcome, " + u.getUsername());
//        } else {
//            usernameInfo.append("NA");
//        }
//        return usernameInfo;
//    }
}

package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.model.User;
import oc.paymybuddy.service.ControllerService;
import oc.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.stereotype.Controller
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private final UserService userService;
    private final ControllerService controllerService;

    public Controller(UserService userService, ControllerService controllerService) {
        this.userService = userService;
        this.controllerService = controllerService;
    }

    @GetMapping({"", "/"})
    public String getHome() {
        return "redirect:/transfer";
    }

    @GetMapping("/error")
    public String getLoginError() {
        return "login-error";
    }
}

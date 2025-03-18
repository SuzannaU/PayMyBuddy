package oc.paymybuddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This Controller handles requests for generic pages
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping({"", "/"})
    public String getHome() {
        return "redirect:/transfer";
    }

    @GetMapping("/error")
    public String getError() {
        return "error";
    }
}

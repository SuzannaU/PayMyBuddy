package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.service.ControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Controller
public class RelationController {
    private static final Logger logger = LoggerFactory.getLogger(RelationController.class);

    private final ControllerService controllerService;

    public RelationController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }


    @GetMapping("/add-relation")
    public String getAddRelation(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "add-relation";
    }

    @PostMapping("/add-relation")
    public String addRelation(@RequestParam String invitedUserEmail, HttpServletRequest request) {
        logger.debug("POST addRelation");
        String invitingUsername = request.getUserPrincipal().getName();
        controllerService.addRelation(invitingUsername, invitedUserEmail);
        return "redirect:/add-relation";
    }


    @GetMapping("/relations-usernames")
    public Set<String> getPrincipalRelationsUsernames(HttpServletRequest request) {
        return controllerService.getRelationsUsernamesByUsername(request.getUserPrincipal().getName());
    }



}

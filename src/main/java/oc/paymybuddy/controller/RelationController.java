package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.service.SuperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class RelationController {

    private SuperService superService;

    public RelationController(SuperService superService) {
        this.superService = superService;
    }

    @GetMapping("/relations-usernames")
    public Set<String> getPrincipalRelationsUsernames(HttpServletRequest request) {
        return superService.getRelationsUsernamesByUsername(request.getUserPrincipal().getName());
    }


}

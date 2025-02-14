package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.model.Relation;
import oc.paymybuddy.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class RelationController {

    @Autowired
    private RelationService relationService;

    @GetMapping("/relations")
    public List<Relation> getAllRelations(){
        return relationService.getAllRelations();
    }

    @GetMapping("/relations-usernames")
    public Set<String> getRelationsUsernames(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return relationService.getRelationsUsernames(username);
    }


}

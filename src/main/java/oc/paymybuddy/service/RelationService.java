package oc.paymybuddy.service;

import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.RelationId;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.RelationRepo;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class RelationService {

    private RelationRepo relationRepo;
    private UserService userService;

    public RelationService(RelationRepo relationRepo, UserService userService) {
        this.relationRepo = relationRepo;
        this.userService = userService;
    }

    public Relation addRelation(Principal principal, User user) {
        // need to check if user is in db
        Relation relation = new Relation();
        relation.setInvitingUser((User) principal);
        relation.setInvitedUser(user);
        return relationRepo.save(relation);
    }

    public Set<String> getRelationsUsernames(String username) {
        int userId = userService.getUserIdByUsername(username);
        return relationRepo.getRelationsByUserId(userId, userId).stream()
                .map(r->r.getUsers())
                .flatMap(Collection::stream)
                .map(u->u.getUsername())
                .filter(Predicate.not(s->s.equals(username)))
                .collect(Collectors.toSet());
    }

    public List<Relation> getAllRelations() {
        return relationRepo.findAll();
    }
}

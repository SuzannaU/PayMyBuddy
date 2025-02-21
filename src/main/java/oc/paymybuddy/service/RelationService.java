package oc.paymybuddy.service;

import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.RelationId;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.RelationRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(RelationService.class);
    private RelationRepo relationRepo;

    public RelationService(RelationRepo relationRepo) {
        this.relationRepo = relationRepo;
    }

    public Relation addRelation(User invitingUser, User invitedUser) {
            Relation relation = new Relation();
            relation.setInvitingUser(invitingUser);
            relation.setInvitedUser(invitedUser);
            logger.debug("Adding relation between {} and {}", invitingUser.getUsername(), invitedUser.getUsername());
            return relationRepo.save(relation);
    }

    public Set<String> getRelationsUsernamesByUser(User user) {
        return relationRepo.findAllByInvitingUserOrInvitedUser(user, user).stream()
                .map(r->r.getUsers())
                .flatMap(Collection::stream)
                .map(u->u.getUsername())
                .filter(Predicate.not(s->s.equals(user.getUsername())))
                .collect(Collectors.toSet());
    }
}

package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.ExistingRelationException;
import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.RelationId;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.RelationRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
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

    /**
     * Creates a new relation and calls repo to save it
     *
     * @param invitingUser the inviting user
     * @param invitedUser  the invited user
     * @return the saved relation
     * @throws ExistingRelationException
     */
    public Relation addRelation(User invitingUser, User invitedUser) {
        Set<String> existingRelations = getRelationsUsernamesByUser(invitingUser);
        if (existingRelations.contains(invitedUser.getUsername())) {
            logger.error("this relation already exists");
            throw new ExistingRelationException();
        }
        Relation relation = new Relation();
        relation.setRelationId(new RelationId(invitingUser.getId(), invitedUser.getId()));
        relation.setInvitingUser(invitingUser);
        relation.setInvitedUser(invitedUser);
        logger.info("Adding relation between {} and {}",
                relation.getInvitingUser().getUsername(), relation.getInvitedUser().getUsername());
        return relationRepo.save(relation);
    }

    /**
     * Calls repo to get relations usernames by user.
     *
     * @param user the user
     * @return the relations usernames for that user
     */
    public Set<String> getRelationsUsernamesByUser(User user) {
        return relationRepo.findAllByInvitingUserOrInvitedUser(user, user).stream()
                .map(r -> r.getUsers())
                .flatMap(Collection::stream)
                .map(u -> u.getUsername())
                .filter(Predicate.not(s -> s.equals(user.getUsername())))
                .collect(Collectors.toSet());
    }
}

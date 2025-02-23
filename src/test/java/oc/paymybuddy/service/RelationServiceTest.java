package oc.paymybuddy.service;

import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.RelationRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RelationServiceTest {

    @MockitoBean
    private RelationRepo relationRepo;

    @Autowired
    private RelationService relationService;
    private User invitingUser;
    private User invitedUser;
    private User anotherUser;

    @BeforeEach
    private void init() {
        invitingUser = new User();
        invitingUser.setUsername("invitingUser");
        invitedUser = new User();
        invitedUser.setUsername("invitedUser");
        anotherUser = new User();
        anotherUser.setUsername("anotherUser");
    }

    @Test
    public void addRelation_withCorrectParameters_savesRelation(){

        when(relationRepo.save(any(Relation.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        Relation relation = relationService.addRelation(invitingUser, invitedUser);

        assertNotNull(relation);
        assertEquals(relation.getInvitingUser().getUsername(), invitingUser.getUsername());
        assertEquals(relation.getInvitedUser().getUsername(), invitedUser.getUsername());
        assertTrue(relation.getUsers().contains(invitingUser)
                && relation.getUsers().contains(invitedUser));
        verify(relationRepo).save(any(Relation.class));
    }

    @Test
    public void getRelationsUsernamesByUser_withCorrectParameters_returnsRelations(){
        Relation relation1 = new Relation();
        relation1.setInvitingUser(invitingUser);
        relation1.setInvitedUser(invitedUser);
        Relation relation2 = new Relation();
        relation2.setInvitingUser(invitingUser);
        relation2.setInvitedUser(anotherUser);
        List<Relation> relations = Arrays.asList(relation1, relation2);

        when(relationRepo.findAllByInvitingUserOrInvitedUser(any(), any())).thenReturn(relations);

        Set<String> relationsUsernames = relationService.getRelationsUsernamesByUser(invitingUser);

        assertNotNull(relationsUsernames);
        assertFalse(relationsUsernames.contains(invitingUser.getUsername()));
        assertTrue(relationsUsernames.contains(invitedUser.getUsername()));
        assertTrue(relationsUsernames.contains(anotherUser.getUsername()));
        verify(relationRepo).findAllByInvitingUserOrInvitedUser(any(), any());
    }
}

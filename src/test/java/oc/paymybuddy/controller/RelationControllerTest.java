package oc.paymybuddy.controller;

import oc.paymybuddy.exceptions.ExistingRelationException;
import oc.paymybuddy.exceptions.UserNotFoundException;
import oc.paymybuddy.model.Relation;
import oc.paymybuddy.service.ControllerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RelationController.class)
public class RelationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ControllerService controllerService;


    @Test
    @WithMockUser(roles = "USER")
    void getAddRelation_withAuthenticatedUser_returnsAddRelationView() throws Exception {

        this.mockMvc.perform(get("/add-relation"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add-relation"))
                .andExpect(content().string(containsString("Ajouter relation")));
    }

    @Test
    @WithAnonymousUser
    void getAddRelation_withAnonymousUser_returnsUnauthorized() throws Exception {

        this.mockMvc.perform(get("/add-relation"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void postAddRelation_withCorrectParameters_callsServiceAndRedirects() throws Exception {

        when(controllerService.addRelation(anyString(), anyString())).thenReturn(new Relation());

        this.mockMvc.perform(post("/add-relation")
                        .param("invitedUserEmail", "user2@example.com")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/add-relation"));

        verify(controllerService).addRelation(anyString(), anyString());
    }

    @Test
    @WithAnonymousUser
    void postAddRelation_withAnonymousUser_returnsUnauthorized() throws Exception {

        this.mockMvc.perform(post("/add-relation")
                        .param("invitedUserEmail", "user2@example.com")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(controllerService, never()).addRelation(anyString(), anyString());
    }

    @Test
    @WithMockUser(roles = "USER")
    void postAddRelation_withUserNotFoundException_returnsErrorMessage() throws Exception {

        doThrow(new UserNotFoundException()).when(controllerService).addRelation(anyString(), anyString());

        this.mockMvc.perform(post("/add-relation")
                        .param("invitedUserEmail", "user2@example.com")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add-relation"))
                .andExpect(model().attribute("relationError","L'email n'existe pas"));

        verify(controllerService).addRelation(anyString(), anyString());
    }

    @Test
    @WithMockUser(roles = "USER")
    void postAddRelation_withExistingRelationException_returnsErrorMessage() throws Exception {

        doThrow(new ExistingRelationException()).when(controllerService).addRelation(anyString(), anyString());

        this.mockMvc.perform(post("/add-relation")
                        .param("invitedUserEmail", "user2@example.com")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add-relation"))
                .andExpect(model().attribute("relationError","Cet utilisateur est déjà une relation."));

        verify(controllerService).addRelation(anyString(), anyString());
    }


}

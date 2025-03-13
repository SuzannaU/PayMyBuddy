package oc.paymybuddy.integration;

import oc.paymybuddy.service.ControllerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/paymybuddy_test?serverTimezone=UTC",
        "spring.datasource.driverClassname=com.mysql.cj.jdbc.Driver",
        "spring.datasource.username=root",
        "spring.datasource.password=root"
})
public class RelationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ControllerService controllerService;

    @Test
    @WithUserDetails("user2@example.com")
    public void addRelation_withCorrectParameters_savesRelation() throws Exception {

        Set<String> initialRelations = controllerService.getRelationsUsernamesByUsername("user2");
        assertThat(initialRelations).doesNotContain("user3");

        this.mockMvc.perform(post("/add-relation")
                        .param("invitedUserEmail", "user3@example.com")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/add-relation"));

        Set<String> result = controllerService.getRelationsUsernamesByUsername("user2");

        assertThat(result).contains("user3");
    }

    @Test
    @WithUserDetails("user2@example.com")
    public void postAddRelation_withNotExistingUser_returnsErrorMessage() throws Exception {

        this.mockMvc.perform(post("/add-relation")
                        .param("invitedUserEmail", "not-existing@example.com")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add-relation"))
                .andExpect(model().attribute("relationError", "L'email n'existe pas"));
    }

    @Test
    @WithUserDetails("user2@example.com")
    public void addRelation_withExistingRelation_returnsErrorMessage() throws Exception {

        Set<String> initialRelations = controllerService.getRelationsUsernamesByUsername("user2");
        assertThat(initialRelations).contains("user1");

        this.mockMvc.perform(post("/add-relation")
                        .param("invitedUserEmail", "user1@example.com")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add-relation"))
                .andExpect(model().attribute("relationError", "Cet utilisateur est déjà une relation."));

        Set<String> result = controllerService.getRelationsUsernamesByUsername("user2");

        assertThat(result).contains("user1");
    }
}

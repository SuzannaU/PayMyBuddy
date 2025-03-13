package oc.paymybuddy.integration;

import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.UserRepo;
import oc.paymybuddy.service.ControllerService;
import oc.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
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
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ControllerService controllerService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void register_withCorrectParameters_savesUser() throws Exception {
        User validUser = new User();
        validUser.setUsername("username");
        validUser.setEmail("email");
        validUser.setPassword("password");

        this.mockMvc.perform(post("/register")
                        .flashAttr("user", validUser)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        assertTrue(userRepo.findByUsername("username").isPresent());
    }

    @Test
    public void register_withExistingUsername_returnsError() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("user1");
        existingUser.setEmail("email");
        existingUser.setPassword("password");

        this.mockMvc.perform(post("/register")
                        .flashAttr("user", existingUser)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "username"));

        //Checks that user1 email has not been replaced
        assertNotEquals("email", userService.getUserByUsername("user1").getEmail());
    }

    @Test
    public void register_withExistingEmail_returnsError() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("username");
        existingUser.setEmail("user1@example.com");
        existingUser.setPassword("password");

        this.mockMvc.perform(post("/register")
                        .flashAttr("user", existingUser)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));

        //Checks that user1 username has not been replaced
        assertNotEquals("username", userService.getUserByEmail("user1@example.com").getUsername());
    }

    @Test
    public void register_withTooLongUsername_returnsError() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("u".repeat(46));
        existingUser.setEmail("email");
        existingUser.setPassword("password");

        this.mockMvc.perform(post("/register")
                        .flashAttr("user", existingUser)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "username"));

        //Checks that user1 email has not been replaced
        assertNotEquals("email", userService.getUserByUsername("user1").getEmail());
    }

    @Test
    public void register_withTooLongEmail_returnsError() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("username");
        existingUser.setEmail("e".repeat(101));
        existingUser.setPassword("password");

        this.mockMvc.perform(post("/register")
                        .flashAttr("user", existingUser)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));

        //Checks that user1 username has not been replaced
        assertNotEquals("username", userService.getUserByEmail("user1@example.com").getUsername());
    }

    @Test
    public void register_withTooLongPassword_returnsError() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("username");
        existingUser.setEmail("email");
        existingUser.setPassword("p".repeat(46));

        this.mockMvc.perform(post("/register")
                        .flashAttr("user", existingUser)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "password"));

        //Checks that user1 username has not been replaced
        assertNotEquals("username", userService.getUserByEmail("user1@example.com").getUsername());
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void updateUsername_withCorrectParameters_updatesUsername() throws Exception {

        assertNotEquals("newUsername", userService.getUserByEmail("user1@example.com").getUsername());

        this.mockMvc.perform(post("/update-username")
                        .param("username", "newUsername")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        assertEquals("newUsername", userService.getUserByEmail("user1@example.com").getUsername());
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void updateUsername_withExistingUsername_returnsError() throws Exception {

        this.mockMvc.perform(post("/update-username")
                        .param("username", "user2")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("usernameError", "Ce nom d'utilisateur existe déjà."));

        assertNotEquals("user2", userService.getUserByEmail("user1@example.com").getUsername());
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void updateUsername_withTooLongUsername_returnsError() throws Exception {

        this.mockMvc.perform(post("/update-username")
                        .param("username", "u".repeat(46))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("usernameError", "Le nom d'utilisateur doit faire moins de 45 caractères"));

        assertNotEquals("u".repeat(46), userService.getUserByEmail("user1@example.com").getUsername());
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void updateEmail_withCorrectParameters_updatesEmail() throws Exception {

        assertNotEquals("newEmail", userService.getUserByUsername("user1").getEmail());

        this.mockMvc.perform(post("/update-email")
                        .param("email", "newEmail")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        assertEquals("newEmail", userService.getUserByUsername("user1").getEmail());
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void updateEmail_withExistingEmail_returnsError() throws Exception {

        this.mockMvc.perform(post("/update-email")
                        .param("email", "user2@example.com")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("emailError", "Ce mail existe déjà."));

        assertNotEquals("user2@example.com", userService.getUserByUsername("user1").getEmail());
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void updateEmail_withTooLongEmail_returnsError() throws Exception {

        this.mockMvc.perform(post("/update-email")
                        .param("email", "e".repeat(101))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("emailError", "L'email doit faire moins de 100 caractères"));

        assertNotEquals("e".repeat(101), userService.getUserByUsername("user1").getEmail());
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void updatePassword_withCorrectParameters_updatesPassword() throws Exception {

        assertFalse(passwordEncoder.matches(
                "newPassword", userService.getUserByUsername("user1").getPassword()));

        this.mockMvc.perform(post("/update-password")
                        .param("password", "newPassword")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        assertTrue(passwordEncoder.matches(
                "newPassword", userService.getUserByUsername("user1").getPassword()));
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void updatePassword_withTooLongPassword_returnsError() throws Exception {

        this.mockMvc.perform(post("/update-password")
                        .param("password", "p".repeat(46))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("passwordError", "Le mot de passe doit faire moins de 45 caractères"));

        assertFalse(passwordEncoder.matches(
                "p".repeat(46), userService.getUserByUsername("user1").getPassword()));
    }
}

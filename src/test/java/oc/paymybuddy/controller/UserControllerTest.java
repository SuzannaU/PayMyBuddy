package oc.paymybuddy.controller;

import oc.paymybuddy.exceptions.*;
import oc.paymybuddy.model.User;
import oc.paymybuddy.service.ControllerService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockitoBean
    private ControllerService controllerService;

    @Nested
    @Tag("register tests")
    class RegisterTests {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @WithMockUser(roles = "USER")
        public void postRegister_withCorrectParameters_callsServiceAndRedirects() throws Exception {

            User validUser = new User();
            validUser.setUsername("username");
            validUser.setEmail("email");
            validUser.setPassword("password");

            when(controllerService.registerUser(any(User.class)))
                    .thenReturn(new User());

            this.mockMvc.perform(post("/register")
                            .flashAttr("user", validUser)
                            .with(csrf().asHeader()))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login"));

            verify(controllerService).registerUser(any(User.class));
        }

        @Test
        @WithMockUser(roles = "USER")
        public void postRegister_withUsernameValidationError_returnsRegisterView() throws Exception {

            User invalidUser = new User();
            invalidUser.setUsername("");
            invalidUser.setEmail("email");
            invalidUser.setPassword("password");

            this.mockMvc.perform(post("/register")
                            .flashAttr("user", invalidUser)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("register"))
                    .andExpect(model().attributeHasFieldErrors("user", "username"));

            verifyNoInteractions(controllerService);
        }

        @Test
        @WithMockUser(roles = "USER")
        public void postRegister_withEmailValidationError_returnsRegisterView() throws Exception {

            User invalidUser = new User();
            invalidUser.setUsername("username");
            invalidUser.setEmail("");
            invalidUser.setPassword("password");

            this.mockMvc.perform(post("/register")
                            .flashAttr("user", invalidUser)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("register"))
                    .andExpect(model().attributeHasFieldErrors("user", "email"));

            verifyNoInteractions(controllerService);
        }

        @Test
        @WithMockUser(roles = "USER")
        public void postRegister_withPasswordValidationError_returnsRegisterView() throws Exception {

            User invalidUser = new User();
            invalidUser.setUsername("username");
            invalidUser.setEmail("email");
            invalidUser.setPassword("");

            this.mockMvc.perform(post("/register")
                            .flashAttr("user", invalidUser)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("register"))
                    .andExpect(model().attributeHasFieldErrors("user", "password"));

            verifyNoInteractions(controllerService);
        }

        @Test
        @WithMockUser(roles = "USER")
        public void postRegister_withExistingUsernameException_returnsRegisterView() throws Exception {

            User user = new User();
            user.setUsername("username");
            user.setEmail("email");
            user.setPassword("password");
            doThrow(ExistingUsernameException.class).when(controllerService).registerUser(any(User.class));

            this.mockMvc.perform(post("/register")
                            .flashAttr("user", user)
                            .with(csrf().asHeader()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("register"))
                    .andExpect(model().attributeHasFieldErrors("user", "username"));

            verify(controllerService).registerUser(any(User.class));
        }

        @Test
        @WithMockUser(roles = "USER")
        public void postRegister_withExistingEmailException_returnsRegisterView() throws Exception {

            User user = new User();
            user.setUsername("username");
            user.setEmail("email");
            user.setPassword("password");
            doThrow(ExistingEmailException.class).when(controllerService).registerUser(any(User.class));

            this.mockMvc.perform(post("/register")
                            .flashAttr("user", user)
                            .with(csrf().asHeader()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("register"))
                    .andExpect(model().attributeHasFieldErrors("user", "email"));

            verify(controllerService).registerUser(any(User.class));
        }

        @Test
        @WithMockUser(roles = "USER")
        public void postRegister_withTooLongUsernameException_returnsRegisterView() throws Exception {

            User user = new User();
            user.setUsername("username");
            user.setEmail("email");
            user.setPassword("password");
            doThrow(TooLongUsernameException.class).when(controllerService).registerUser(any(User.class));

            this.mockMvc.perform(post("/register")
                            .flashAttr("user", user)
                            .with(csrf().asHeader()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("register"))
                    .andExpect(model().attributeHasFieldErrors("user", "username"));

            verify(controllerService).registerUser(any(User.class));
        }

        @Test
        @WithMockUser(roles = "USER")
        public void postRegister_withTooLongEmailException_returnsRegisterView() throws Exception {

            User user = new User();
            user.setUsername("username");
            user.setEmail("email");
            user.setPassword("password");
            doThrow(TooLongEmailException.class).when(controllerService).registerUser(any(User.class));

            this.mockMvc.perform(post("/register")
                            .flashAttr("user", user)
                            .with(csrf().asHeader()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("register"))
                    .andExpect(model().attributeHasFieldErrors("user", "email"));

            verify(controllerService).registerUser(any(User.class));
        }

        @Test
        @WithMockUser(roles = "USER")
        public void postRegister_withTooLongPasswordException_returnsRegisterView() throws Exception {

            User user = new User();
            user.setUsername("username");
            user.setEmail("email");
            user.setPassword("password");
            doThrow(TooLongPasswordException.class).when(controllerService).registerUser(any(User.class));

            this.mockMvc.perform(post("/register")
                            .flashAttr("user", user)
                            .with(csrf().asHeader()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("register"))
                    .andExpect(model().attributeHasFieldErrors("user", "password"));

            verify(controllerService).registerUser(any(User.class));
        }
    }

    @Nested
    @Tag("profile tests")
    class ProfileTests {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @WithMockUser(roles = "USER")
        public void getProfile_withAuthenticatedUser_returnsProfileView() throws Exception {

            when(controllerService.getUserByUsername(anyString())).thenReturn(new User());

            this.mockMvc.perform(get("/profile"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(content().string(containsString("Profil")));

            verify(controllerService).getUserByUsername(anyString());
        }

        @Test
        @WithAnonymousUser
        public void getProfile_withAnonymousUser_returnsUnauthorized() throws Exception {

            this.mockMvc.perform(get("/profile"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "USER")
        public void updateUsername_withCorrectParameters_redirectsToProfileView() throws Exception {

            when(controllerService.updateUsername(anyString(), anyString())).thenReturn(new User());

            this.mockMvc.perform(post("/update-username")
                            .param("username", "username")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/profile"));

            verify(controllerService).updateUsername(anyString(), anyString());
        }

        @Test
        @WithMockUser(roles = "USER")
        public void updateUsername_withExistingUsernameException_returnsProfileViewWithErrors() throws Exception {

            when(controllerService.getUserByUsername(anyString())).thenReturn(new User());
            doThrow(ExistingUsernameException.class).when(controllerService).updateUsername(anyString(), anyString());

            this.mockMvc.perform(post("/update-username")
                            .param("username", "username")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(model().attribute("usernameError", "Ce nom d'utilisateur existe déjà."));

            verify(controllerService).getUserByUsername(anyString());
            verify(controllerService).updateUsername(anyString(), anyString());
        }

        @Test
        @WithMockUser(roles = "USER")
        public void updateUsername_withTooLongUsernameException_returnsProfileViewWithErrors() throws Exception {

            when(controllerService.getUserByUsername(anyString())).thenReturn(new User());
            doThrow(TooLongUsernameException.class).when(controllerService).updateUsername(anyString(), anyString());

            this.mockMvc.perform(post("/update-username")
                            .param("username", "username")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(model().attribute("usernameError", "Le nom d'utilisateur doit faire moins de 45 caractères"));

            verify(controllerService).getUserByUsername(anyString());
            verify(controllerService).updateUsername(anyString(), anyString());
        }

        @Test
        @WithMockUser(roles = "USER")
        public void updateEmail_withCorrectParameters_redirectsToProfileView() throws Exception {

            when(controllerService.updateEmail(anyString(), anyString())).thenReturn(new User());

            this.mockMvc.perform(post("/update-email")
                            .param("email", "email")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/profile"));

            verify(controllerService).updateEmail(anyString(), anyString());
        }

        @Test
        @WithMockUser(roles = "USER")
        public void updateEmail_withExistingEmailException_returnsProfileViewWithErrors() throws Exception {

            when(controllerService.getUserByUsername(anyString())).thenReturn(new User());
            doThrow(ExistingEmailException.class).when(controllerService).updateEmail(anyString(), anyString());

            this.mockMvc.perform(post("/update-email")
                            .param("email", "email")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(model().attribute("emailError", "Ce mail existe déjà."));

            verify(controllerService).getUserByUsername(anyString());
            verify(controllerService).updateEmail(anyString(), anyString());
        }

        @Test
        @WithMockUser(roles = "USER")
        public void updateEmail_withTooLongEmailException_returnsProfileViewWithErrors() throws Exception {

            when(controllerService.getUserByUsername(anyString())).thenReturn(new User());
            doThrow(TooLongEmailException.class).when(controllerService).updateEmail(anyString(), anyString());

            this.mockMvc.perform(post("/update-email")
                            .param("email", "email")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(model().attribute("emailError", "L'email doit faire moins de 100 caractères"));

            verify(controllerService).getUserByUsername(anyString());
            verify(controllerService).updateEmail(anyString(), anyString());
        }

        @Test
        @WithMockUser(roles = "USER")
        public void updatePassword_withCorrectParameters_redirectsToProfileView() throws Exception {

            when(controllerService.updatePassword(anyString(), anyString())).thenReturn(new User());

            this.mockMvc.perform(post("/update-password")
                            .param("password", "password")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/profile"));

            verify(controllerService).updatePassword(anyString(), anyString());
        }

        @Test
        @WithMockUser(roles = "USER")
        public void updatePassword_withTooLongUsernameException_returnsProfileViewWithErrors() throws Exception {

            when(controllerService.getUserByUsername(anyString())).thenReturn(new User());
            doThrow(TooLongPasswordException.class).when(controllerService).updatePassword(anyString(), anyString());

            this.mockMvc.perform(post("/update-password")
                            .param("password", "password")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(model().attribute("passwordError", "Le mot de passe doit faire moins de 45 caractères"));

            verify(controllerService).getUserByUsername(anyString());
            verify(controllerService).updatePassword(anyString(), anyString());
        }


    }
}

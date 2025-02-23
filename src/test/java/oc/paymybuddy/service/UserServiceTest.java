package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.ExistingEmailException;
import oc.paymybuddy.exceptions.ExistingUsernameException;
import oc.paymybuddy.exceptions.TooLongException;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockitoBean
    private UserRepo userRepo;
    @MockitoBean
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    private User user1;
    private User user2;

    @BeforeEach
    private void init() {
        user1 = new User();
        user2 = new User();
    }

    @Test
    public void registerUser_withCorrectParameters_returnsUser() {
        user1.setUsername("username1");
        user1.setEmail("email1");
        user1.setPassword("password1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.registerUser(user1);

        assertNotNull(user);
        assertEquals("password", user.getPassword());
        verify(passwordEncoder).encode(anyString());
        verify(userRepo).findByUsername(anyString());
        verify(userRepo).findByEmail(anyString());
        verify(userRepo).save(any());
    }

    @Test
    public void registerUser_withExistingUsername_throwsException() {
        user1.setUsername("username1");
        user1.setEmail("email1");
        user1.setPassword("password1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user1));

        assertThrows(ExistingUsernameException.class, () -> userService.registerUser(user1));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void registerUser_withExistingEmail_throwsException() {
        user1.setUsername("username1");
        user1.setEmail("email1");
        user1.setPassword("password1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user1));

        assertThrows(ExistingEmailException.class, () -> userService.registerUser(user1));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void registerUser_withTooLongUsername_throwsException() {
        user1.setUsername("a".repeat(46));
        user1.setEmail("email1");
        user1.setPassword("password1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(TooLongException.class, () -> userService.registerUser(user1));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void registerUser_withTooLongEmail_throwsException() {
        user1.setUsername("username1");
        user1.setEmail("a".repeat(101));
        user1.setPassword("password1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(TooLongException.class, () -> userService.registerUser(user1));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void registerUser_withTooLongPassword_throwsException() {
        user1.setUsername("username1");
        user1.setEmail("email1");
        user1.setPassword("a".repeat(46));
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(TooLongException.class, () -> userService.registerUser(user1));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updateUsername_withCorrectParameters_returnsUser() {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.updateUsername(user1, "newUsername");

        assertNotNull(user);
        assertEquals("newUsername", user.getUsername());
        verify(userRepo).findByUsername(anyString());
        verify(userRepo).save(any());
    }

    @Test
    public void updateUsername_withExistingUsername_throwsException() {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user1));

        assertThrows(ExistingUsernameException.class,
                () -> userService.updateUsername(user1, "newUsername"));

        verify(userRepo).findByUsername(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updateUsername_withTooLongUsername_throwsException() {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(TooLongException.class,
                () -> userService.updateUsername(user1, "a".repeat(46)));

        verify(userRepo).findByUsername(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updateEmail_withCorrectParameters_returnsUser() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.updateEmail(user1, "newEmail");

        assertNotNull(user);
        assertEquals("newEmail", user.getEmail());
        verify(userRepo).findByEmail(anyString());
        verify(userRepo).save(any());

    }

    @Test
    public void updateEmail_withExistingEmail_throwsException() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user1));

        assertThrows(ExistingEmailException.class,
                () -> userService.updateEmail(user1, "newEmail"));

        verify(userRepo).findByEmail(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updateEmail_withTooLongEmail_throwsException() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(TooLongException.class,
                () -> userService.updateEmail(user1, "a".repeat(101)));

        verify(userRepo).findByEmail(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updatePassword_withCorrectParameters_returnsUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.updatePassword(user1, "newPassword");

        assertNotNull(user);
        assertEquals("encodedPassword", user.getPassword());
        verify(passwordEncoder).encode(anyString());
        verify(userRepo).save(any());
    }

    @Test
    public void updatePassword_withTooLongPassword_throwsException() {

        assertThrows(TooLongException.class, () -> userService.updatePassword(user1, "a".repeat(46)));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updateBalances_withCorrectParameters_updatesUsers() {
        user1.setBalance(10);
        user2.setBalance(20);
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        userService.updateBalances(user1, user2, 5);

        assertEquals(5, user1.getBalance());
        assertEquals(25, user2.getBalance());
        verify(userRepo, times(2)).save(any());
    }

    @Test
    public void getUserByUsername_withCorrectParameters_returnsUser() {
        user1.setUsername("username1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user1));

        User user = userService.getUserByUsername("username1");

        assertNotNull(user);
        assertEquals(user1.getUsername(), user.getUsername());
        verify(userRepo).findByUsername(anyString());
    }

    @Test
    public void getUserByUsername_withNonExistingUsername_throwsException() {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,() -> userService.getUserByUsername("nonExistingUsername"));

        verify(userRepo).findByUsername(anyString());
    }

    @Test
    public void isAnExistingUsername_withExistingUsername_returnsTrue() {
        user1.setUsername("username1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user1));
        assertTrue(userService.isAnExistingUsername("username1"));
    }

    @Test
    public void isAnExistingUsername_withNonExistingUsername_returnsFalse() {
        user1.setUsername("username1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        assertFalse(userService.isAnExistingUsername("username1"));
    }

    @Test
    public void isAnExistingEmail_withExistingEmail_returnsTrue() {
        user1.setEmail("email1");
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user1));
        assertTrue(userService.isAnExistingEmail("email1"));
    }

    @Test
    public void isAnExistingEmail_withNonExistingEmail_returnsFalse() {
        user1.setEmail("email1");
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        assertFalse(userService.isAnExistingEmail("email1"));
    }
}

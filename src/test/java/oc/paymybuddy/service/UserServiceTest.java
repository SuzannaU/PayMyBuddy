package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.*;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void setup() {
        user1 = new User();
        user2 = new User();
    }

    @Test
    public void registerUser_withCorrectParameters_callsRepoAndReturnsUser() {
        user1.setUsername("username1");
        user1.setEmail("email1");
        user1.setPassword("password1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.registerUser(user1);

        assertNotNull(user);
        assertEquals("username1", user.getUsername());
        assertEquals("email1", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepo).findByUsername(anyString());
        verify(userRepo).findByEmail(anyString());
        verify(passwordEncoder).encode(anyString());
        verify(userRepo).save(any());
    }

    @Test
    public void registerUser_withExistingUsername_doesNotCallAndThrowsException() {
        user1.setUsername("username1");
        user1.setEmail("email1");
        user1.setPassword("password1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user1));

        assertThrows(ExistingUsernameException.class, () -> userService.registerUser(user1));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void registerUser_withExistingEmail_doesNotCallAndThrowsException() {
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
    public void registerUser_withTooLongUsername_doesNotCallAndThrowsException() {
        user1.setUsername("a".repeat(46));
        user1.setEmail("email1");
        user1.setPassword("password1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(TooLongUsernameException.class, () -> userService.registerUser(user1));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void registerUser_withTooLongEmail_doesNotCallAndThrowsException() {
        user1.setUsername("username1");
        user1.setEmail("a".repeat(101));
        user1.setPassword("password1");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(TooLongEmailException.class, () -> userService.registerUser(user1));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void registerUser_withTooLongPassword_doesNotCallAndThrowsException() {
        user1.setUsername("username1");
        user1.setEmail("email1");
        user1.setPassword("a".repeat(46));
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(TooLongPasswordException.class, () -> userService.registerUser(user1));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updateUsername_withCorrectParameters_callsRepoAndReturnsUser() {
        when(userRepo.findByUsername("newUsername")).thenReturn(Optional.empty());
        when(userRepo.findByUsername("username1")).thenReturn(Optional.of(user1));
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.updateUsername("username1", "newUsername");

        assertNotNull(user);
        assertEquals("newUsername", user.getUsername());
        verify(userRepo, times(2)).findByUsername(anyString());
        verify(userRepo).save(any());
    }

    @Test
    public void updateUsername_withExistingUsername_doesNotCallAndThrowsException() {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user1));

        assertThrows(ExistingUsernameException.class,
                () -> userService.updateUsername("username1", "newUsername"));

        verify(userRepo).findByUsername(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updateUsername_withTooLongUsername_doesNotCallAndThrowsException() {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(TooLongUsernameException.class,
                () -> userService.updateUsername("username1", "a".repeat(46)));

        verify(userRepo).findByUsername(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updateEmail_withCorrectParameters_callsRepoAndReturnsUser() {
        when(userRepo.findByEmail("newEmail")).thenReturn(Optional.empty());
        when(userRepo.findByUsername("username1")).thenReturn(Optional.of(user1));
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.updateEmail("username1", "newEmail");

        assertNotNull(user);
        assertEquals("newEmail", user.getEmail());
        verify(userRepo).findByEmail(anyString());
        verify(userRepo).findByUsername(anyString());
        verify(userRepo).save(any());

    }

    @Test
    public void updateEmail_withExistingEmail_doesNotCallAndThrowsException() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user1));

        assertThrows(ExistingEmailException.class,
                () -> userService.updateEmail("username1", "newEmail"));

        verify(userRepo).findByEmail(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updateEmail_withTooLongEmail_doesNotCallAndThrowsException() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(TooLongEmailException.class,
                () -> userService.updateEmail("username1", "a".repeat(101)));

        verify(userRepo).findByEmail(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updatePassword_withCorrectParameters_returnsUser() {
        when(userRepo.findByUsername("username1")).thenReturn(Optional.of(user1));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.updatePassword("username1", "newPassword");

        assertNotNull(user);
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepo).findByUsername(anyString());
        verify(passwordEncoder).encode(anyString());
        verify(userRepo).save(any());
    }

    @Test
    public void updatePassword_withTooLongPassword_doesNotCallAndThrowsException() {

        assertThrows(TooLongPasswordException.class,
                () -> userService.updatePassword("username1", "a".repeat(46)));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void updateBalances_withCorrectParameters_callsRepo() {
        user1.setBalance(10);
        user2.setBalance(20);
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        userService.updateBalances(user1, user2, 5);

        assertEquals(5, user1.getBalance());
        assertEquals(25, user2.getBalance());
        verify(userRepo, times(2)).save(any());
    }

    @Test
    public void getUserByUsername_withCorrectParameters_callsRepoAndReturnsUser() {
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

        assertThrows(UserNotFoundException.class,() -> userService.getUserByUsername("nonExistingUsername"));

        verify(userRepo).findByUsername(anyString());
    }

    @Test
    public void getUserByEmail_withCorrectParameters_callsRepoAndReturnsUser() {
        user1.setEmail("email1");
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user1));

        User user = userService.getUserByEmail("email1");

        assertNotNull(user);
        assertEquals(user1.getEmail(), user.getEmail());
        verify(userRepo).findByEmail(anyString());
    }

    @Test
    public void getUserByEmail_withNonExistingEmail_throwsException() {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,() -> userService.getUserByEmail("nonExistingUsername"));

        verify(userRepo).findByEmail(anyString());
    }

    @Test
    public void isAnExistingUsername_withExistingUsername_callsRepoAndReturnsTrue() {
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

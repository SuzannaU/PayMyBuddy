package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.UnsufficientFundsException;
import oc.paymybuddy.exceptions.UserNotFoundException;
import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ControllerServiceTest {

    @MockitoBean
    private RelationService relationService;
    @MockitoBean
    private TransactionService transactionService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserRoleService userRoleService;
    @MockitoBean
    private RoleService roleService;
    @Autowired
    private ControllerService controllerService;
    private User user1;
    private User user2;

    @BeforeEach
    private void init() {
        user1 = new User();
        user1.setUsername("user1");
        user2 = new User();
        user2.setUsername("user2");
    }

    @Test
    public void addRelation_withExistingEmail_callsServiceAndReturnsRelation() {
        user2.setEmail("email2");
        when(userService.isAnExistingEmail(any())).thenReturn(true);
        when(userService.getUserByUsername(any())).thenReturn(user1);
        when(userService.getUserByEmail(any())).thenReturn(user2);
        when(relationService.addRelation(any(), any())).thenReturn(new Relation());

        Relation relation = controllerService.addRelation(user1.getUsername(), user2.getEmail());

        assertNotNull(relation);
        verify(userService).isAnExistingEmail(any());
        verify(relationService).addRelation(user1, user2);
    }

    @Test
    public void addRelation_withNotExistingEmail_doesNotCallAndThrowsException() {
        user2.setEmail("email2");
        when(userService.isAnExistingEmail(any())).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> controllerService.addRelation(user1.getUsername(), user2.getEmail()));

        verify(userService).isAnExistingEmail(any());
        verify(relationService, never()).addRelation(user1, user2);
    }

    @Test
    public void getRelationsUsernamesByUsername_withCorrectParameters_callsServiceAndReturnsRelations() {
        Set<String> mockUsernames = Set.of("user1", "user2");
        when(userService.getUserByUsername(any())).thenReturn(new User());
        when(relationService.getRelationsUsernamesByUser(any())).thenReturn(mockUsernames);

        Set<String> usernames = controllerService.getRelationsUsernamesByUsername("user");

        assertNotNull(usernames);
        assertTrue(usernames.contains("user1") && usernames.contains("user2"));
        verify(userService).getUserByUsername(any());
        verify(relationService).getRelationsUsernamesByUser(any());
    }

    @Test
    public void getSentTransactionsByUsername_withCorrectParameters_callsServiceAndReturnsTransactions() {
        List<Transaction> mockTransactions = new ArrayList<>();
        when(userService.getUserByUsername(any())).thenReturn(user1);
        when(transactionService.getSentTransactionsByUser(any())).thenReturn(mockTransactions);

        List<Transaction> transactions = controllerService.getSentTransactionsByUsername("username");

        assertNotNull(transactions);
        verify(userService).getUserByUsername(any());
        verify(transactionService).getSentTransactionsByUser(any());
    }

    @Test
    public void transfer_withCorrectParameters_callsServiceAndReturnsTransaction() {
        user1.setBalance(10.00);

        when(userService.getUserByUsername(anyString())).thenReturn(user1);
        doNothing().when(userService).updateBalances(any(), any(), any(Double.class));
        when(transactionService.addTransaction(
                any(), any(), anyString(), any(Double.class))).thenReturn(new Transaction());

        Transaction transaction = controllerService.transfer(
                "senderUsername",
                "receiverUsername",
                "description",
                "10");

        assertNotNull(transaction);
        verify(userService, times(2)).getUserByUsername(anyString());
        verify(userService).updateBalances(any(), any(), any(Double.class));
        verify(transactionService).addTransaction(any(), any(), anyString(), any(Double.class));

    }

    @Test
    public void transfer_withUnsufficientFunds_doesNotCallAndThrowsException() {
        user1.setBalance(9.9999);

        when(userService.getUserByUsername(anyString())).thenReturn(user1);

        assertThrows(UnsufficientFundsException.class,
                () -> controllerService.transfer(
                        "senderUsername",
                        "receiverUsername",
                        "description",
                        "10"));

        verify(userService, times(2)).getUserByUsername(anyString());
        verify(userService, never()).updateBalances(any(), any(), any(Double.class));
        verify(transactionService, never()).addTransaction(any(), any(), anyString(), any(Double.class));
    }

    @Test
    public void getUserByUsername_withCorrectParameters_callsUserServiceAndReturnsUser() {
        when(userService.getUserByUsername(anyString())).thenReturn(user1);

        User user = controllerService.getUserByUsername("username");

        assertNotNull(user);
        verify(userService).getUserByUsername(anyString());
    }

    @Test
    public void registerUser_withCorrectParameters_callsServiceAndReturnsUser() {
        when(userService.registerUser(any())).thenReturn(user1);
        doNothing().when(userRoleService).assignRoleToUser(any(), any());

        User user = controllerService.registerUser(user1);

        assertNotNull(user);
        verify(userService).registerUser(any());
        verify(userRoleService).assignRoleToUser(any(), any());
    }

    @Test
    public void updateUsername_withCorrectParameters_callsServiceAndReturnsUser() {
        user1.setUsername("username1");
        when(userService.updateUsername(any(), any())).thenReturn(user1);

        User user = controllerService.updateUsername(user1.getUsername(),  "newUsername");

        assertNotNull(user);
        verify(userService).updateUsername(any(), any());
    }

    @Test
    public void updateEmail_withCorrectParameters_callsServiceAndReturnsUser() {
        user1.setEmail("email1");
        when(userService.updateEmail(any(), any())).thenReturn(user1);

        User user = controllerService.updateEmail(user1.getEmail(),"newEmail");

        assertNotNull(user);
        verify(userService).updateEmail(any(), any());
    }

    @Test
    public void updatePassword_withCorrectParameters_callsServiceAndReturnsUser() {
        user1.setPassword("password1");
        when(userService.updatePassword(any(), any())).thenReturn(user1);

        User user = controllerService.updatePassword(user1.getPassword(),"newPassword");

        assertNotNull(user);
        verify(userService).updatePassword(any(), any());
    }

}

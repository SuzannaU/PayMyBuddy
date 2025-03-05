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
public class SuperServiceTest {

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
    private SuperService superService;
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
    public void addRelation_withExistingUsername_returnsRelation() {
        when(userService.isAnExistingUsername(any())).thenReturn(true);
        when(relationService.addRelation(any(), any())).thenReturn(new Relation());

        Relation relation = superService.addRelation(user1, user2);

        assertNotNull(relation);
        verify(userService).isAnExistingUsername(any());
        verify(relationService).addRelation(user1, user2);
    }

    @Test
    public void addRelation_withNotExistingUsername_throwsException() {
        when(userService.isAnExistingUsername(any())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> superService.addRelation(user1, user2));

        verify(userService).isAnExistingUsername(any());
        verify(relationService, never()).addRelation(user1, user2);
    }

    @Test
    public void getRelationsUsernamesByUsername_withCorrectParameters_returnsRelations() {
        Set<String> mockUsernames = Set.of("user1", "user2");
        when(userService.getUserByUsername(any())).thenReturn(new User());
        when(relationService.getRelationsUsernamesByUser(any())).thenReturn(mockUsernames);

        Set<String> usernames = superService.getRelationsUsernamesByUsername("user");

        assertNotNull(usernames);
        assertTrue(usernames.contains("user1") && usernames.contains("user2"));
        verify(userService).getUserByUsername(any());
        verify(relationService).getRelationsUsernamesByUser(any());
    }

    @Test
    public void transfer_withCorrectParameters_returnsTransaction() {
        user1.setBalance(10.00);

        when(userService.getUserByUsername(anyString())).thenReturn(user1);
        doNothing().when(userService).updateBalances(any(), any(), any(Double.class));
        when(transactionService.addTransaction(
                any(), any(), anyString(), any(Double.class))).thenReturn(new Transaction());

        Transaction transaction = superService.transfer(
                "senderUsername",
                "receiverUsername",
                "description",
                "10");

        assertNotNull(transaction);
        verify(userService).updateBalances(any(), any(), any(Double.class));
        verify(transactionService).addTransaction(any(), any(), anyString(), any(Double.class));

    }

    @Test
    public void transfer_withUnsufficientFunds_throwsException() {
        user1.setBalance(9.9999);

        assertThrows(UnsufficientFundsException.class,
                () -> superService.transfer(
                        "senderUsername",
                        "receiverUsername",
                        "description",
                        "10"));

        verify(userService, never()).updateBalances(any(), any(), any(Double.class));
        verify(transactionService, never()).addTransaction(any(), any(), anyString(), any(Double.class));
    }

    @Test
    public void getTransactionsByUsername_withCorrectParameters_returnsTransactions() {
        List<Transaction> mockTransactions = new ArrayList<>();
        when(userService.getUserByUsername(any())).thenReturn(user1);
        when(transactionService.getSentTransactionsByUser(any())).thenReturn(mockTransactions);

        List<Transaction> transactions = superService.getSentTransactionsByUsername("username");

        assertNotNull(transactions);
        verify(userService).getUserByUsername(any());
        verify(transactionService).getSentTransactionsByUser(any());
    }

    @Test
    public void registerUser_withCorrectParameters_returnsUser() {
        when(userService.registerUser(any())).thenReturn(user1);
        doNothing().when(userRoleService).assignRoleToUser(any(), any());

        User user = superService.registerUser(user1);

        assertNotNull(user);
        verify(userService).registerUser(any());
        verify(userRoleService).assignRoleToUser(any(), any());
    }

}

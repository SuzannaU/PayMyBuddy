package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.TooLongException;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.TransactionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {

    @MockitoBean
    private TransactionRepo transactionRepo;
    @Autowired
    private TransactionService transactionService;
    private User user1;
    private User user2;

    @BeforeEach
    private void init() {
        user1 = new User();
        user2 = new User();
    }

    @Test
    public void addTransaction_withCorrectParameters_callsRepoAndReturnsTransaction() {
        when(transactionRepo.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = transactionService.addTransaction(
                user1, user2, "description", 150.00, 0);

        assertNotNull(transaction);
        assertEquals(user1, transaction.getSender());
        assertEquals(user2, transaction.getReceiver());
        assertEquals("description", transaction.getDescription());
        assertEquals(150.00, transaction.getAmount());
        assertEquals(0.00, transaction.getFee());
        verify(transactionRepo).save(any());
    }

    @Test
    public void addTransaction_withTooLongDescription_doesNotCallAndThrowsException() {

        assertThrows(TooLongException.class, () -> transactionService.addTransaction(
                user1, user2, "d".repeat(251), 150.00, 0));

        verify(transactionRepo, never()).save(any());
    }

    @Test
    public void getTransactionsByUser_withCorrectParameters_returnsTransactions() {
        when(transactionRepo.findAllBySender(any(), any(Sort.class))).thenReturn(new ArrayList<>());
        List<Transaction> transactions = transactionService.getSentTransactionsByUser(user1);

        assertNotNull(transactions);
        verify(transactionRepo).findAllBySender(any(), any(Sort.class));
    }
}

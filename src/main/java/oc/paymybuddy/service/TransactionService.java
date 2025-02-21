package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.TooLongException;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.TransactionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public Transaction addTransaction(User sender, User receiver, String description, double amount) {
        if(description.length()>250){
            throw new TooLongException();
        }
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        return transactionRepo.save(transaction);
    }

    public List<Transaction> getTransactionsByUser(User user) {
        return transactionRepo.findAllBySenderOrReceiver(user, user);
    }


    // dev utilities
    public Transaction getTransactionById(int id) {
        return transactionRepo.findById(id).get();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }
}

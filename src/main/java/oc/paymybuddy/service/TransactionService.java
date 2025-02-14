package oc.paymybuddy.service;

import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.TransactionRepo;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepo transactionRepo;
    private UserService userService;

    public TransactionService(TransactionRepo transactionRepo, UserService userService) {
        this.transactionRepo = transactionRepo;
        this.userService = userService;
    }

    public Transaction transfer(Principal principal, User receiver, String description, double amount){
        User sender = (User) principal;
        userService.updateBalance(sender, sender.getBalance() - amount);
        userService.updateBalance(receiver, receiver.getBalance() + amount);
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        return transactionRepo.save(transaction);
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepo.save(transaction);
    }

    public List<Transaction> getTransactionsByUsername(String username) {
        return null;
    }


    // dev utilities
    public Transaction getTransactionById(int id){
        return transactionRepo.findById(id).get();
    }
    public List<Transaction> getAllTransactions(){
        return transactionRepo.findAll();
    }
}

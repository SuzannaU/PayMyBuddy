package oc.paymybuddy.service;

import oc.paymybuddy.exceptions.TooLongException;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import oc.paymybuddy.repository.TransactionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public Transaction addTransaction(
            User sender,
            User receiver,
            String description,
            double amount,
            double fee) {

        if (description.length() > 250) {
            logger.error("description length exceeds 250 characters");
            throw new TooLongException();
        }
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setFee(fee);
        return transactionRepo.save(transaction);
    }

    public List<Transaction> getSentTransactionsByUser(User user) {
        return transactionRepo.findAllBySender(
                user, Sort.by(Sort.Direction.DESC, "id"));
    }

    //TODO fee calculation method (V1)
    public double calculateFee(double netAmount) {
        return 0;
    }
}

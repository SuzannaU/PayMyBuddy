package oc.paymybuddy.service;

import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    public Transaction getTransactionById(int id){
        return transactionRepo.findById(id).get();
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepo.findAll();
    }
}

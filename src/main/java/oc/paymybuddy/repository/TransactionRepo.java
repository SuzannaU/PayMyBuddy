package oc.paymybuddy.repository;

import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllBySender(User sender, Sort sort);
}

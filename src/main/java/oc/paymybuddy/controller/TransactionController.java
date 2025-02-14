package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.service.SuperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    private final SuperService superService;

    public TransactionController(SuperService superService) {
        this.superService = superService;
    }

    @GetMapping("/transactions")
    public List<Transaction> getPrincipalTransactions(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return superService.getTransactionsByUsername(username);
    }


}

package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import oc.paymybuddy.service.SuperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final SuperService superService;

    public TransactionController(SuperService superService) {
        this.superService = superService;
    }

    @GetMapping("/transactions")
    public List<Transaction> getPrincipalTransactions(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return superService.getSentTransactionsByUsername(username);
    }

    @GetMapping("/transfer")
    public String getPrincipalTransactions2(HttpServletRequest request, Model model) {
        String username = request.getUserPrincipal().getName();
        Transaction transaction = new Transaction();
        var transactions = superService.getSentTransactionsByUsername(username);
        var relationsUsernames = superService.getRelationsUsernamesByUsername(username);
        model.addAttribute("transaction", transaction);
        model.addAttribute("transactions", transactions);
        model.addAttribute("relationsUsernames", relationsUsernames);
        model.addAttribute("currentUrl", request.getRequestURI());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String receiver, @RequestParam String description,@RequestParam String amount,HttpServletRequest request) {
        logger.debug("POST transfer");
        String senderUsername = request.getUserPrincipal().getName();
        superService.transfer(senderUsername, receiver, description, amount);
        return "redirect:/transfer";
    }

}

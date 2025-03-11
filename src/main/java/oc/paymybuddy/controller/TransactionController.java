package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.exceptions.TooLongException;
import oc.paymybuddy.exceptions.UnsufficientFundsException;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.service.ControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final ControllerService controllerService;

    public TransactionController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }


    @GetMapping("/transfer")
    public String getTransfer(HttpServletRequest request, Model model) {
        String senderUsername = request.getUserPrincipal().getName();
        setUpView(request, model, senderUsername);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String receiver,
            @RequestParam String description,
            @RequestParam String amount,
            Model model,
            HttpServletRequest request) {
        logger.debug("POST transfer");
        String senderUsername = request.getUserPrincipal().getName();
        try {
            controllerService.transfer(senderUsername, receiver, description, amount);
        }catch (UnsufficientFundsException e){
            logger.error("Not enough funds for this transfer");
            setUpView(request, model, senderUsername);
            model.addAttribute("fundsError", "Le solde est insuffisant.");
            return "transfer";
        } catch (TooLongException e2){
            logger.error("Description is too long");
            setUpView(request, model, senderUsername);
            model.addAttribute("descriptionError", "La description doit faire moins de 250 caractères.");
            return "transfer";
        }
        return "redirect:/transfer";
    }

    private void setUpView(HttpServletRequest request, Model model, String senderUsername) {
        Transaction transaction = new Transaction();
        var transactions = controllerService.getSentTransactionsByUsername(senderUsername);
        var relationsUsernames = controllerService.getRelationsUsernamesByUsername(senderUsername);
        model.addAttribute("transaction", transaction);
        model.addAttribute("transactions", transactions);
        model.addAttribute("relationsUsernames", relationsUsernames);
        model.addAttribute("currentUrl", request.getRequestURI());
    }

}

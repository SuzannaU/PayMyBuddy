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

/**
 * Handles requests related to Transactions
 */
@Controller
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final ControllerService controllerService;

    public TransactionController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    /**
     * Returns transfer page, with model to specify the current URL
     * @param request used to retrieve the current URL as well as the Principal
     * @param model used to inject the current URL (for active link in navigation)
     * @return transfer template
     */
    @GetMapping("/transfer")
    public String getTransfer(HttpServletRequest request, Model model) {
        String senderUsername = request.getUserPrincipal().getName();
        setUpView(request, model, senderUsername);
        return "transfer";
    }

    /**
     * Calls ControllerService to initiate the creation of a new Transaction
     * Catches Exceptions and reloads page with error messages
     *
     * @param receiver retrieved as RequestParam
     * @param description retrieved as RequestParam
     * @param amount Retrieved as RequestParam
     * @param request used to retrieve the current URL as well as the Principal
     * @param model used to inject the current URL in case of reload
     * @return redirection to /transfer
     */
    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String receiver,
            @RequestParam String description,
            @RequestParam String amount,
            Model model,
            HttpServletRequest request) {
        logger.info("POST transfer");
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

    /**
     * Sets up the transfer view for fresh loads as well as reloads
     * Calls ControllerService to retrieve transactions and relations to be displayed on the page
     *
     * @param request used to retrieve the current URL
     * @param model used to inject the current URL (for active link in navigation)
     * @param senderUsername username to which transactions and relations are related
     */
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

package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.exceptions.ExistingRelationException;
import oc.paymybuddy.exceptions.UserNotFoundException;
import oc.paymybuddy.service.ControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This controller handles requests related to Relations
 */
@Controller
public class RelationController {
    private static final Logger logger = LoggerFactory.getLogger(RelationController.class);

    private final ControllerService controllerService;

    public RelationController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    /**
     * Returns add-relation page, with model to specify the current URL
     * @param request used to retrieve the current URL
     * @param model used to inject the current URL (for active link in navigation)
     * @return add-relation template
     */
    @GetMapping("/add-relation")
    public String getAddRelation(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "add-relation";
    }

    /**
     * Calls ControllerService to initiate the creation of a new Relation
     * Catches Exceptions and reloads page with error messages
     *
     * @param invitedUserEmail retrieved as RequestParam
     * @param request used to retrieve the current URL as well as the Principal
     * @param model used to inject the current URL in case of reload
     * @return redirection to /add-relation
     */
    @PostMapping("/add-relation")
    public String addRelation(@RequestParam String invitedUserEmail,Model model, HttpServletRequest request) {
        logger.info("POST addRelation");
        String invitingUsername = request.getUserPrincipal().getName();
        try {
            controllerService.addRelation(invitingUsername, invitedUserEmail);
        }  catch (UserNotFoundException e) {
            logger.error("User with this email not found");
            model.addAttribute("currentUrl", request.getRequestURI());
            model.addAttribute("relationError", "L'email n'existe pas");
            return "add-relation";
        }  catch (ExistingRelationException e2) {
            logger.error("This relation already exists");
            model.addAttribute("currentUrl", request.getRequestURI());
            model.addAttribute("relationError", "Cet utilisateur est déjà une relation.");
            return "add-relation";
        }
        return "redirect:/add-relation";
    }
}

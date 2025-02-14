package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.Role;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.UserRole;
import oc.paymybuddy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    UserService userService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/")
    public String getHome(HttpServletRequest request) {

        StringBuffer userInfo = new StringBuffer();
        //if (principal instanceof UsernamePasswordAuthenticationToken) {
            userInfo.append(getUsernamePasswordLoginInfo(request.getUserPrincipal()))
                    .append(request.getUserPrincipal().getName());
        //}
        return userInfo.toString();
    }

    @GetMapping("/csrf")
    public String getCsrfToken(HttpServletRequest request) {
        return request.getAttribute("csrfToken").toString();
    }

    @GetMapping("/test")
    public String getTest() {
        return "Test";
    }
    @GetMapping("/admin")
    public String getAdmin() {
        return "Admin";
    }
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.getAllRoles();
    }
    @GetMapping("/users-roles")
    public List<UserRole> getAllUserRoles(){
        return userRoleService.getAllUserRoles();
    }

    private StringBuffer getUsernamePasswordLoginInfo(Principal principal) {
        StringBuffer usernameInfo = new StringBuffer();

        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) principal);
        if (token.isAuthenticated()) {
            org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) token.getPrincipal();
            usernameInfo.append("Welcome, " + u.getUsername());
        } else {
            usernameInfo.append("NA");
        }
        return usernameInfo;
    }
}

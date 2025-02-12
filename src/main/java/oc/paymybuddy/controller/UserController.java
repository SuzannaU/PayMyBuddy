package oc.paymybuddy.controller;

import oc.paymybuddy.model.*;
import oc.paymybuddy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/relations")
    public List<Relation> getAllRelations(){
        return relationService.getAllRelations();
    }
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.getAllRoles();
    }
    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions(){
        return transactionService.getAllTransactions();
    }
    @GetMapping("/users-roles")
    public List<UserRole> getAllUserRoles(){
        return userRoleService.getAllUserRoles();
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }
}

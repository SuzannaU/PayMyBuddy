package oc.paymybuddy.controller;

import jakarta.servlet.http.HttpServletRequest;
import oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class Controller {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String getHome(Principal user) {

        StringBuffer userInfo = new StringBuffer();
        if (user instanceof UsernamePasswordAuthenticationToken) {
            userInfo.append(getUsernamePasswordLoginInfo(user));
        }
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

    @GetMapping("/error")
    public String getError() {
        return "Error";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "Admin";
    }

    @GetMapping("/user")
    public String getUser() {
        return "User";
    }

    @GetMapping("/no-role")
    public String getNoRole() {
        return "No role";
    }
    @PostMapping("/user")
    public String getPost() {
        return "Post request";
    }

    private StringBuffer getUsernamePasswordLoginInfo(Principal user) {
        StringBuffer usernameInfo = new StringBuffer();

        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
        if (token.isAuthenticated()) {
            org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) token.getPrincipal();
            usernameInfo.append("Welcome, " + u.getUsername());
        } else {
            usernameInfo.append("NA");
        }
        return usernameInfo;
    }
}

package oc.paymybuddy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ControllerTest {
    @Autowired
    private UserController userController;

    @Test
    public void getLogin_returnsLoginView() {
        String viewName = userController.getLogin();
        assertEquals("login", viewName);
    }

    @Test
    public void getRegister_returnsRegisterView() {
        Model model = new ConcurrentModel();
        String viewName = userController.getRegister(model);
        assertEquals("register", viewName);
    }
}

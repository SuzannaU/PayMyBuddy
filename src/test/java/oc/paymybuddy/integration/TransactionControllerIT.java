package oc.paymybuddy.integration;

import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import oc.paymybuddy.service.ControllerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/paymybuddy_test?serverTimezone=UTC",
        "spring.datasource.driverClassname=com.mysql.cj.jdbc.Driver",
        "spring.datasource.username=root",
        "spring.datasource.password=root"
})
public class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ControllerService controllerService;

    @Test
    @WithUserDetails("user1@example.com")
    public void transfer_withCorrectParameters_updatesBalanceAndSavesTransaction() throws Exception {

        User user1 = controllerService.getUserByUsername("user1");
        double user1BalanceBefore =user1.getBalance();
        User user2 = controllerService.getUserByUsername("user2");
        double user2BalanceBefore =user2.getBalance();
        List<Transaction> transactionsBeforeTest = controllerService.getSentTransactionsByUsername(user1.getUsername());
        assertTrue(transactionsBeforeTest.isEmpty());
        String stringAmount = "10.0";
        double amount = Double.parseDouble(stringAmount);

        this.mockMvc.perform(post("/transfer")
                        .param("receiver", "user2")
                        .param("description", "test description")
                        .param("amount", stringAmount)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"));


        List<Transaction> result = controllerService.getSentTransactionsByUsername(user1.getUsername());
        assertThat(result.size()).isEqualTo(1);

        User user1After = controllerService.getUserByUsername("user1");
        User user2After = controllerService.getUserByUsername("user2");
        assertThat(user1After.getBalance()).isEqualTo(user1BalanceBefore-amount);
        assertThat(user2After.getBalance()).isEqualTo(user2BalanceBefore+amount);
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void transfer_withUnsufficientFunds_returnsErrorMessage() throws Exception {

        User user1 = controllerService.getUserByUsername("user1");
        User user2 = controllerService.getUserByUsername("user2");
        double user1BalanceBefore =user1.getBalance();
        double user2BalanceBefore =user2.getBalance();
        List<Transaction> transactionsBeforeTest = controllerService.getSentTransactionsByUsername(user1.getUsername());
        assertTrue(transactionsBeforeTest.isEmpty());
        String stringAmount = "1000.0";

        this.mockMvc.perform(post("/transfer")
                        .param("receiver", "user2")
                        .param("description", "test description")
                        .param("amount", stringAmount)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attribute("fundsError", "Le solde est insuffisant."));


        List<Transaction> result = controllerService.getSentTransactionsByUsername(user1.getUsername());
        assertTrue(result.isEmpty());

        User user1After = controllerService.getUserByUsername("user1");
        User user2After = controllerService.getUserByUsername("user2");
        assertThat(user1After.getBalance()).isEqualTo(user1BalanceBefore);
        assertThat(user2After.getBalance()).isEqualTo(user2BalanceBefore);
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void transfer_withTooLongDescription_returnsErrorMessage() throws Exception {

        User user1 = controllerService.getUserByUsername("user1");
        User user2 = controllerService.getUserByUsername("user2");
        double user1BalanceBefore =user1.getBalance();
        double user2BalanceBefore =user2.getBalance();
        List<Transaction> transactionsBeforeTest = controllerService.getSentTransactionsByUsername(user1.getUsername());
        assertTrue(transactionsBeforeTest.isEmpty());
        String stringAmount = "10.0";
        String tooLongDescription = "d".repeat(251);

        this.mockMvc.perform(post("/transfer")
                        .param("receiver", "user2")
                        .param("description", tooLongDescription)
                        .param("amount", stringAmount)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attribute("descriptionError", "La description doit faire moins de 250 caractères."));


        List<Transaction> result = controllerService.getSentTransactionsByUsername(user1.getUsername());
        assertTrue(result.isEmpty());

        User user1After = controllerService.getUserByUsername("user1");
        User user2After = controllerService.getUserByUsername("user2");
        assertThat(user1After.getBalance()).isEqualTo(user1BalanceBefore);
        assertThat(user2After.getBalance()).isEqualTo(user2BalanceBefore);
    }
}

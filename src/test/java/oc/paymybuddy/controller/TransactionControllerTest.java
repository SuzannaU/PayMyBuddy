package oc.paymybuddy.controller;

import oc.paymybuddy.exceptions.TooLongException;
import oc.paymybuddy.exceptions.UnsufficientFundsException;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.service.ControllerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ControllerService controllerService;

    List<Transaction> transactions;
    Set<String> relationsUsernames;


    @BeforeEach
    public void setup() {
        transactions = new ArrayList<>();
        relationsUsernames = new HashSet<>();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getTransfer_withAuthenticatedUser_returnsTransferView() throws Exception {

        when(controllerService.getSentTransactionsByUsername(anyString())).thenReturn(transactions);
        when(controllerService.getRelationsUsernamesByUsername(anyString())).thenReturn(relationsUsernames);

        this.mockMvc.perform(get("/transfer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(content().string(containsString("Transférer")));

        verify(controllerService).getSentTransactionsByUsername(anyString());
        verify(controllerService).getRelationsUsernamesByUsername(anyString());
    }

    @Test
    @WithAnonymousUser
    public void getTransfer_withAnonymousUser_returnsUnauthorized() throws Exception {

        this.mockMvc.perform(get("/transfer"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void postTransfer_withCorrectParameters_callsServiceAndRedirects() throws Exception {

        when(controllerService.transfer(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new Transaction());

        this.mockMvc.perform(post("/transfer")
                        .param("receiver", "email1")
                        .param("description", "description")
                        .param("amount", "10")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"));

        verify(controllerService).transfer(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void postTransfer_withUnsufficientFundsException_returnsErrorMessage() throws Exception {

        when(controllerService.getSentTransactionsByUsername(anyString())).thenReturn(transactions);
        when(controllerService.getRelationsUsernamesByUsername(anyString())).thenReturn(relationsUsernames);

        doThrow(new UnsufficientFundsException()).when(controllerService)
                .transfer(anyString(), anyString(), anyString(), anyString());

        this.mockMvc.perform(post("/transfer")
                        .param("receiver", "email1")
                        .param("description", "description")
                        .param("amount", "10")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attribute("fundsError", "Le solde est insuffisant."));

        verify(controllerService).getSentTransactionsByUsername(anyString());
        verify(controllerService).getRelationsUsernamesByUsername(anyString());
        verify(controllerService).transfer(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void postTransfer_withTooLongException_returnsErrorMessage() throws Exception {

        when(controllerService.getSentTransactionsByUsername(anyString())).thenReturn(transactions);
        when(controllerService.getRelationsUsernamesByUsername(anyString())).thenReturn(relationsUsernames);

        doThrow(new TooLongException()).when(controllerService)
                .transfer(anyString(), anyString(), anyString(), anyString());

        this.mockMvc.perform(post("/transfer")
                        .param("receiver", "email1")
                        .param("description", "description")
                        .param("amount", "10")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attribute("descriptionError", "La description doit faire moins de 250 caractères."));

        verify(controllerService).getSentTransactionsByUsername(anyString());
        verify(controllerService).getRelationsUsernamesByUsername(anyString());
        verify(controllerService).transfer(anyString(), anyString(), anyString(), anyString());
    }
}

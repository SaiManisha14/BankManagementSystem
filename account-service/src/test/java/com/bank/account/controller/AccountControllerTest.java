package com.bank.account.controller;

import com.bank.account.dto.AccountDTO;
import com.bank.account.entity.Account;
import com.bank.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    void testCreateAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(1L);
        accountDTO.setAccountType("SAVINGS");
        accountDTO.setBalance(new BigDecimal("10000.00"));
        
        Account account = Account.builder()
                .id(1L)
                .userId(1L)
                .accountType("SAVINGS")
                .balance(new BigDecimal("10000.00"))
                .build();

        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(account);

        mockMvc.perform(post("/api/accounts")
                        .header("X-User-Roles", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.balance").value(10000.00));
    }

    @Test
    void testGetAllAccounts() throws Exception {
        Account account = Account.builder()
                .id(1L)
                .userId(1L)
                .accountType("SAVINGS")
                .balance(new BigDecimal("10000.00"))
                .build();

        when(accountService.getAllAccounts()).thenReturn(List.of(account));

        mockMvc.perform(get("/api/accounts")
                        .header("X-User-Roles", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].accountType").value("SAVINGS"));
    }

    @Test
    void testGetAccountById() throws Exception {
        Account account = Account.builder()
                .id(1L)
                .userId(1L)
                .accountType("SAVINGS")
                .balance(new BigDecimal("10000.00"))
                .build();

        when(accountService.getAccountById(1L)).thenReturn(account);

        mockMvc.perform(get("/api/accounts/1")
                        .header("X-User-Roles", "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.balance").value(10000.00));
    }

    @Test
    void testUpdateBalance() throws Exception {
        Account account = Account.builder()
                .id(1L)
                .userId(1L)
                .accountType("SAVINGS")
                .balance(new BigDecimal("11000.00"))
                .build();
        when(accountService.updateBalance(eq(1L), eq(new BigDecimal("1000")), eq("ADD"))).thenReturn(account);

        String requestBody = "{\"operation\":\"ADD\",\"amount\":1000}";

        mockMvc.perform(put("/api/accounts/1/balance")
                        .header("X-User-Roles", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(11000.00));
    }

    @Test
    void testGetAccountsByUserId() throws Exception {
        Account account = Account.builder()
                .id(1L)
                .userId(1L)
                .accountType("SAVINGS")
                .balance(new BigDecimal("10000.00"))
                .build();

        when(accountService.getAccountsByUserId(1L)).thenReturn(List.of(account));

        mockMvc.perform(get("/api/accounts/user/1")
                        .header("X-User-Roles", "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1));
    }

    @Test
    void testDeleteAccount() throws Exception {
        mockMvc.perform(delete("/api/accounts/1")
                        .header("X-User-Roles", "USER"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllAccounts_AdminOnly() throws Exception {
        Account account = Account.builder()
                .id(1L)
                .userId(1L)
                .accountType("SAVINGS")
                .balance(new BigDecimal("10000.00"))
                .build();

        when(accountService.getAllAccounts()).thenReturn(List.of(account));

        mockMvc.perform(get("/api/accounts")
                        .header("X-User-Roles", "ADMIN"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllAccounts_UserForbidden() throws Exception {
        mockMvc.perform(get("/api/accounts")
                        .header("X-User-Roles", "USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateAccount_Unauthorized() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(1L);
        accountDTO.setAccountType("SAVINGS");
        accountDTO.setBalance(new BigDecimal("10000.00"));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isForbidden());
    }
}
package com.bank.admin.controller;

import com.bank.admin.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    void testGetAllAccounts_WithAdminRole_Success() throws Exception {
        List<Object> mockAccounts = Arrays.asList(new Object(), new Object());
        when(adminService.getAllAccounts()).thenReturn(mockAccounts);

        mockMvc.perform(get("/api/admin/accounts")
                        .header("X-User-Roles", "ADMIN"))
                .andExpect(status().isOk());

        verify(adminService, times(1)).getAllAccounts();
    }

    @Test
    void testGetAllAccounts_WithoutAdminRole_Forbidden() throws Exception {
        mockMvc.perform(get("/api/admin/accounts")
                        .header("X-User-Roles", "USER"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Only ADMIN can view all accounts"));

        verify(adminService, never()).getAllAccounts();
    }

    @Test
    void testGetAllAccounts_WithoutRoleHeader_Forbidden() throws Exception {
        mockMvc.perform(get("/api/admin/accounts"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Only ADMIN can view all accounts"));

        verify(adminService, never()).getAllAccounts();
    }

    @Test
    void testGetAllUsers_WithAdminRole_Success() throws Exception {
        List<Object> mockUsers = Arrays.asList(new Object(), new Object());
        when(adminService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/api/admin/users")
                        .header("X-User-Roles", "ADMIN"))
                .andExpect(status().isOk());

        verify(adminService, times(1)).getAllUsers();
    }

    @Test
    void testGetAllUsers_WithoutAdminRole_Forbidden() throws Exception {
        mockMvc.perform(get("/api/admin/users")
                        .header("X-User-Roles", "USER"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Only ADMIN can view all users"));

        verify(adminService, never()).getAllUsers();
    }

    @Test
    void testGetAllUsers_WithoutRoleHeader_Forbidden() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Only ADMIN can view all users"));

        verify(adminService, never()).getAllUsers();
    }

    @Test
    void testGetAllTransactions_WithAdminRole_Success() throws Exception {
        List<Object> mockTransactions = Arrays.asList(new Object(), new Object());
        when(adminService.getAllTransactions()).thenReturn(mockTransactions);

        mockMvc.perform(get("/api/admin/transactions")
                        .header("X-User-Roles", "ADMIN"))
                .andExpect(status().isOk());

        verify(adminService, times(1)).getAllTransactions();
    }

    @Test
    void testGetAllTransactions_WithoutAdminRole_Forbidden() throws Exception {
        mockMvc.perform(get("/api/admin/transactions")
                        .header("X-User-Roles", "USER"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Only ADMIN can view all transactions"));

        verify(adminService, never()).getAllTransactions();
    }

    @Test
    void testGetAllTransactions_WithoutRoleHeader_Forbidden() throws Exception {
        mockMvc.perform(get("/api/admin/transactions"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Only ADMIN can view all transactions"));

        verify(adminService, never()).getAllTransactions();
    }
}

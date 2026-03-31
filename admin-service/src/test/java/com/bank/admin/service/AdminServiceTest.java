package com.bank.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AdminService adminService;

    private static final String ACCOUNT_SERVICE_URL = "http://account-service/api/accounts";
    private static final String USER_SERVICE_URL = "http://user-service/api/users";
    private static final String TRANSACTION_SERVICE_URL = "http://transaction-service/api/transactions";

    @Test
    void testGetAllAccounts() {
        Object mockResponse = new Object();
        when(restTemplate.getForObject(eq(ACCOUNT_SERVICE_URL), eq(Object.class)))
                .thenReturn(mockResponse);

        Object result = adminService.getAllAccounts();

        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(restTemplate, times(1)).getForObject(eq(ACCOUNT_SERVICE_URL), eq(Object.class));
    }

    @Test
    void testGetAllUsers() {
        Object mockResponse = new Object();
        when(restTemplate.getForObject(eq(USER_SERVICE_URL), eq(Object.class)))
                .thenReturn(mockResponse);

        Object result = adminService.getAllUsers();

        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(restTemplate, times(1)).getForObject(eq(USER_SERVICE_URL), eq(Object.class));
    }

    @Test
    void testGetAllTransactions() {
        Object mockResponse = new Object();
        when(restTemplate.getForObject(eq(TRANSACTION_SERVICE_URL), eq(Object.class)))
                .thenReturn(mockResponse);

        Object result = adminService.getAllTransactions();

        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(restTemplate, times(1)).getForObject(eq(TRANSACTION_SERVICE_URL), eq(Object.class));
    }
}

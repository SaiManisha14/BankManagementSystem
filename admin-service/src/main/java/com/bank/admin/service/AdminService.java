package com.bank.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final RestTemplate restTemplate;

    private static final String ACCOUNT_SERVICE_URL = "http://account-service/api/accounts";
    private static final String USER_SERVICE_URL = "http://user-service/api/users";
    private static final String TRANSACTION_SERVICE_URL = "http://transaction-service/api/transactions";

    public Object getAllAccounts() {
        log.info("Fetching all accounts from account-service");
        return restTemplate.getForObject(ACCOUNT_SERVICE_URL, Object.class);
    }

    public Object getAllUsers() {
        log.info("Fetching all users from user-service");
        return restTemplate.getForObject(USER_SERVICE_URL, Object.class);
    }

    public Object getAllTransactions() {
        log.info("Fetching all transactions from transaction-service");
        return restTemplate.getForObject(TRANSACTION_SERVICE_URL, Object.class);
    }
}

package com.bank.transaction.client;

import com.bank.transaction.dto.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "account-service")
public interface AccountClient {
    
    @GetMapping("/api/accounts/{id}")
    Account getAccountById(@PathVariable Long id);
    
    @PutMapping("/api/accounts/internal/{id}/balance")
    Account updateBalance(@PathVariable Long id, @RequestBody Map<String, Object> request);
}

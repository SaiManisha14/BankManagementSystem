package com.bank.account.controller;

import com.bank.account.dto.AccountDTO;
import com.bank.account.entity.Account;
import com.bank.account.exception.UnauthorizedException;
import com.bank.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(@RequestHeader(value = "X-User-Roles", required = false) String roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            throw new UnauthorizedException("Only ADMIN can view all accounts");
        }
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "account-service"));
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<Account> testGetAccount(@PathVariable Long id) {
        try {
            Account account = accountService.getAccountById(id);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            log.error("Test get account failed: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        if (roles == null || (!roles.contains("ADMIN") && !roles.contains("USER"))) {
            throw new UnauthorizedException("Authentication required");
        }
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(
            @PathVariable Long userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        if (roles == null || (!roles.contains("ADMIN") && !roles.contains("USER"))) {
            throw new UnauthorizedException("Authentication required");
        }
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @Valid @RequestBody AccountDTO accountDTO,
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        if (roles == null || (!roles.contains("ADMIN") && !roles.contains("USER"))) {
            throw new UnauthorizedException("Authentication required");
        }
        return ResponseEntity.ok(accountService.createAccount(accountDTO));
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<Account> updateBalance(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request,
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        if (roles == null || (!roles.contains("ADMIN") && !roles.contains("USER"))) {
            throw new UnauthorizedException("Authentication required");
        }
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String operation = request.get("operation").toString();
        return ResponseEntity.ok(accountService.updateBalance(id, amount, operation));
    }

    // Internal endpoint for service-to-service calls
    @PutMapping("/internal/{id}/balance")
    public ResponseEntity<Account> updateBalanceInternal(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        log.info("Internal balance update request for account: {} with request: {}", id, request);
        try {
            // No auth check for internal service calls
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String operation = request.get("operation").toString();
            Account updatedAccount = accountService.updateBalance(id, amount, operation);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            log.error("Internal balance update failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    
   
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        if (roles == null || (!roles.contains("ADMIN") && !roles.contains("USER"))) {
            throw new UnauthorizedException("Authentication required");
        }
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
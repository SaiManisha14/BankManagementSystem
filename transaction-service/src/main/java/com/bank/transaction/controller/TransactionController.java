package com.bank.transaction.controller;

import com.bank.transaction.dto.TransactionDTO;
import com.bank.transaction.dto.TransferDTO;
import com.bank.transaction.entity.Transaction;
import com.bank.transaction.exception.UnauthorizedException;
import com.bank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "transaction-service"));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(@RequestHeader(value = "X-User-Roles", required = false) String roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            throw new UnauthorizedException("Only ADMIN can view all transactions");
        }
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(
            @PathVariable Long accountId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        log.info("Get transactions request with roles: {}", roles);
        if (roles == null || (!roles.contains("ADMIN") && !roles.contains("USER"))) {
            throw new UnauthorizedException("Authentication required");
        }
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(
            @Valid @RequestBody TransactionDTO transactionDTO,
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        log.info("Deposit request with roles: {}", roles);
        if (roles == null || (!roles.contains("ADMIN") && !roles.contains("USER"))) {
            throw new UnauthorizedException("Authentication required");
        }
        return ResponseEntity.ok(transactionService.deposit(transactionDTO.getAccountId(), transactionDTO.getAmount()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @Valid @RequestBody TransactionDTO transactionDTO,
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        log.info("Withdraw request with roles: {}", roles);
        if (roles == null || (!roles.contains("ADMIN") && !roles.contains("USER"))) {
            throw new UnauthorizedException("Authentication required");
        }
        return ResponseEntity.ok(transactionService.withdraw(transactionDTO.getAccountId(), transactionDTO.getAmount()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(
            @Valid @RequestBody TransferDTO transferDTO,
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        log.info("Transfer request with roles: {}", roles);
        if (roles == null || (!roles.contains("ADMIN") && !roles.contains("USER"))) {
            throw new UnauthorizedException("Authentication required");
        }
        return ResponseEntity.ok(transactionService.transfer(transferDTO.getFromAccountId(), transferDTO.getToAccountId(), transferDTO.getAmount()));
    }
}

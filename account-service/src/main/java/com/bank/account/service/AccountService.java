package com.bank.account.service;

import com.bank.account.dto.AccountDTO;
import com.bank.account.entity.Account;
import com.bank.account.exception.BadRequestException;
import com.bank.account.exception.InsufficientBalanceException;
import com.bank.account.exception.ResourceNotFoundException;
import com.bank.account.repository.AccountRepository;
import com.bank.account.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        log.info("Fetching all accounts");
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found");
        }
        return accounts;
    }

    public Account getAccountById(Long id) {
        log.info("Fetching account with id: {}", id);
        ValidationUtil.validateNotNull(id, "Account ID");
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    public List<Account> getAccountsByUserId(Long userId) {
        log.info("Fetching accounts for user: {}", userId);
        ValidationUtil.validateNotNull(userId, "User ID");
        List<Account> accounts = accountRepository.findByUserId(userId);
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found for user: " + userId);
        }
        return accounts;
    }

    public Account createAccount(AccountDTO accountDTO) {
        log.info("Creating account for user: {}", accountDTO.getUserId());

        Account account = new Account();
        account.setUserId(accountDTO.getUserId());
        account.setAccountType(accountDTO.getAccountType());

        if (accountDTO.getBalance() != null) {
            account.setBalance(accountDTO.getBalance());
        } else {
            account.setBalance(BigDecimal.ZERO);
        }

        account.setAccountNumber(generateAccountNumber());
        return accountRepository.save(account);
    }

    /**
     * MAIN BALANCE UPDATE METHOD
     * Accepts "CREDIT" and "DEBIT" — matching transaction-service.
     */
    public Account updateBalance(Long id, BigDecimal amount, String operation) {
        log.info("Updating balance for account: {} op: {} amount: {}", id, operation, amount);

        ValidationUtil.validateNotNull(id, "Account ID");
        ValidationUtil.validateAmount(amount);
        ValidationUtil.validateNotEmpty(operation, "Operation");

        // 🔥 FIX: allow CREDIT & DEBIT (banking standard)
        if (!operation.equalsIgnoreCase("CREDIT") && !operation.equalsIgnoreCase("DEBIT")) {
            throw new BadRequestException("Operation must be either CREDIT or DEBIT");
        }

        Account account = getAccountById(id);
        log.info("Current balance: {}", account.getBalance());

        // CREDIT → add money
        if (operation.equalsIgnoreCase("CREDIT")) {
            account.setBalance(account.getBalance().add(amount));
        }

        // DEBIT → subtract money
        else if (operation.equalsIgnoreCase("DEBIT")) {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientBalanceException("Insufficient balance in account");
            }
            account.setBalance(account.getBalance().subtract(amount));
        }

        account.setUpdatedAt(LocalDateTime.now());
        Account updated = accountRepository.save(account);

        log.info("Updated balance: {}", updated.getBalance());
        return updated;
    }

    public void deleteAccount(Long id) {
        log.info("Deleting account: {}", id);
        ValidationUtil.validateNotNull(id, "Account ID");
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis() + new Random().nextInt(1000);
    }
}
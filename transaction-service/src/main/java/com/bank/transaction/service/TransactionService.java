package com.bank.transaction.service;

import com.bank.transaction.client.AccountClient;
import com.bank.transaction.dto.Account;
import com.bank.transaction.entity.Transaction;
import com.bank.transaction.enums.TransactionStatus;
import com.bank.transaction.enums.TransactionType;
import com.bank.transaction.exception.BadRequestException;
import com.bank.transaction.exception.TransactionFailedException;
import com.bank.transaction.repository.TransactionRepository;
import com.bank.transaction.util.TransactionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;

    public List<Transaction> getAllTransactions() {
        log.info("Fetching all transactions");
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            throw new BadRequestException("No transactions found");
        }
        return transactions;
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        log.info("Fetching transactions for account: {}", accountId);

        if (accountId == null) {
            throw new BadRequestException("Account ID cannot be null");
        }

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        if (transactions.isEmpty()) {
            throw new BadRequestException("No transactions found for account: " + accountId);
        }
        return transactions;
    }

    // ================================================
    // DEPOSIT
    // ================================================
    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount) {
        log.info("Deposit {} to account {}", amount, accountId);

        if (accountId == null) {
            throw new BadRequestException("Account ID cannot be null");
        }
        if (!TransactionUtil.isValidAmount(amount)) {
            throw new BadRequestException("Amount must be greater than zero");
        }

        try {
            Map<String, Object> request = Map.of(
                    "operation", "CREDIT",   // FINAL VALUE
                    "amount", amount
            );

            Account updatedAccount = accountClient.updateBalance(accountId, request);

            Transaction transaction = new Transaction();
            transaction.setAccountId(accountId);
            transaction.setTransactionType(TransactionType.DEPOSIT);
            transaction.setAmount(amount);
            transaction.setBalanceAfter(updatedAccount.getBalance());
            transaction.setDescription("Deposit to account");
            transaction.setStatus(TransactionStatus.SUCCESS.name());

            return transactionRepository.save(transaction);

        } catch (Exception e) {
            log.error("Deposit failed: {}", e.getMessage());
            throw new TransactionFailedException("Deposit transaction failed: " + e.getMessage());
        }
    }

    // ================================================
    // WITHDRAW
    // ================================================
    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount) {
        log.info("Withdraw {} from account {}", amount, accountId);

        if (accountId == null) {
            throw new BadRequestException("Account ID cannot be null");
        }
        if (!TransactionUtil.isValidAmount(amount)) {
            throw new BadRequestException("Amount must be greater than zero");
        }

        try {
            Map<String, Object> request = Map.of(
                    "operation", "DEBIT",   // FINAL VALUE
                    "amount", amount
            );

            Account updatedAccount = accountClient.updateBalance(accountId, request);

            Transaction transaction = new Transaction();
            transaction.setAccountId(accountId);
            transaction.setTransactionType(TransactionType.WITHDRAW);
            transaction.setAmount(amount);
            transaction.setBalanceAfter(updatedAccount.getBalance());
            transaction.setDescription("Withdrawal from account");
            transaction.setStatus(TransactionStatus.SUCCESS.name());

            return transactionRepository.save(transaction);

        } catch (Exception e) {
            log.error("Withdrawal failed: {}", e.getMessage());
            throw new TransactionFailedException("Withdrawal transaction failed: " + e.getMessage());
        }
    }

    // ================================================
    // TRANSFER
    // ================================================
    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        log.info("Transfer {} from {} to {}", amount, fromAccountId, toAccountId);

        if (fromAccountId == null) {
            throw new BadRequestException("Source account ID cannot be null");
        }
        if (toAccountId == null) {
            throw new BadRequestException("Destination account ID cannot be null");
        }
        if (!TransactionUtil.isValidAmount(amount)) {
            throw new BadRequestException("Amount must be greater than zero");
        }
        if (fromAccountId.equals(toAccountId)) {
            throw new BadRequestException("Cannot transfer to the same account");
        }

        try {
            // 1) DEBIT source account
            Map<String, Object> withdrawRequest = Map.of(
                    "operation", "DEBIT",
                    "amount", amount
            );
            Account fromAccount = accountClient.updateBalance(fromAccountId, withdrawRequest);

            // 2) CREDIT destination account
            Map<String, Object> depositRequest = Map.of(
                    "operation", "CREDIT",
                    "amount", amount
            );
            accountClient.updateBalance(toAccountId, depositRequest);

            // 3) Save only 1 transaction record (source)
            Transaction transaction = new Transaction();
            transaction.setAccountId(fromAccountId);
            transaction.setTransactionType(TransactionType.TRANSFER);
            transaction.setAmount(amount);
            transaction.setBalanceAfter(fromAccount.getBalance());
            transaction.setDescription("Transfer to account " + toAccountId);
            transaction.setStatus(TransactionStatus.SUCCESS.name());

            return transactionRepository.save(transaction);

        } catch (Exception e) {
            log.error("Transfer failed: {}", e.getMessage());
            throw new TransactionFailedException("Transfer transaction failed: " + e.getMessage());
        }
    }
}
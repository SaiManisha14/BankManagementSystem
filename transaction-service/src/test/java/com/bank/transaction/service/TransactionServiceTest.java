package com.bank.transaction.service;

import com.bank.transaction.client.AccountClient;
import com.bank.transaction.dto.Account;
import com.bank.transaction.entity.Transaction;
import com.bank.transaction.enums.TransactionType;
import com.bank.transaction.exception.BadRequestException;
import com.bank.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private Account account;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccountId(1L);
        transaction.setAmount(BigDecimal.valueOf(1000));
        transaction.setTransactionType(TransactionType.DEPOSIT);

        account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(10000));
    }

    @Test
    void getAllTransactions_ShouldReturnAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction));
        List<Transaction> result = transactionService.getAllTransactions();
        assertEquals(1, result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getAllTransactions_ShouldThrowException_WhenEmpty() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList());
        assertThrows(BadRequestException.class, () -> transactionService.getAllTransactions());
    }

    @Test
    void getTransactionsByAccountId_ShouldReturnTransactions() {
        when(transactionRepository.findByAccountId(1L)).thenReturn(Arrays.asList(transaction));
        List<Transaction> result = transactionService.getTransactionsByAccountId(1L);
        assertEquals(1, result.size());
        verify(transactionRepository, times(1)).findByAccountId(1L);
    }

    @Test
    void getTransactionsByAccountId_ShouldThrowException_WhenEmpty() {
        when(transactionRepository.findByAccountId(1L)).thenReturn(Arrays.asList());
        assertThrows(BadRequestException.class, () -> transactionService.getTransactionsByAccountId(1L));
    }

    @Test
    void deposit_ShouldCreateTransaction() {
        when(accountClient.updateBalance(eq(1L), any(Map.class))).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.deposit(1L, BigDecimal.valueOf(1000));

        assertNotNull(result);
        verify(accountClient, times(1)).updateBalance(eq(1L), any(Map.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void withdraw_ShouldCreateTransaction() {
        when(accountClient.updateBalance(eq(1L), any(Map.class))).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.withdraw(1L, BigDecimal.valueOf(500));

        assertNotNull(result);
        verify(accountClient, times(1)).updateBalance(eq(1L), any(Map.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void transfer_ShouldCreateTransaction() {
        when(accountClient.updateBalance(eq(1L), any(Map.class))).thenReturn(account);
        when(accountClient.updateBalance(eq(2L), any(Map.class))).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.transfer(1L, 2L, BigDecimal.valueOf(300));

        assertNotNull(result);
        verify(accountClient, times(2)).updateBalance(any(Long.class), any(Map.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void deposit_ShouldThrowException_WhenInvalidAmount() {
        assertThrows(BadRequestException.class, () -> transactionService.deposit(1L, BigDecimal.valueOf(-100)));
        verify(accountClient, never()).updateBalance(any(Long.class), any(Map.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void deposit_ShouldThrowException_WhenZeroAmount() {
        assertThrows(BadRequestException.class, () -> transactionService.deposit(1L, BigDecimal.ZERO));
        verify(accountClient, never()).updateBalance(any(Long.class), any(Map.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void deposit_ShouldThrowException_WhenNullAccountId() {
        assertThrows(BadRequestException.class, () -> transactionService.deposit(null, BigDecimal.valueOf(100)));
        verify(accountClient, never()).updateBalance(any(Long.class), any(Map.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void withdraw_ShouldThrowException_WhenInvalidAmount() {
        assertThrows(BadRequestException.class, () -> transactionService.withdraw(1L, BigDecimal.valueOf(0)));
        verify(accountClient, never()).updateBalance(any(Long.class), any(Map.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void withdraw_ShouldThrowException_WhenNegativeAmount() {
        assertThrows(BadRequestException.class, () -> transactionService.withdraw(1L, BigDecimal.valueOf(-500)));
        verify(accountClient, never()).updateBalance(any(Long.class), any(Map.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void transfer_ShouldThrowException_WhenSameAccount() {
        assertThrows(BadRequestException.class, () -> transactionService.transfer(1L, 1L, BigDecimal.valueOf(100)));
        verify(accountClient, never()).updateBalance(any(Long.class), any(Map.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void transfer_ShouldThrowException_WhenInvalidAmount() {
        assertThrows(BadRequestException.class, () -> transactionService.transfer(1L, 2L, BigDecimal.ZERO));
        verify(accountClient, never()).updateBalance(any(Long.class), any(Map.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}

package com.bank.transaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bank.transaction.enums.TransactionType;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long accountId;

@Enumerated(EnumType.STRING)
private TransactionType transactionType;

    
    private BigDecimal amount;
    
    private BigDecimal balanceAfter;
    
    private String description;
    
    private String status = "SUCCESS";
    
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

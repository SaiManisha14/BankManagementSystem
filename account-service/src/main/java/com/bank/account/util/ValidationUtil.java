package com.bank.account.util;

import com.bank.account.exception.BadRequestException;

import java.math.BigDecimal;

public class ValidationUtil {
    
    public static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new BadRequestException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        	throw new BadRequestException("Amount must be greater than zero");
        }
    }
    
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new BadRequestException(fieldName + " cannot be null");
        }
    }
    
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(fieldName + " cannot be empty");
        }
    }
    
    public static void validateAccountType(String accountType) {
        if (accountType == null || accountType.trim().isEmpty()) {
            throw new BadRequestException("Account type cannot be empty");
        }
        if (!accountType.equals("SAVINGS") && !accountType.equals("CURRENT")) {
            throw new BadRequestException("Account type must be either SAVINGS or CURRENT");
        }
    }
}


package com.bank.transaction.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class TransactionUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generateTransactionId() {
        return "TXN" + LocalDateTime.now().format(FORMATTER) + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isSufficientBalance(BigDecimal balance, BigDecimal amount) {
        return balance != null && amount != null && balance.compareTo(amount) >= 0;
    }

    public static String formatAmount(BigDecimal amount) {
        return String.format("%.2f", amount);
    }

    public static BigDecimal calculateFee(BigDecimal amount, double feePercentage) {
        return amount.multiply(BigDecimal.valueOf(feePercentage / 100));
    }
}

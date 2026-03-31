package com.bank.user.util;

import com.bank.user.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void validateEmail_ShouldPass_WhenEmailValid() {
        assertDoesNotThrow(() -> ValidationUtil.validateEmail("test@example.com"));
    }

    @Test
    void validateEmail_ShouldThrowException_WhenEmailNull() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.validateEmail(null));
    }

    @Test
    void validateEmail_ShouldThrowException_WhenEmailEmpty() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.validateEmail(""));
    }

    @Test
    void validateEmail_ShouldThrowException_WhenEmailInvalid() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.validateEmail("invalid-email"));
    }

    @Test
    void validatePhone_ShouldPass_WhenPhoneValid() {
        assertDoesNotThrow(() -> ValidationUtil.validatePhone("1234567890"));
    }

    @Test
    void validatePhone_ShouldThrowException_WhenPhoneNull() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.validatePhone(null));
    }

    @Test
    void validatePhone_ShouldThrowException_WhenPhoneEmpty() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.validatePhone(""));
    }

    @Test
    void validatePhone_ShouldThrowException_WhenPhoneInvalid() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.validatePhone("123"));
    }

    @Test
    void validateNotEmpty_ShouldPass_WhenValueValid() {
        assertDoesNotThrow(() -> ValidationUtil.validateNotEmpty("test", "Field"));
    }

    @Test
    void validateNotEmpty_ShouldThrowException_WhenValueNull() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.validateNotEmpty(null, "Field"));
    }

    @Test
    void validateNotEmpty_ShouldThrowException_WhenValueEmpty() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.validateNotEmpty("", "Field"));
    }

    @Test
    void validateNotNull_ShouldPass_WhenValueNotNull() {
        assertDoesNotThrow(() -> ValidationUtil.validateNotNull("test", "Field"));
    }

    @Test
    void validateNotNull_ShouldThrowException_WhenValueNull() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.validateNotNull(null, "Field"));
    }
}

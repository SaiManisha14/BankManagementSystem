package com.bank.auth.service;

import com.bank.auth.dto.LoginRequest;
import com.bank.auth.dto.LoginResponse;
import com.bank.auth.dto.RegisterRequest;
import com.bank.auth.dto.RegisterResponse;
import com.bank.auth.entity.AuthUser;
import com.bank.auth.exception.BadRequestException;
import com.bank.auth.repository.AuthUserRepository;
import com.bank.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");
        registerRequest.setEmail("test@example.com");
        registerRequest.setRole("USER");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        authUser = new AuthUser();
        authUser.setId(1L);
        authUser.setUsername("testuser");
        authUser.setPassword("encodedPassword");
        authUser.setEmail("test@example.com");
        authUser.setRole("USER");
    }

    @Test
    void register_ShouldReturnRegisterResponse_WhenUsernameNotExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(AuthUser.class))).thenReturn(authUser);

        RegisterResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("encodedPassword", response.getPassword());
        verify(userRepository, times(1)).save(any(AuthUser.class));
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(AuthUser.class));
    }

    @Test
    void login_ShouldReturnLoginResponse_WhenCredentialsValid() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", "USER", 1L)).thenReturn("token123");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("token123", response.getToken());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordInvalid() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> authService.login(loginRequest));
    }

    @Test
    void validateToken_ShouldReturnTrue_WhenTokenValid() {
        when(jwtUtil.validateToken("validToken")).thenReturn(true);

        boolean result = authService.validateToken("validToken");

        assertTrue(result);
    }

    @Test
    void register_ShouldThrowException_WhenUsernameEmpty() {
        registerRequest.setUsername("");
        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
    }

    @Test
    void register_ShouldThrowException_WhenEmailInvalid() {
        registerRequest.setEmail("invalid-email");
        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
    }

    @Test
    void register_ShouldThrowException_WhenPasswordTooShort() {
        registerRequest.setPassword("123");
        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
    }

    @Test
    void register_ShouldThrowException_WhenRoleInvalid() {
        registerRequest.setRole("INVALID");
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
    }

    @Test
    void register_ShouldThrowException_WhenAdminAlreadyExists() {
        registerRequest.setRole("ADMIN");
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.countByRole("ADMIN")).thenReturn(1L);
        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
    }

    @Test
    void register_ShouldSucceed_WhenAdminDoesNotExist() {
        registerRequest.setRole("ADMIN");
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.countByRole("ADMIN")).thenReturn(0L);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(AuthUser.class))).thenReturn(authUser);

        RegisterResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
    }

    @Test
    void login_ShouldThrowException_WhenUsernameEmpty() {
        loginRequest.setUsername("");
        assertThrows(BadRequestException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordEmpty() {
        loginRequest.setPassword("");
        assertThrows(BadRequestException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> authService.login(loginRequest));
    }

    @Test
    void validateToken_ShouldReturnFalse_WhenTokenInvalid() {
        when(jwtUtil.validateToken("invalidToken")).thenReturn(false);

        boolean result = authService.validateToken("invalidToken");

        assertFalse(result);
    }

    @Test
    void validateToken_ShouldThrowException_WhenTokenEmpty() {
        assertThrows(BadRequestException.class, () -> authService.validateToken(""));
    }

    @Test
    void register_ShouldSetDefaultRoleToUser_WhenRoleNotProvided() {
        registerRequest.setRole(null);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(AuthUser.class))).thenReturn(authUser);

        RegisterResponse response = authService.register(registerRequest);

        assertNotNull(response);
        verify(userRepository, times(1)).save(any(AuthUser.class));
    }
}

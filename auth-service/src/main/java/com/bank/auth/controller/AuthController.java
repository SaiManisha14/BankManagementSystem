package com.bank.auth.controller;

import com.bank.auth.dto.LoginResponse;
import com.bank.auth.dto.RegisterResponse;
import com.bank.auth.dto.LoginRequest;
import com.bank.auth.dto.RegisterRequest;
import com.bank.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request received for: {}", request.getUsername());
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "X-User-Roles", required = false) String roles) {
        log.info("Get all users request with roles: {}", roles);
        if (roles == null || !roles.contains("ADMIN")) {
            return ResponseEntity.status(403).body("Only ADMIN can view all users");
        }
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request received for: {}", request.getUsername());
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        return ResponseEntity.ok(authService.validateToken(jwtToken));
    }

    @PostMapping("/test-register")
    public ResponseEntity<String> testRegister(@RequestBody RegisterRequest request) {
        log.info("Test register request received for: {}", request.getUsername());
        return ResponseEntity.ok("Registration request received for: " + request.getUsername());
    }
}


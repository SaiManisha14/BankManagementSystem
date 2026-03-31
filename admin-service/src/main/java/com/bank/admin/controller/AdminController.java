package com.bank.admin.controller;

import com.bank.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/accounts")
    public ResponseEntity<?> getAllAccounts(
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            return ResponseEntity.status(403).body("Only ADMIN can view all accounts");
        }
        return ResponseEntity.ok(adminService.getAllAccounts());
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            return ResponseEntity.status(403).body("Only ADMIN can view all users");
        }
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions(
            @RequestHeader(value = "X-User-Roles", required = false) String roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            return ResponseEntity.status(403).body("Only ADMIN can view all transactions");
        }
        return ResponseEntity.ok(adminService.getAllTransactions());
    }
}

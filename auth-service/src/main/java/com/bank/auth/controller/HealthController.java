package com.bank.auth.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Auth service is running!";
    }
    
    @PostMapping("/test")
    public String test(@RequestBody String body) {
        return "Received: " + body;
    }
}
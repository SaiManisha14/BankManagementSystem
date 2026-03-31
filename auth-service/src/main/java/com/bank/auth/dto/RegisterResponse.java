package com.bank.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String username;
    private String password;  // Encrypted password
    private Long userId;
    private String message;
    
    public RegisterResponse(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

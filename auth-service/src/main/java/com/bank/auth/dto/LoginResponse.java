package com.bank.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String password;  // Encrypted password
    private Long userId;
    
    public LoginResponse(String token, String username, String password) {
        this.token = token;
        this.username = username;
        this.password = password;
    }
}

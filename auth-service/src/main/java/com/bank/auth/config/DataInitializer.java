package com.bank.auth.config;

import com.bank.auth.entity.AuthUser;
import com.bank.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user exists
        if (!userRepository.existsByUsername("admin")) {
            log.info("Creating default admin user...");
            
            AuthUser admin = new AuthUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@bank.com");
            admin.setRole("ADMIN");
            
            userRepository.save(admin);
            log.info("Default admin user created successfully");
        }
        
        // Check if test user exists
        if (!userRepository.existsByUsername("user1")) {
            log.info("Creating default test user...");
            
            AuthUser user = new AuthUser();
            user.setUsername("user1");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setEmail("user1@bank.com");
            user.setRole("USER");
            
            userRepository.save(user);
            log.info("Default test user created successfully");
        }
        
        log.info("Total users in database: {}", userRepository.count());
    }
}
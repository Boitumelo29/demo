package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.JwtService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        // Check if account with same email or phone exists
        if (accountRepository.existsByEmail(account.getEmail())) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Account with this email already exists"));
        }
        
        if (accountRepository.existsByCellNumber(account.getCellNumber())) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Account with this phone number already exists"));
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);

        String token = jwtService.generateToken(account.getAccountNumber());
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(Map.of("message", "Account created successfully",
                        "accountId", account.getAccountNumber(),
                        "token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username"); // email or cell number
        String password = loginData.get("password");

        Account existingAccount = accountRepository.findByEmail(username);
        
        // If not found by email, try cell number
        if (existingAccount == null) {
            existingAccount = accountRepository.findByCellNumber(username);
            if (existingAccount == null) {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Please enter correct username or password"));
            }
        }

        if (!passwordEncoder.matches(password, existingAccount.getPassword())) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Please enter correct username or password"));
        }

        String token = jwtService.generateToken(existingAccount.getAccountNumber());

        // Generate JWT token
        return ResponseEntity
            .ok()
            .body(Map.of("message", "Login successful", "accountId", existingAccount.getAccountNumber(), "token", token));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable Long accountNumber) {
        Account account = accountRepository.findById(accountNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return ResponseEntity.ok(account);
    }
}

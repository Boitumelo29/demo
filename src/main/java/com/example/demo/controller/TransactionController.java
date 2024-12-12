package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Account;
import com.example.demo.model.Transaction;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.JwtService;
import com.example.demo.repository.AccountRepository;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/{accountNumber}")
    public List<Transaction> getTransactionsByAccountNumber(@PathVariable Long accountNumber) {
        Account account = accountRepository.findById(accountNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return account.getTransactions();
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferFunds(
        @RequestHeader("Authorization") String token,
        @RequestBody Transaction transaction) {
        
        // Long authenticatedAccountId = Long.parseLong(jwtService.verifyToken(token));

        // get source account from repo
        Account sourceAccount = accountRepository.findById(Long.parseLong(token))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        transaction.setAccount(sourceAccount);

        if (!sourceAccount.getAccountNumber().equals(Long.parseLong(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "You are not authorized to transfer funds from this account"));
        }

        Transaction newTransaction = transactionRepository.save(transaction);

        return ResponseEntity.ok(newTransaction);
    }
}

package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    boolean existsByCellNumber(String cellNumber);
    Account findByEmail(String email);
    Account findByCellNumber(String cellNumber);
}

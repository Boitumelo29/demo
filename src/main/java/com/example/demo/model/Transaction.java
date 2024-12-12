package com.example.demo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "accountNumber")
    @JsonBackReference
    private Account account;

    private String accountName;

    private String beneficiaryReference;

    private String bank;

    private String myReference;

    private Long accNumber; // account number of the beneficiary

    private Date transactionDate;

    public Transaction() {}

    public Transaction(Double amount, String accountName, String beneficiaryReference, String bank, String myReference, Long accNumber, Date transactionDate) {
        this.amount = amount;
        this.accountName = accountName;
        this.beneficiaryReference = beneficiaryReference;
        this.bank = bank;
        this.myReference = myReference;
        this.accNumber = accNumber;
        this.transactionDate = transactionDate;
    }

    @PrePersist
    protected void onCreate() {
        this.transactionDate = new Date();
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBeneficiaryReference() {
        return beneficiaryReference;
    }

    public void setBeneficiaryReference(String beneficiaryReference) {
        this.beneficiaryReference = beneficiaryReference;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getMyReference() {
        return myReference;
    }

    public void setMyReference(String myReference) {
        this.myReference = myReference;
    }

    public Long getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(Long accNumber) {
        this.accNumber = accNumber;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}

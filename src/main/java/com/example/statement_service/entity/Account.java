package com.example.statement_service.entity;

import java.time.LocalDate;

public class Account {
    private String accountId;
    private double balance;

    // getters / setters
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
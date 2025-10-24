package com.elitebank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private LocalDateTime date;
    private double amount;
    private String type; // deposit, withdraw
    private String accountNumber;

    public Transaction(String transactionId, double amount, String type, String accountNumber) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.accountNumber = accountNumber;
        this.date = LocalDateTime.now();
    }

    public void display() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(date.format(formatter) + " - " + type.toUpperCase() + ": " + amount + " (Account: " + accountNumber + ")");
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public LocalDateTime getDate() { return date; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getAccountNumber() { return accountNumber; }
}
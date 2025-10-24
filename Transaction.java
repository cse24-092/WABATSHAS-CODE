package com.elitebank;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private LocalDateTime date;
    private double amount;
    private String type; // deposit, withdraw

    public Transaction(String transactionId, double amount, String type) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.date = LocalDateTime.now();
    }

    public void display() {
        System.out.println(date + " - " + type.toUpperCase() + ": " + amount);
    }
}

package com.elitebank;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer owner;
    protected List<String> auditNotes;

    public Account(String accountNumber, double balance, String branch, Customer owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.owner = owner;
        this.auditNotes = new ArrayList<>();
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public Customer getOwner() { return owner; }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive");
            return;
        }
        balance += amount;
        auditNotes.add("Deposit: " + amount);
    }

    // Withdraw behaviour delegated to Withdrawal-capable subclasses or implemented here
    public void withdraw(double amount) {
        // default withdraw: allow if funds available
        if (amount <= 0) {
            System.out.println("Withdraw amount must be positive");
            return;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds");
            return;
        }
        balance -= amount;
        auditNotes.add("Withdraw: " + amount);
    }

    public double getBalanceAmount() { return balance; }

    public List<String> getAuditNotes() { return auditNotes; }
}

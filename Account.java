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
    public String getBranch() { return branch; }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive");
            return;
        }
        balance += amount;
        auditNotes.add("Deposit: " + amount);
        System.out.println("Deposited: $" + amount + " to account " + accountNumber + ". New balance: $" + balance);
    }

    public void withdraw(double amount) {
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
        System.out.println("Withdrew: $" + amount + " from account " + accountNumber + ". New balance: $" + balance);
    }

    public double getBalanceAmount() { return balance; }

    public List<String> getAuditNotes() { return auditNotes; }
    
    public String getAccountType() {
        return this.getClass().getSimpleName();
    }
}
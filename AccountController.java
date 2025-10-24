package com.elitebank;

public class AccountController {
    private Bank bank;

    public AccountController(Bank bank) {
        this.bank = bank;
    }

    public Account openAccount(String customerId, String accountNumber, String type, double amount) {
        Customer owner = null;
        for (Customer c : bank.getCustomers()) {
            if (c.getCustomerId().equals(customerId)) {
                owner = c;
                break;
            }
        }
        if (owner == null) {
            System.out.println("Customer not found: " + customerId);
            return null;
        }
        return bank.openAccount(accountNumber, type, amount, owner);
    }

    public void deposit(String accountNumber, double amount) {
        bank.deposit(accountNumber, amount);
    }

    public void withdraw(String accountNumber, double amount) {
        bank.withdraw(accountNumber, amount);
    }

    public void applyInterest() {
        bank.applyInterest();
    }
    
    public Account findAccount(String accountNumber) {
        return bank.findAccount(accountNumber);
    }
}
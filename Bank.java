package com.elitebank;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String name;
    private String branch;
    private List<Customer> customers;
    private List<Account> accounts;

    public Bank(String name, String branch) {
        this.name = name;
        this.branch = branch;
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public Account openAccount(String accountNumber, String type, double amount, Customer owner) {
        Account acc;
        switch (type.toLowerCase()) {
            case "savings":
                acc = new SavingsAccount(accountNumber, amount, branch, owner);
                break;
            case "investment":
                acc = new InvestmentAccount(accountNumber, amount, branch, owner);
                break;
            case "cheque":
            default:
                acc = new ChequeAccount(accountNumber, amount, branch, owner);
                break;
        }
        owner.addAccount(acc);
        addAccount(acc);
        return acc;
    }

    public void deposit(String accountNumber, double amount) {
        Account a = findAccount(accountNumber);
        if (a != null) a.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount) {
        Account a = findAccount(accountNumber);
        if (a != null) a.withdraw(amount);
    }

    public void applyInterest() {
        for (Account a : accounts) {
            if (a instanceof InterestBearing) {
                ((InterestBearing)a).applyMonthlyInterest();
            }
        }
    }

    public Account findAccount(String accountNumber) {
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(accountNumber)) return a;
        }
        return null;
    }

    public void printCustomerSummary(String customerId) {
        for (Customer c : customers) {
            if (c.getCustomerId().equals(customerId)) {
                System.out.println("Customer: " + c.getFirstName() + " " + c.getLastName());
                for (Account a : c.getAccounts()) {
                    System.out.println(" - Account: " + a.getAccountNumber() + " Balance: " + a.getBalance());
                }
            }
        }
    }

    public String getName() { return name; }
    public String getBranch() { return branch; }
    public List<Customer> getCustomers() { return customers; }
    public List<Account> getAccounts() { return accounts; }
}
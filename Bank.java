// [file name]: Bank.java
package com.elitebank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String name;
    private String branch;
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private List<Customer> customers;
    private List<Account> accounts;

    public Bank(String name, String branch) {
        this.name = name;
        this.branch = branch;
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
        
        // Load initial data from database
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        try {
            this.customers = customerDAO.getAllCustomers();
            for (Customer customer : customers) {
                accounts.addAll(customer.getAccounts());
            }
        } catch (SQLException e) {
            System.err.println("Error loading data from database: " + e.getMessage());
        }
    }

    public void addCustomer(Customer customer) {
        try {
            customerDAO.addCustomer(customer);
            customers.add(customer);
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }

    public void addAccount(Account account) {
        try {
            accountDAO.addAccount(account);
            accounts.add(account);
        } catch (SQLException e) {
            System.err.println("Error adding account: " + e.getMessage());
        }
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
        try {
            Account a = findAccount(accountNumber);
            if (a != null) {
                a.deposit(amount);
                accountDAO.updateAccountBalance(accountNumber, a.getBalance());
                accountDAO.addAuditNote(accountNumber, "Deposit: " + amount);
            }
        } catch (SQLException e) {
            System.err.println("Error during deposit: " + e.getMessage());
        }
    }

    public void withdraw(String accountNumber, double amount) {
        try {
            Account a = findAccount(accountNumber);
            if (a != null) {
                a.withdraw(amount);
                accountDAO.updateAccountBalance(accountNumber, a.getBalance());
                accountDAO.addAuditNote(accountNumber, "Withdraw: " + amount);
            }
        } catch (SQLException e) {
            System.err.println("Error during withdrawal: " + e.getMessage());
        }
    }

    public void applyInterest() {
        for (Account a : accounts) {
            if (a instanceof InterestBearing) {
                ((InterestBearing)a).applyMonthlyInterest();
                try {
                    accountDAO.updateAccountBalance(a.getAccountNumber(), a.getBalance());
                } catch (SQLException e) {
                    System.err.println("Error updating balance after interest: " + e.getMessage());
                }
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
    
    // Additional method for transaction recording
    public void recordTransaction(Transaction transaction) {
        try {
            transactionDAO.addTransaction(transaction);
        } catch (SQLException e) {
            System.err.println("Error recording transaction: " + e.getMessage());
        }
    }
    
    public List<Transaction> getCustomerTransactions(Customer customer) {
        try {
            return transactionDAO.getTransactionsByCustomer(customer.getCustomerId());
        } catch (SQLException e) {
            System.err.println("Error getting transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

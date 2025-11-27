// [file name]: AccountDAO.java
package com.elitebank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    
    public void addAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, monthly_rate) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getOwner().getCustomerId());
            pstmt.setString(3, account.getClass().getSimpleName().replace("Account", "").toUpperCase());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5, account.getBranch());
            
            if (account instanceof InterestBearing) {
                pstmt.setDouble(6, ((InterestBearing) account).monthlyRate());
            } else {
                pstmt.setDouble(6, 0.0);
            }
            
            pstmt.executeUpdate();
            
            // Save initial audit note
            addAuditNote(account.getAccountNumber(), "Account created with initial balance: " + account.getBalance());
        }
    }
    
    public Account findAccountByNumber(String accountNumber) throws SQLException {
        String sql = "SELECT a.*, c.customer_id, c.first_name, c.last_name, c.username, c.password, " +
                    "c.address_line1, c.city, c.country_code " +
                    "FROM accounts a JOIN customers c ON a.customer_id = c.customer_id " +
                    "WHERE a.account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        }
        return null;
    }
    
    public List<Account> findAccountsByCustomerId(String customerId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.*, c.customer_id, c.first_name, c.last_name, c.username, c.password, " +
                    "c.address_line1, c.city, c.country_code " +
                    "FROM accounts a JOIN customers c ON a.customer_id = c.customer_id " +
                    "WHERE a.customer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        }
        return accounts;
    }
    
    public void updateAccountBalance(String accountNumber, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNumber);
            pstmt.executeUpdate();
        }
    }
    
    public void addAuditNote(String accountNumber, String note) throws SQLException {
        String sql = "INSERT INTO audit_notes (account_number, note_text) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, note);
            pstmt.executeUpdate();
        }
    }
    
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        String accountNumber = rs.getString("account_number");
        double balance = rs.getDouble("balance");
        String branch = rs.getString("branch");
        String accountType = rs.getString("account_type");
        
        // Create customer
        String customerId = rs.getString("customer_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String username = rs.getString("username");
        String password = rs.getString("password");
        
        String line1 = rs.getString("address_line1");
        String city = rs.getString("city");
        String countryCode = rs.getString("country_code");
        Address address = new Address(line1, city, countryCode);
        
        Customer customer = new Customer(customerId, firstName, lastName, username, password, address);
        
        // Create appropriate account type
        Account account;
        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                account = new SavingsAccount(accountNumber, balance, branch, customer);
                break;
            case "INVESTMENT":
                account = new InvestmentAccount(accountNumber, balance, branch, customer);
                break;
            case "CHEQUE":
            default:
                account = new ChequeAccount(accountNumber, balance, branch, customer);
                break;
        }
        
        return account;
    }
}
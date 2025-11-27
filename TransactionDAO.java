// [file name]: TransactionDAO.java
package com.elitebank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    
    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (transaction_id, account_number, amount, transaction_type, description) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, transaction.getTransactionId());
            pstmt.setString(2, transaction.getAccountNumber());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getType().toUpperCase());
            pstmt.setString(5, "Transaction: " + transaction.getType());
            
            pstmt.executeUpdate();
        }
    }
    
    public List<Transaction> getTransactionsByCustomer(String customerId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.* FROM transactions t " +
                    "JOIN accounts a ON t.account_number = a.account_number " +
                    "WHERE a.customer_id = ? ORDER BY t.transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String transactionId = rs.getString("transaction_id");
                double amount = rs.getDouble("amount");
                String type = rs.getString("transaction_type").toLowerCase();
                String accountNumber = rs.getString("account_number");
                
                Transaction transaction = new Transaction(transactionId, amount, type, accountNumber);
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
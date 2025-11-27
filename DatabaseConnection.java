// [file name]: DatabaseConnection.java
package com.elitebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:h2:./elitebank;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    
    static {
        initializeDatabase();
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    private static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            
            // Create customers table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS customers (" +
                "customer_id VARCHAR(20) PRIMARY KEY, " +
                "first_name VARCHAR(50) NOT NULL, " +
                "last_name VARCHAR(50) NOT NULL, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(100) NOT NULL, " +
                "address_line1 VARCHAR(100), " +
                "city VARCHAR(50), " +
                "country_code VARCHAR(10))"
            );
            
            // Create accounts table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_number VARCHAR(20) PRIMARY KEY, " +
                "customer_id VARCHAR(20) NOT NULL, " +
                "account_type VARCHAR(20) NOT NULL, " +
                "balance DECIMAL(15,2) DEFAULT 0.00, " +
                "branch VARCHAR(50), " +
                "monthly_rate DECIMAL(5,2) DEFAULT 0.00, " +
                "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))"
            );
            
            // Create transactions table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "transaction_id VARCHAR(20) PRIMARY KEY, " +
                "account_number VARCHAR(20) NOT NULL, " +
                "amount DECIMAL(15,2) NOT NULL, " +
                "transaction_type VARCHAR(20) NOT NULL, " +
                "transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "description VARCHAR(200), " +
                "FOREIGN KEY (account_number) REFERENCES accounts(account_number))"
            );
            
            // Create audit_notes table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS audit_notes (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "account_number VARCHAR(20) NOT NULL, " +
                "note_text TEXT NOT NULL, " +
                "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (account_number) REFERENCES accounts(account_number))"
            );
            
            System.out.println("Database initialized successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
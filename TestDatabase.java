// [file name]: TestDatabase.java
package com.elitebank;

import java.sql.*;

public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("=== Testing Elite Bank Database ===");
        
        try {
            // Test connection
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("✅ Database connection successful!");
            
            // Show tables
            showTableContents(conn, "customers");
            showTableContents(conn, "accounts");
            showTableContents(conn, "transactions");
            showTableContents(conn, "audit_notes");
            
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
        }
        
        System.out.println("\n" + DatabaseConnection.getConnectionDetails());
    }
    
    private static void showTableContents(Connection conn, String tableName) throws SQLException {
        System.out.println("\n--- " + tableName.toUpperCase() + " TABLE ---");
        
        String sql = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Print column headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", metaData.getColumnName(i));
            }
            System.out.println();
            
            // Print separator
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", "--------------------");
            }
            System.out.println();
            
            // Print data
            int rowCount = 0;
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value != null && value.length() > 18) {
                        value = value.substring(0, 15) + "...";
                    }
                    System.out.printf("%-20s", value);
                }
                System.out.println();
                rowCount++;
            }
            
            if (rowCount == 0) {
                System.out.println("(No data found)");
            }
        }
    }
}
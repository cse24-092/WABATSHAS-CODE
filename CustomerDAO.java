// [file name]: CustomerDAO.java
package com.elitebank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    public void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (customer_id, first_name, last_name, username, password, address_line1, city, country_code) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getLastName());
            pstmt.setString(4, customer.getUsername());
            
            // Since we can't use getPassword(), we'll use a default password or find another way
            // For now, using "default" as password - you'll need to handle this properly
            pstmt.setString(5, "default"); 
            
            Address address = customer.getAddress();
            pstmt.setString(6, address != null ? address.toString().split(", ")[0] : "");
            pstmt.setString(7, address != null ? address.toString().split(", ")[1] : "");
            pstmt.setString(8, address != null ? address.toString().split(", ")[2] : "");
            
            pstmt.executeUpdate();
        }
    }
    
    public Customer findCustomerByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM customers WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String customerId = rs.getString("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String password = rs.getString("password");
                
                String line1 = rs.getString("address_line1");
                String city = rs.getString("city");
                String countryCode = rs.getString("country_code");
                Address address = new Address(line1, city, countryCode);
                
                return new Customer(customerId, firstName, lastName, username, password, address);
            }
        }
        return null;
    }
    
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String customerId = rs.getString("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                
                String line1 = rs.getString("address_line1");
                String city = rs.getString("city");
                String countryCode = rs.getString("country_code");
                Address address = new Address(line1, city, countryCode);
                
                customers.add(new Customer(customerId, firstName, lastName, username, password, address));
            }
        }
        return customers;
    }
    
    public boolean authenticate(String username, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customers WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
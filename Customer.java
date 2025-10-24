package com.elitebank;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Address address;
    private List<Account> accounts;

    public Customer(String customerId, String firstName, String lastName, String username, String password, Address address) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

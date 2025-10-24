package com.elitebank;

public class CustomerController {
    private Bank bank;

    public CustomerController(Bank bank) {
        this.bank = bank;
    }

    public void registerCustomer(Customer customer) {
        bank.addCustomer(customer);
    }

    public boolean authenticate(String username, String password) {
        for (Customer c : bank.customers) {
            if (c.authenticate(username, password)) return true;
        }
        return false;
    }
}

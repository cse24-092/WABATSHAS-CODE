package com.elitebank;

public class CustomerUI {
    private CustomerController controller;

    public CustomerUI(CustomerController controller) {
        this.controller = controller;
    }

    public void register(Customer customer) {
        controller.registerCustomer(customer);
        System.out.println("Registered customer: " + customer.getCustomerId());
    }

    public boolean login(String username, String password) {
        return controller.authenticate(username, password);
    }
}

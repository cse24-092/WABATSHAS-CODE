package com.elitebank;

public class EmploymentDetails {
    private String employerName;
    private Address employerAddress;

    public EmploymentDetails(String employerName, Address employerAddress) {
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    public String getEmployerName() { return employerName; }
    public Address getEmployerAddress() { return employerAddress; }
}
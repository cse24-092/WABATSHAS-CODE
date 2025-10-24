package com.elitebank;

public class InvestmentAccount extends Account implements InterestBearing {
    private double monthlyRate;

    public InvestmentAccount(String accountNumber, double balance, String branch, Customer owner) {
        super(accountNumber, balance, branch, owner);
        this.monthlyRate = 1.0; // higher example rate
    }

    @Override
    public double monthlyRate() {
        return monthlyRate;
    }

    @Override
    public void applyMonthlyInterest() {
        double interest = balance * (monthlyRate / 100.0);
        balance += interest;
        getAuditNotes().add("Interest applied: " + interest);
    }
}
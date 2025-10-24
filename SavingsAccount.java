package com.elitebank;

public class SavingsAccount extends Account implements InterestBearing {
    private double monthlyRate; // as percentage e.g., 0.5 for 0.5%

    public SavingsAccount(String accountNumber, double balance, String branch, Customer owner) {
        super(accountNumber, balance, branch, owner);
        this.monthlyRate = 0.5; // default example rate
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

    @Override
    public void withdraw(double amount) {
        // Savings accounts don't allow withdrawals - only deposits
        System.out.println("Withdrawals are not allowed from Savings accounts. Please use a Cheque account for withdrawals.");
        getAuditNotes().add("Withdrawal attempted but denied: " + amount);
    }
}
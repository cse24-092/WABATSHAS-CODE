package com.elitebank;

public class ChequeAccount extends Account implements Withdrawal {
    private EmploymentDetails employment;

    public ChequeAccount(String accountNumber, double balance, String branch, Customer owner) {
        super(accountNumber, balance, branch, owner);
    }

    public void setEmploymentDetails(EmploymentDetails employment) {
        this.employment = employment;
    }

    @Override
    public void withdraw(double amount) {
        // cheque account can have overdraft rules if needed, but simple check:
        if (amount <= 0) {
            System.out.println("Withdraw amount must be positive");
            return;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds in cheque account");
            return;
        }
        balance -= amount;
        getAuditNotes().add("Cheque Withdraw: " + amount);
    }
}

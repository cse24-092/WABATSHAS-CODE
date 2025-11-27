// [file name]: TransactionController.java
package com.elitebank;

import java.util.ArrayList;
import java.util.List;

public class TransactionController {
    private Bank bank;

    public TransactionController(Bank bank) {
        this.bank = bank;
    }

    public void recordTransaction(Transaction t) {
        bank.recordTransaction(t);
    }

    public List<Transaction> getTransactionHistory() {
        // This would return all transactions - implement as needed
        return new ArrayList<>();
    }
    
    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        // Implement based on your needs
        return new ArrayList<>();
    }
    
    public List<Transaction> getTransactionsForCustomer(Customer customer) {
        return bank.getCustomerTransactions(customer);
    }
}

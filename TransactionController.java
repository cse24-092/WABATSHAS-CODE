package com.elitebank;

import java.util.ArrayList;
import java.util.List;

public class TransactionController {
    private List<Transaction> transactions = new ArrayList<>();

    public void recordTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> getTransactionHistory() {
        return transactions;
    }
    
    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAccountNumber().equals(accountNumber)) {
                accountTransactions.add(t);
            }
        }
        return accountTransactions;
    }
    
    public List<Transaction> getTransactionsForCustomer(Customer customer) {
        List<Transaction> customerTransactions = new ArrayList<>();
        for (Account account : customer.getAccounts()) {
            customerTransactions.addAll(getTransactionsForAccount(account.getAccountNumber()));
        }
        return customerTransactions;
    }
}
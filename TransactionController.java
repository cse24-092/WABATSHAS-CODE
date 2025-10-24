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
}

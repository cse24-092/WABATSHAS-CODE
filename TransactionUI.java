package com.elitebank;

public class TransactionUI {
    private TransactionController controller;

    public TransactionUI(TransactionController controller) {
        this.controller = controller;
    }

    public void viewTransactionHistory() {
        for (Transaction t : controller.getTransactionHistory()) {
            t.display();
        }
    }
    
    public void viewAccountTransactionHistory(String accountNumber) {
        for (Transaction t : controller.getTransactionsForAccount(accountNumber)) {
            t.display();
        }
    }
}
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
}

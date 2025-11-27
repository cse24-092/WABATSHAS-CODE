// [file name]: Main.java
package com.elitebank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Bank bank;
    private CustomerController customerController;
    private AccountController accountController;
    private TransactionController transactionController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeBankingSystem();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/elitebank/JAVABankingsystem.fxml"));
        Parent root = loader.load();
        
        BankingSystemController controller = loader.getController();
        controller.setDependencies(bank, customerController, accountController, transactionController);
        
        Scene scene = new Scene(root, 900, 650);
        primaryStage.setTitle("🏦 Elite Banking System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeBankingSystem() {
        bank = new Bank("Elite Bank", "Main Branch");
        customerController = new CustomerController(bank);
        accountController = new AccountController(bank);
        transactionController = new TransactionController(bank);
        
        // Only setup sample data if no customers exist
        if (bank.getCustomers().isEmpty()) {
            setupSampleCustomers();
        }
    }

    private void setupSampleCustomers() {
        // Sample customer 1
        Address address1 = new Address("123 Main St", "Gaborone", "BW");
        Customer customer1 = new Customer("CUST1001", "John", "Doe", "john", "password", address1);
        customerController.registerCustomer(customer1);
        
        // Create sample accounts with balances
        accountController.openAccount("CUST1001", "ACC1001", "savings", 1500.0);
        accountController.openAccount("CUST1001", "ACC1002", "cheque", 750.50);
        accountController.openAccount("CUST1001", "ACC1003", "investment", 3000.0);
        
        // Add some sample transactions
        transactionController.recordTransaction(new Transaction("TXN001", 500.0, "deposit", "ACC1001"));
        transactionController.recordTransaction(new Transaction("TXN002", 200.0, "withdraw", "ACC1002"));
        transactionController.recordTransaction(new Transaction("TXN003", 1000.0, "deposit", "ACC1003"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

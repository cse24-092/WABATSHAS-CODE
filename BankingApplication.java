package com.elitebank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingApplication extends Application {
    private Bank bank;
    private CustomerController customerController;
    private AccountController accountController;
    private TransactionController transactionController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the banking system
        initializeBankingSystem();
        
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/elitebank/JAVABankingsystem.fxml"));
        Parent root = loader.load();
        
        // Get the controller and inject dependencies
        BankingSystemController controller = loader.getController();
        controller.setDependencies(bank, customerController, accountController, transactionController);
        
        // Set up the scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Elite Banking System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeBankingSystem() {
        // Create bank instance
        bank = new Bank("Elite Bank", "Main Branch");
        
        // Initialize controllers
        customerController = new CustomerController(bank);
        accountController = new AccountController(bank);
        transactionController = new TransactionController();
        
        // Setup sample data
        setupSampleData();
    }

    private void setupSampleData() {
        // Create sample address
        Address address = new Address("123 Main St", "Gaborone", "BW");
        
        // Create sample customer
        Customer customer = new Customer("CUST001", "John", "Doe", "john", "password", address);
        customerController.registerCustomer(customer);
        
        // Create sample accounts
        accountController.openAccount("CUST001", "ACC001", "savings", 1000.0);
        accountController.openAccount("CUST001", "ACC002", "cheque", 500.0);
        accountController.openAccount("CUST001", "ACC003", "investment", 2000.0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
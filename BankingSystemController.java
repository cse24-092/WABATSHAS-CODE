package com.elitebank;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class BankingSystemController {
    private Bank bank;
    private CustomerController customerController;
    private AccountController accountController;
    private TransactionController transactionController;
    private Customer currentCustomer;

    // LOGIN Tab elements
    @FXML private Tab loginTab;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblLoginStatus;

    // REGISTER Tab elements
    @FXML private Tab registerTab;
    @FXML private TextField txtRegFirstName;
    @FXML private TextField txtRegLastName;
    @FXML private TextField txtRegUsername;
    @FXML private PasswordField txtRegPassword;
    @FXML private TextField txtRegAddress;
    @FXML private TextField txtRegCity;
    @FXML private TextField txtRegCountry;

    // DASHBOARD Tab elements
    @FXML private Tab dashboardTab;
    @FXML private Label lblWelcome;
    @FXML private Label lblTotalBalance;
    @FXML private TableView<Account> accountsTable;
    @FXML private TableColumn<Account, String> colAccountNumber;
    @FXML private TableColumn<Account, String> colAccountType;
    @FXML private TableColumn<Account, Double> colBalance;
    @FXML private TableColumn<Account, String> colBranch;

    // OPEN ACCOUNT elements
    @FXML private ComboBox<String> cmbAccountType;
    @FXML private TextField txtInitialDeposit;
    @FXML private Label lblMinDeposit;

    // TRANSACTIONS Tab elements
    @FXML private Tab transactionsTab;
    @FXML private ComboBox<String> cmbFromAccount;
    @FXML private TextField txtAmount;
    @FXML private ComboBox<String> cmbTransactionType;
    @FXML private Label lblAccountBalance;
    @FXML private Label lblAccountType;

    // HISTORY Tab elements
    @FXML private Tab historyTab;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> colTxnAccountNumber;
    @FXML private TableColumn<Transaction, Double> colTxnAmount;
    @FXML private TableColumn<Transaction, String> colTxnType;
    @FXML private TableColumn<Transaction, String> colTxnDate;

    public void setDependencies(Bank bank, CustomerController customerController, 
                               AccountController accountController, TransactionController transactionController) {
        this.bank = bank;
        this.customerController = customerController;
        this.accountController = accountController;
        this.transactionController = transactionController;
        initializeUI();
    }

    private void initializeUI() {
        // Initialize combo boxes
        cmbTransactionType.getItems().addAll("Deposit", "Withdraw");
        cmbAccountType.getItems().addAll("Savings Account", "Cheque Account", "Investment Account");
        
        // Initialize account table columns
        colAccountNumber.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAccountNumber()));
        colAccountType.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAccountType()));
        colBalance.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getBalance()).asObject());
        colBranch.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBranch()));
            
        // Initialize transaction table columns
        colTxnAccountNumber.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAccountNumber()));
        colTxnAmount.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        colTxnType.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));
        colTxnDate.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate().toString()));

        // Start with only login tab visible
        showLoginView();
    }

    @FXML
    private void handleAccountTypeChange() {
        String selectedType = cmbAccountType.getValue();
        if (selectedType != null) {
            switch (selectedType) {
                case "Savings Account":
                    lblMinDeposit.setText("Minimum Deposit: $50.00 (No withdrawals allowed)");
                    txtInitialDeposit.setPromptText("Enter at least $50");
                    break;
                case "Cheque Account":
                    lblMinDeposit.setText("No minimum deposit required");
                    txtInitialDeposit.setPromptText("Enter initial deposit (optional)");
                    break;
                case "Investment Account":
                    lblMinDeposit.setText("Minimum Deposit: $500.00");
                    txtInitialDeposit.setPromptText("Enter at least $500");
                    break;
            }
        }
    }

    private void showLoginView() {
        dashboardTab.setDisable(true);
        transactionsTab.setDisable(true);
        historyTab.setDisable(true);
        loginTab.setDisable(false);
        registerTab.setDisable(false);
        loginTab.getTabPane().getSelectionModel().select(loginTab);
    }

    private void showCustomerView() {
        dashboardTab.setDisable(false);
        transactionsTab.setDisable(false);
        historyTab.setDisable(false);
        loginTab.setDisable(true);
        registerTab.setDisable(true);
        dashboardTab.getTabPane().getSelectionModel().select(dashboardTab);
        
        lblWelcome.setText("Welcome, " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
        refreshCustomerData();
    }

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        
        if (customerController.authenticate(username, password)) {
            currentCustomer = customerController.findCustomerByUsername(username);
            
            if (currentCustomer != null) {
                lblLoginStatus.setText("Login successful!");
                lblLoginStatus.setStyle("-fx-text-fill: green;");
                showCustomerView();
            }
        } else {
            lblLoginStatus.setText("Invalid username or password");
            lblLoginStatus.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleRegister() {
        try {
            String firstName = txtRegFirstName.getText();
            String lastName = txtRegLastName.getText();
            String username = txtRegUsername.getText();
            String password = txtRegPassword.getText();
            String addressLine = txtRegAddress.getText();
            String city = txtRegCity.getText();
            String country = txtRegCountry.getText();
            
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill in all required fields");
                return;
            }
            
            String customerId = "CUST" + (bank.getCustomers().size() + 1001);
            Address address = new Address(addressLine, city, country);
            Customer customer = new Customer(customerId, firstName, lastName, username, password, address);
            customerController.registerCustomer(customer);
            
            // Auto-login after registration
            currentCustomer = customer;
            showCustomerView();
            clearRegistrationFields();
            
        } catch (Exception e) {
            showAlert("Error", "Registration failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleTransaction() {
        try {
            String accountNumber = cmbFromAccount.getValue();
            String type = cmbTransactionType.getValue();
            double amount = Double.parseDouble(txtAmount.getText());
            
            if (accountNumber == null || type == null) {
                showAlert("Error", "Please select account and transaction type");
                return;
            }
            
            if (amount <= 0) {
                showAlert("Error", "Amount must be greater than zero");
                return;
            }
            
            Account account = accountController.findAccount(accountNumber);
            if (account == null || !account.getOwner().getCustomerId().equals(currentCustomer.getCustomerId())) {
                showAlert("Error", "Account not found or you don't have access to this account");
                return;
            }
            
            // Check if withdrawal is attempted from Savings account
            if ("Withdraw".equals(type) && account instanceof SavingsAccount) {
                showAlert("Error", "Withdrawals are not allowed from Savings accounts. Please use a Cheque account for withdrawals.");
                return;
            }
            
            double oldBalance = account.getBalance();
            
            if ("Deposit".equals(type)) {
                accountController.deposit(accountNumber, amount);
                showAlert("Success", "Deposit of $" + String.format("%.2f", amount) + " completed successfully!\n" +
                          "Old Balance: $" + String.format("%.2f", oldBalance) + "\n" +
                          "New Balance: $" + String.format("%.2f", account.getBalance()));
            } else if ("Withdraw".equals(type)) {
                if (amount > account.getBalance()) {
                    showAlert("Error", "Insufficient funds for withdrawal");
                    return;
                }
                accountController.withdraw(accountNumber, amount);
                showAlert("Success", "Withdrawal of $" + String.format("%.2f", amount) + " completed successfully!\n" +
                          "Old Balance: $" + String.format("%.2f", oldBalance) + "\n" +
                          "New Balance: $" + String.format("%.2f", account.getBalance()));
            }
            
            // Record transaction
            Transaction transaction = new Transaction(
                "TXN" + System.currentTimeMillis(), amount, type.toLowerCase(), accountNumber
            );
            transactionController.recordTransaction(transaction);
            
            refreshCustomerData();
            clearTransactionFields();
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        } catch (Exception e) {
            showAlert("Error", "Transaction failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenAccount() {
        try {
            String accountType = cmbAccountType.getValue();
            String initialDepositText = txtInitialDeposit.getText();
            
            if (accountType == null) {
                showAlert("Error", "Please select an account type");
                return;
            }
            
            double initialDeposit = 0.0;
            if (!initialDepositText.isEmpty()) {
                initialDeposit = Double.parseDouble(initialDepositText);
                if (initialDeposit < 0) {
                    showAlert("Error", "Initial deposit cannot be negative");
                    return;
                }
            }
            
            // Validate minimum deposit requirements
            if (accountType.equals("Savings Account") && initialDeposit < 50.0) {
                showAlert("Error", "Savings account requires minimum deposit of $50.00");
                return;
            }
            
            if (accountType.equals("Investment Account") && initialDeposit < 500.0) {
                showAlert("Error", "Investment account requires minimum deposit of $500.00");
                return;
            }
            
            // Generate account number and map account type
            String accountNumber = "ACC" + (currentCustomer.getAccounts().size() + 1001);
            String typeCode = accountType.replace(" Account", "").toLowerCase();
            
            Account account = accountController.openAccount(currentCustomer.getCustomerId(), accountNumber, typeCode, initialDeposit);
            if (account != null) {
                showAlert("Success", 
                    "Account opened successfully!\n" +
                    "Account Number: " + accountNumber + "\n" +
                    "Account Type: " + accountType + "\n" +
                    "Initial Balance: $" + String.format("%.2f", initialDeposit));
                
                refreshCustomerData();
                clearOpenAccountFields();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount for initial deposit");
        } catch (Exception e) {
            showAlert("Error", "Failed to open account: " + e.getMessage());
        }
    }

    @FXML
    private void handleAccountSelection() {
        String selectedAccount = cmbFromAccount.getValue();
        if (selectedAccount != null) {
            Account account = accountController.findAccount(selectedAccount);
            if (account != null) {
                lblAccountBalance.setText("Current Balance: $" + String.format("%.2f", account.getBalance()));
                lblAccountType.setText("Account Type: " + account.getAccountType());
                
                // Update transaction type availability based on account type
                if (account instanceof SavingsAccount) {
                    cmbTransactionType.getItems().clear();
                    cmbTransactionType.getItems().add("Deposit");
                    cmbTransactionType.setValue("Deposit");
                    cmbTransactionType.setDisable(true);
                } else {
                    cmbTransactionType.getItems().clear();
                    cmbTransactionType.getItems().addAll("Deposit", "Withdraw");
                    cmbTransactionType.setDisable(false);
                }
            }
        }
    }

    @FXML
    private void handleLogout() {
        currentCustomer = null;
        showLoginView();
        lblLoginStatus.setText("Logged out successfully");
        lblLoginStatus.setStyle("-fx-text-fill: green;");
        clearLoginFields();
        clearTransactionFields();
    }

    private void refreshCustomerData() {
        if (currentCustomer != null) {
            // Refresh accounts table
            ObservableList<Account> accountData = FXCollections.observableArrayList(currentCustomer.getAccounts());
            accountsTable.setItems(accountData);
            
            // Refresh account combo box
            cmbFromAccount.getItems().clear();
            for (Account account : currentCustomer.getAccounts()) {
                cmbFromAccount.getItems().add(account.getAccountNumber());
            }
            
            // Refresh transactions
            List<Transaction> customerTransactions = transactionController.getTransactionsForCustomer(currentCustomer);
            ObservableList<Transaction> transactionData = FXCollections.observableArrayList(customerTransactions);
            transactionTable.setItems(transactionData);
            
            // Update total balance
            double totalBalance = currentCustomer.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum();
            lblTotalBalance.setText("Total Balance: $" + String.format("%.2f", totalBalance));
        }
    }

    private void clearLoginFields() {
        txtUsername.clear();
        txtPassword.clear();
    }

    private void clearRegistrationFields() {
        txtRegFirstName.clear();
        txtRegLastName.clear();
        txtRegUsername.clear();
        txtRegPassword.clear();
        txtRegAddress.clear();
        txtRegCity.clear();
        txtRegCountry.clear();
    }

    private void clearTransactionFields() {
        txtAmount.clear();
        lblAccountBalance.setText("Current Balance: $0.00");
        lblAccountType.setText("Account Type: None");
        cmbTransactionType.setDisable(false);
        cmbTransactionType.getItems().clear();
        cmbTransactionType.getItems().addAll("Deposit", "Withdraw");
    }

    private void clearOpenAccountFields() {
        cmbAccountType.setValue(null);
        txtInitialDeposit.clear();
        lblMinDeposit.setText("Select account type to see requirements");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
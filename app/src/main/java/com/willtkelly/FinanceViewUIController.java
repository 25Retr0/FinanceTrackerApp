package com.willtkelly;

import java.beans.EventHandler;
import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FinanceViewUIController {

    private final TransactionService ts;

    // Balance Label FXML
    @FXML private Label balanceValueLabel;

    // TableView FXML
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, Number> idColumn;
    @FXML private TableColumn<Transaction, Number> amountColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, LocalDate> dateColumn;
    @FXML private TableColumn<Transaction, String> descriptionColumn;

    public FinanceViewUIController(TransactionService ts) {
        this.ts = ts;
    }


    @FXML
    private void initialize() {
        // Initialise Table Columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Load and Display Data 
        List<Account> allAccs = ts.getAllAccounts();

        if (allAccs.size() == 0) {
            System.err.println("No Accounts Available...");
            return;
        }

        Account acc = allAccs.getFirst();
        ts.setCurrentAccount(acc);
        List<Transaction> trans = acc.getTransactions();

        ObservableList<Transaction> shownTransactions = FXCollections.observableArrayList(trans);
        transactionTable.setItems(shownTransactions);

        // Display Total Balance
        double balance = ts.calculateTotalBalance();
        balanceValueLabel.setText(String.format("$%,.2f", balance));

        System.out.println("Application ready.");
    }

    public void addDataToTable(Transaction t) {
        // add any new data to the table view to be displayed
        this.transactionTable.getItems().add(t);
    }

    public void updateTotalBalanceLabel() {
        String amount = String.format("%,.2f", this.ts.calculateTotalBalance());
        balanceValueLabel.setText(amount);
    }

    public void onClickAddTransaction() {
        // TODO: Create UI for Adding a Transaction
        
        Transaction t = new Transaction(20, Category.GROCERIES, "Weekly Groceries");
        Account a = this.ts.getCurrentAccount();
        this.ts.addTransaction(a.getName(), t);
        addDataToTable(t);
        updateTotalBalanceLabel();
        System.out.println("Adding Transaction");
    }

    public void onClickAddAccount() {
        // TODO: Create UI for Adding an Account
        
        System.out.println("Adding Account");
    }

}

package com.willtkelly;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FinanceViewUIController {

    private final TransactionService ts;
    private List<String> accountNames;

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
        this.accountNames = new ArrayList<>();
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

        for (Account account : allAccs) {
            this.accountNames.add(account.getName());
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
        this.transactionTable.getItems().addFirst(t);
    }

    public void updateTotalBalanceLabel() {
        String amount = String.format("%,.2f", this.ts.calculateTotalBalance());
        balanceValueLabel.setText(amount);
    }

    public void onClickAddTransaction() {
        ViewPopupAddUpdate viewPopup = new ViewPopupAddUpdate(this.accountNames);
        viewPopup.display();

        Transaction t = viewPopup.getSubmittedTransaction();
        if (t == null) { return; }

        String accountName = viewPopup.getSubmittedAccountName();
        Account acc = this.ts.getAccount(accountName);

        //Account a = this.ts.getCurrentAccount();
        this.ts.addTransaction(acc.getName(), t);
        addDataToTable(t);
        updateTotalBalanceLabel();
        System.out.println("Adding Transaction");
        
    }

    public void onClickAddAccount() {
        // TODO: Create UI for Adding an Account
        
        System.out.println("Adding Account");
    }

}

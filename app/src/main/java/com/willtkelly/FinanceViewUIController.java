package com.willtkelly;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sun.glass.ui.Menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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

    @FXML private Label currentAccountLabel;
    @FXML private ListView<String> accountsList;  
 
    private ContextMenu transactionRowMenu;

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
            this.accountsList.getItems().add(account.getName());
        }

        // NOTE: Move to function
        Account acc = allAccs.getFirst();
        ts.setCurrentAccount(acc);
        this.currentAccountLabel.setText("Account: " + acc.getName());

        // NOTE: Move to function
        List<Transaction> trans = acc.getTransactions();
        ObservableList<Transaction> shownTransactions = FXCollections.observableArrayList(trans);
        transactionTable.setItems(shownTransactions);

        // Display Total Balance
        double balance = ts.calculateTotalBalance();
        balanceValueLabel.setText(String.format("$%,.2f", balance));


        // Add listener for Accounts List View
        accountsList.getSelectionModel().selectFirst();
        accountsList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
            // NOTE: Move to function
            if (newValue != null) {
                System.out.println("Selected item: " + newValue);
                this.currentAccountLabel.setText("Account: " + newValue); 
                ts.setCurrentAccount(ts.getAccount(newValue));
                List<Transaction> t = ts.getCurrentAccount().getTransactions();
                ObservableList<Transaction> tableData = FXCollections.observableArrayList(t);
                transactionTable.setItems(tableData);
            }
        });

        // Add Context Menus to TableView
        setupTransactionTableContextMenu();

        System.out.println("Application ready.");
    }

    private void setupTransactionTableContextMenu() {
        transactionRowMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit Transaction");
        editItem.setOnAction(event -> {
            Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
            if (selectedTransaction != null) {
                System.out.println("Editing transaction: " + selectedTransaction);
                // TODO: Implement logic to open Edit/Update Popup
            }
        });

        MenuItem deleteItem = new MenuItem("Delete Transaction");
        deleteItem.setOnAction(event -> {
            Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
            if (selectedTransaction != null) {
                System.out.println("Deleting transaction: " + selectedTransaction);
                // TODO: Implement logic to delete transaction
                
                // Remove from ts
                // NOTE: When removing from ts add to remove from database
                transactionTable.getItems().remove(selectedTransaction);
                updateTotalBalanceLabel();
            }
        });

        transactionRowMenu.getItems().addAll(editItem, deleteItem);

        transactionTable.setRowFactory(tv -> {
            final TableRow<Transaction> row = new TableRow<>();

            row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    row.setContextMenu(null);
                } else {
                    row.setContextMenu(transactionRowMenu);
                }
            });
            return row;
        });
    }

    private void setupAccountsListContextMenu() {
        // TODO:
        return;
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
        ViewPopupAddAccount viewPopup = new ViewPopupAddAccount();
        viewPopup.display();

        Account acc = viewPopup.getSubmittedAccount();
        if (acc == null) { return; }

        this.ts.addAccount(acc);
        
        // Add to List view
        accountsList.getItems().add(acc.getName());
        
        updateTotalBalanceLabel();
        
        System.out.println("Adding Account");
    }

}
